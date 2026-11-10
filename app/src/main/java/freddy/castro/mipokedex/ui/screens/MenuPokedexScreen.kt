package freddy.castro.mipokedex.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import freddy.castro.mipokedex.R
import freddy.castro.mipokedex.data.pokemonList
import freddy.castro.mipokedex.domain.Pokemon
import freddy.castro.mipokedex.ui.components.FavoritesRow
import freddy.castro.mipokedex.ui.components.MenuPokedex
import freddy.castro.mipokedex.ui.components.PokedexGrid
import freddy.castro.mipokedex.ui.components.SectionLabel
import freddy.castro.mipokedex.ui.theme.Blue
import freddy.castro.mipokedex.ui.theme.Dimens
import freddy.castro.mipokedex.ui.theme.Green
import freddy.castro.mipokedex.ui.theme.LightBlue
import freddy.castro.mipokedex.ui.theme.LightGreen
import freddy.castro.mipokedex.ui.theme.MiPokedexTheme

@Composable
fun MenuPokedexScreen(
    innerPadding: PaddingValues,
    pokemonList: List<Pokemon>,
    favoriteList: List<Pokemon>,
    grid: Boolean,
    onGridChange: (Boolean) -> Unit,
    onPokemonClick: (Int) -> Unit,
    onGuessClick: () -> Unit,
    onHuntClick: () -> Unit,
    onProfileClick: () -> Unit,
    onLogoutClick: () -> Unit,
) {
    val openPokemon = { pokemon: Pokemon -> onPokemonClick(pokemon.number) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
    ) {
        Column {
            Row(
                modifier = Modifier.padding(top = Dimens.MarginSmall),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                SectionLabel(
                    text = stringResource(R.string.label_favorites),
                    modifier = Modifier.weight(1f),
                )
                ProfileButton(onClick = onProfileClick)
                LogoutButton(
                    onClick = onLogoutClick,
                    modifier = Modifier.padding(end = Dimens.MarginSmall),
                )
            }
            if (favoriteList.isEmpty()) {
                Text(
                    text = stringResource(R.string.favorites_empty),
                    modifier = Modifier.padding(Dimens.MarginMedium),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium,
                )
            } else {
                FavoritesRow(favoriteList, openPokemon)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                SectionLabel(
                    text = stringResource(R.string.label_all_pokemons),
                    modifier = Modifier.weight(1f),
                )
                ViewSwitch(
                    grid = grid,
                    onGridChange = onGridChange,
                    modifier = Modifier.padding(end = Dimens.MarginMedium),
                )
            }
            if (grid) {
                PokedexGrid(pokemonList, openPokemon, bottomPadding = Dimens.FabSpace)
            } else {
                MenuPokedex(pokemonList, openPokemon, bottomPadding = Dimens.FabSpace)
            }
        }
        GuessButton(
            onClick = onGuessClick,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(Dimens.MarginMedium),
        )
        HuntButton(
            onClick = onHuntClick,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(Dimens.MarginMedium),
        )
    }
}

@Composable
private fun ViewSwitch(
    grid: Boolean,
    onGridChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Switch(
        checked = grid,
        onCheckedChange = onGridChange,
        modifier = modifier,
        thumbContent = {
            Icon(
                painter = painterResource(if (grid) R.drawable.ic_grid else R.drawable.ic_list),
                contentDescription = stringResource(
                    if (grid) R.string.cd_view_grid else R.string.cd_view_list,
                ),
                modifier = Modifier.size(SwitchDefaults.IconSize),
            )
        },
        colors = SwitchDefaults.colors(
            checkedThumbColor = Green,
            checkedTrackColor = LightGreen,
            uncheckedThumbColor = Blue,
            uncheckedTrackColor = LightBlue,
            uncheckedBorderColor = Color.Transparent,
        ),
    )
}

@Composable
private fun ProfileButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    IconButton(onClick = onClick, modifier = modifier) {
        Icon(
            painter = painterResource(R.drawable.ic_person),
            contentDescription = stringResource(R.string.cd_profile),
            tint = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Composable
private fun LogoutButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = MaterialTheme.colorScheme.secondary,
        ),
    ) {
        Text(stringResource(R.string.logout))
    }
}

@Composable
private fun GuessButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    ExtendedFloatingActionButton(
        onClick = onClick,
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.secondary,
        contentColor = MaterialTheme.colorScheme.onSecondary,
        icon = {
            Icon(
                painter = painterResource(R.drawable.ic_question),
                contentDescription = null,
                modifier = Modifier.size(Dimens.FabIconSize),
            )
        },
        text = { Text(stringResource(R.string.go_guess)) },
    )
}

@Composable
private fun HuntButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    ExtendedFloatingActionButton(
        onClick = onClick,
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onSurface,
        icon = {
            Image(
                painter = painterResource(R.drawable.ic_pokeball),
                contentDescription = null,
                modifier = Modifier.size(Dimens.FabIconSize),
            )
        },
        text = { Text(stringResource(R.string.go_hunt)) },
    )
}

@Preview(showBackground = true)
@Composable
private fun MenuPokedexScreenPreview() {
    MiPokedexTheme {
        MenuPokedexScreen(
            innerPadding = PaddingValues(),
            pokemonList = pokemonList,
            favoriteList = pokemonList.filter { it.favorite },
            grid = false,
            onGridChange = {},
            onPokemonClick = {},
            onGuessClick = {},
            onHuntClick = {},
            onProfileClick = {},
            onLogoutClick = {},
        )
    }
}
