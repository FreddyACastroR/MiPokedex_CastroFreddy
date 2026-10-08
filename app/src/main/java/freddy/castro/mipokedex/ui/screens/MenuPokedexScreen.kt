package freddy.castro.mipokedex.ui.screens

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import freddy.castro.mipokedex.ui.theme.Blue
import freddy.castro.mipokedex.ui.theme.Dimens
import freddy.castro.mipokedex.ui.theme.Green
import freddy.castro.mipokedex.ui.theme.LightBlue
import freddy.castro.mipokedex.ui.theme.LightGreen
import freddy.castro.mipokedex.ui.theme.MiPokedexTheme

/**
 * Pantalla del menú: los favoritos en una fila arriba y todos los Pokémon abajo, en lista
 * o en tabla según el interruptor. Al tocar cualquiera se avisa con [onPokemonClick].
 */
@Composable
fun MenuPokedexScreen(innerPadding: PaddingValues, onPokemonClick: (Pokemon) -> Unit) {
    // La lista no cambia, así que los favoritos se filtran una sola vez
    val favoriteList = remember { pokemonList.filter { it.favorite } }
    // Saveable para que la vista elegida siga ahí al volver del detalle o girar la pantalla
    var grid by rememberSaveable { mutableStateOf(false) }
    Column(modifier = Modifier.padding(innerPadding)) {
        SectionLabel(R.string.label_favorites, Modifier.padding(top = Dimens.MarginMedium))
        FavoritesRow(favoriteList, onPokemonClick)
        Row(verticalAlignment = Alignment.CenterVertically) {
            SectionLabel(R.string.label_all_pokemons, Modifier.weight(1f))
            ViewSwitch(
                grid = grid,
                onGridChange = { grid = it },
                modifier = Modifier.padding(end = Dimens.MarginMedium),
            )
        }
        if (grid) {
            PokedexGrid(pokemonList, onPokemonClick)
        } else {
            MenuPokedex(pokemonList, onPokemonClick)
        }
    }
}

/** Etiqueta que encabeza cada sección de la pantalla. */
@Composable
private fun SectionLabel(@StringRes text: Int, modifier: Modifier = Modifier) {
    Text(
        text = stringResource(text),
        modifier = modifier.padding(horizontal = Dimens.MarginMedium),
        style = MaterialTheme.typography.titleMedium,
    )
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

@Preview(showBackground = true)
@Composable
private fun MenuPokedexScreenPreview() {
    MiPokedexTheme {
        MenuPokedexScreen(PaddingValues(), onPokemonClick = {})
    }
}
