package freddy.castro.mipokedex.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import freddy.castro.mipokedex.data.pokemonList
import freddy.castro.mipokedex.domain.Pokemon
import freddy.castro.mipokedex.ui.theme.MiPokedexTheme

/** Lista de Pokémon: solo compone las filas que están a la vista. */
@Composable
fun MenuPokedex(pokemonList: List<Pokemon>, innerPadding: PaddingValues) {
    LazyColumn(modifier = Modifier.padding(innerPadding)) {
        // La llave le deja a Compose reutilizar cada fila aunque la lista cambie de orden
        items(pokemonList, key = { it.number }) { pokemon ->
            PokemonRow(pokemon)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MenuPokedexPreview() {
    MiPokedexTheme {
        MenuPokedex(pokemonList, PaddingValues())
    }
}
