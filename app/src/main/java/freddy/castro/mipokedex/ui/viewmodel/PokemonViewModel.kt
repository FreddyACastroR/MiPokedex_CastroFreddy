package freddy.castro.mipokedex.ui.viewmodel

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import freddy.castro.mipokedex.data.pokemonList
import freddy.castro.mipokedex.domain.Pokemon
import kotlin.random.Random

/** Probabilidad de que la pokébola atrape al Pokémon salvaje. */
private const val CAPTURE_CHANCE = 0.5f

/**
 * Estado de toda la Pokédex. Sobrevive a los giros de pantalla y lo comparten todas las
 * pantallas: un favorito marcado en el detalle aparece en el menú, y los capturados siguen
 * ahí al volver a la cacería. Las pantallas solo leen el estado y llaman a estas funciones.
 */
class PokemonViewModel(
    pokemons: List<Pokemon> = pokemonList,
    private val captureChance: Float = CAPTURE_CHANCE,
) : ViewModel() {

    // Lista observable: cuando cambia un Pokémon se recomponen las pantallas que la leen
    private val _pokemons = pokemons.toMutableStateList()
    val pokemons: List<Pokemon> get() = _pokemons

    // Solo se vuelve a filtrar cuando cambia la lista, no cada vez que se lee
    val favorites: List<Pokemon> by derivedStateOf { _pokemons.filter { it.favorite } }

    var pokedexState by mutableStateOf(PokedexState())
        private set

    /** El Pokémon con ese número y sus vecinos, o null si no existe. */
    fun detailOf(number: Int): PokemonDetailState? {
        val index = _pokemons.indexOfFirst { it.number == number }
        if (index == -1) return null
        // La lista da la vuelta: antes del primero va el último, y después del último el primero
        return PokemonDetailState(
            pokemon = _pokemons[index],
            previous = _pokemons[(index - 1).mod(_pokemons.size)],
            next = _pokemons[(index + 1).mod(_pokemons.size)],
        )
    }

    fun toggleFavorite(number: Int) {
        val index = _pokemons.indexOfFirst { it.number == number }
        if (index == -1) return
        // Pokemon es inmutable: se reemplaza por una copia para que la lista avise del cambio
        val pokemon = _pokemons[index]
        _pokemons[index] = pokemon.copy(favorite = !pokemon.favorite)
    }

    /** Busca en la hierba: aparece un Pokémon al azar y se olvida el intento anterior. */
    fun searchPokemon() {
        pokedexState = pokedexState.copy(
            wildPokemon = _pokemons.random(),
            lastCaptured = null,
            escaped = null,
        )
    }

    /** Lanza la pokébola: si lo atrapa se une al equipo y si no, huye. Sin Pokémon no hace nada. */
    fun capturePokemon() {
        val wild = pokedexState.wildPokemon ?: return
        pokedexState = if (Random.nextFloat() < captureChance) {
            pokedexState.copy(
                wildPokemon = null,
                lastCaptured = wild,
                team = listOf(wild) + pokedexState.team,
            )
        } else {
            pokedexState.copy(wildPokemon = null, escaped = wild)
        }
    }
}
