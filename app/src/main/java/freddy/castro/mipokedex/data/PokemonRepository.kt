package freddy.castro.mipokedex.data

import androidx.compose.runtime.toMutableStateList
import freddy.castro.mipokedex.domain.Pokemon

class PokemonRepository(pokemons: List<Pokemon> = pokemonList) {

    private val _pokemons = pokemons.toMutableStateList()
    val pokemons: List<Pokemon> get() = _pokemons

    fun getPokemon(number: Int): Pokemon? = _pokemons.find { it.number == number }

    fun toggleFavorite(number: Int) {
        val index = _pokemons.indexOfFirst { it.number == number }
        if (index == -1) return
        val pokemon = _pokemons[index]
        _pokemons[index] = pokemon.copy(favorite = !pokemon.favorite)
    }
}
