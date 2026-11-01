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

/**
 * Pantalla del menú: los favoritos en una fila arriba y todos los Pokémon abajo, en lista
 * o en tabla según [grid]. Al tocar cualquiera se avisa con [onPokemonClick], el
 * interruptor avisa con [onGridChange], el botón flotante con [onHuntClick] y el de arriba a
 * la derecha con [onLogoutClick].
 */
@Composable
fun MenuPokedexScreen(
    innerPadding: PaddingValues,
    pokemonList: List<Pokemon>,
    favoriteList: List<Pokemon>,
    grid: Boolean,
    onGridChange: (Boolean) -> Unit,
    onPokemonClick: (Pokemon) -> Unit,
    onHuntClick: () -> Unit,
    onLogoutClick: () -> Unit,
) {
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
                LogoutButton(
                    onClick = onLogoutClick,
                    modifier = Modifier.padding(end = Dimens.MarginSmall),
                )
            }
            if (favoriteList.isEmpty()) {
                // Sin favoritos la fila quedaría vacía; mejor se explica cómo agregarlos
                Text(
                    text = stringResource(R.string.favorites_empty),
                    modifier = Modifier.padding(Dimens.MarginMedium),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium,
                )
            } else {
                FavoritesRow(favoriteList, onPokemonClick)
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
                PokedexGrid(pokemonList, onPokemonClick, bottomPadding = Dimens.FabSpace)
            } else {
                MenuPokedex(pokemonList, onPokemonClick, bottomPadding = Dimens.FabSpace)
            }
        }
        HuntButton(
            onClick = onHuntClick,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(Dimens.MarginMedium),
        )
    }
}

/** Interruptor de la vista: apagado muestra la lista y encendido la tabla. */
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
        // El ícono del círculo dice qué vista está puesta
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

/** Botón para cerrar la sesión, con letras rojas como el de iniciar sesión. */
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

/** Botón flotante amarillo, como la Pokédex, que lleva a la cacería. */
@Composable
private fun HuntButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    ExtendedFloatingActionButton(
        onClick = onClick,
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.primary,
        // onPrimary es blanco y no se lee sobre el amarillo
        contentColor = MaterialTheme.colorScheme.onSurface,
        icon = {
            // Image y no Icon: la pokébola conserva sus colores
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
            onHuntClick = {},
            onLogoutClick = {},
        )
    }
}
