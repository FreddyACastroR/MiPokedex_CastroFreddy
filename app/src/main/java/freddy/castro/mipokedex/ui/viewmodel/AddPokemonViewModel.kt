package freddy.castro.mipokedex.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import freddy.castro.mipokedex.data.interfaces.IPokemonRepository
import freddy.castro.mipokedex.domain.Pokemon
import freddy.castro.mipokedex.pokedexApplication
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AddPokemonViewModel(private val pokemonRepository: IPokemonRepository) : ViewModel() {

    private val pokemonsMutable = MutableStateFlow<List<Pokemon>>(emptyList())

    val pokemons: StateFlow<List<Pokemon>> = pokemonsMutable.asStateFlow()

    var pokemonSaved by mutableStateOf(false)
        private set

    init {
        loadPokemons()
    }

    fun loadPokemons() {
        viewModelScope.launch {
            pokemonsMutable.value = pokemonRepository.getPokemons()
        }
    }

    fun savePokemon(name: String, type: String, number: Int, description: String) {
        val savingPokemon = Pokemon(
            name = name.trim(),
            number = number,
            type = type.trim(),
            description = description.trim(),
            height = 0f,
            weight = 0f,
            favorite = false,
            ability = "",
            image = -1,
        )
        viewModelScope.launch {
            pokemonRepository.addPokemon(savingPokemon)
            pokemonSaved = true
            loadPokemons()
        }
    }

    companion object {
        val Factory = viewModelFactory {
            initializer { AddPokemonViewModel(pokedexApplication.pokemonSQLRepository) }
        }
    }
}
