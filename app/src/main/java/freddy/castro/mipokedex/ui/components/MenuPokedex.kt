package freddy.castro.mipokedex.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import freddy.castro.mipokedex.data.pokemonList
import freddy.castro.mipokedex.domain.Pokemon
import freddy.castro.mipokedex.ui.theme.Dimens
import freddy.castro.mipokedex.ui.theme.MiPokedexTheme

private const val GRID_COLUMNS = 3

/**
 * Lista de Pokémon: solo compone las filas que están a la vista. [bottomPadding] deja
 * espacio después de la última fila para que un botón flotante no la tape.
 */
@Composable
fun MenuPokedex(
    pokemonList: List<Pokemon>,
    onPokemonClick: (Pokemon) -> Unit,
    bottomPadding: Dp = 0.dp,
) {
    LazyColumn(contentPadding = PaddingValues(bottom = bottomPadding)) {
        // La llave le deja a Compose reutilizar cada fila aunque la lista cambie de orden
        items(pokemonList, key = { it.number }) { pokemon ->
            PokemonRow(pokemon, onClick = { onPokemonClick(pokemon) })
        }
    }
}

/** Fila de favoritos: se desliza a los lados y solo compone los que están a la vista. */
@Composable
fun FavoritesRow(favoriteList: List<Pokemon>, onPokemonClick: (Pokemon) -> Unit) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = Dimens.MarginMedium),
        horizontalArrangement = Arrangement.spacedBy(Dimens.MarginMedium),
    ) {
        items(favoriteList, key = { it.number }) { pokemon ->
            FavoritePokemon(pokemon, onClick = { onPokemonClick(pokemon) })
        }
    }
}

/** Tabla de Pokémon de tres columnas; [bottomPadding] funciona igual que en [MenuPokedex]. */
@Composable
fun PokedexGrid(
    pokemonList: List<Pokemon>,
    onPokemonClick: (Pokemon) -> Unit,
    bottomPadding: Dp = 0.dp,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(GRID_COLUMNS),
        contentPadding = PaddingValues(
            start = Dimens.GridPaddingHorizontal,
            top = Dimens.GridPaddingVertical,
            end = Dimens.GridPaddingHorizontal,
            bottom = Dimens.GridPaddingVertical + bottomPadding,
        ),
        verticalArrangement = Arrangement.spacedBy(Dimens.GridSpacingVertical),
        horizontalArrangement = Arrangement.spacedBy(Dimens.GridSpacingHorizontal),
    ) {
        items(pokemonList, key = { it.number }) { pokemon ->
            PokemonCell(pokemon, onClick = { onPokemonClick(pokemon) })
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MenuPokedexPreview() {
    MiPokedexTheme {
        MenuPokedex(pokemonList, onPokemonClick = {})
    }
}

@Preview(showBackground = true)
@Composable
private fun FavoritesRowPreview() {
    MiPokedexTheme {
        FavoritesRow(pokemonList.filter { it.favorite }, onPokemonClick = {})
    }
}

@Preview(showBackground = true)
@Composable
private fun PokedexGridPreview() {
    MiPokedexTheme {
        PokedexGrid(pokemonList, onPokemonClick = {})
    }
}
