package freddy.castro.mipokedex.ui.screens

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import freddy.castro.mipokedex.R
import freddy.castro.mipokedex.data.pokemonList
import freddy.castro.mipokedex.ui.components.FavoritesRow
import freddy.castro.mipokedex.ui.components.PokedexGrid
import freddy.castro.mipokedex.ui.theme.Dimens
import freddy.castro.mipokedex.ui.theme.MiPokedexTheme

/** Pantalla del menú: los favoritos en una fila arriba y todos los Pokémon en una tabla abajo. */
@Composable
fun MenuPokedexScreen(innerPadding: PaddingValues) {
    // La lista no cambia, así que los favoritos se filtran una sola vez
    val favoriteList = remember { pokemonList.filter { it.favorite } }
    Column(modifier = Modifier.padding(innerPadding)) {
        SectionLabel(R.string.label_favorites)
        FavoritesRow(favoriteList)
        SectionLabel(R.string.label_all_pokemons)
        PokedexGrid(pokemonList)
    }
}

/** Etiqueta que encabeza cada sección de la pantalla. */
@Composable
private fun SectionLabel(@StringRes text: Int) {
    Text(
        text = stringResource(text),
        modifier = Modifier.padding(
            start = Dimens.MarginMedium,
            top = Dimens.MarginMedium,
            end = Dimens.MarginMedium,
        ),
        style = MaterialTheme.typography.titleMedium,
    )
}

@Preview(showBackground = true)
@Composable
private fun MenuPokedexScreenPreview() {
    MiPokedexTheme {
        MenuPokedexScreen(PaddingValues())
    }
}
