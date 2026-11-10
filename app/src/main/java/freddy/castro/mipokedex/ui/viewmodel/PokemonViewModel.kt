package freddy.castro.mipokedex.ui.viewmodel

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import freddy.castro.mipokedex.data.PokemonRepository
import freddy.castro.mipokedex.domain.Pokemon
import freddy.castro.mipokedex.pokedexApplication
import kotlin.random.Random

private const val CAPTURE_CHANCE = 0.5f

private const val GUESS_OPTIONS = 4

class PokemonViewModel(
    private val repository: PokemonRepository = PokemonRepository(),
    private val captureChance: Float = CAPTURE_CHANCE,
) : ViewModel() {

    val pokemons: List<Pokemon> get() = repository.pokemons

    val favorites: List<Pokemon> by derivedStateOf { repository.pokemons.filter { it.favorite } }

    var pokedexState by mutableStateOf(PokedexState())
        private set

    var whoIsThatState by mutableStateOf(WhoIsThatPokemonState())
        private set

    init {
        nextMysteryPokemon()
    }

    fun searchPokemon() {
        if (pokemons.isEmpty()) return
        pokedexState = pokedexState.copy(
            wildPokemon = pokemons.random(),
            lastCaptured = null,
            escaped = null,
        )
    }

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

    fun nextMysteryPokemon() {
        if (pokemons.isEmpty()) return
        val previous = whoIsThatState.mystery
        val mystery = pokemons.filter { it.number != previous?.number }
            .ifEmpty { pokemons }
            .random()
        val others = pokemons.filter { it.number != mystery.number }
            .shuffled()
            .take(GUESS_OPTIONS - 1)
        whoIsThatState = whoIsThatState.copy(
            mystery = mystery,
            options = (others + mystery).shuffled(),
            guess = null,
        )
    }

    fun guessPokemon(pokemon: Pokemon) {
        val state = whoIsThatState
        if (state.mystery == null || state.revealed) return
        val answered = state.copy(guess = pokemon, rounds = state.rounds + 1)
        whoIsThatState = if (answered.isCorrect) answered.copy(hits = state.hits + 1) else answered
    }

    companion object {
        val Factory = viewModelFactory {
            initializer { PokemonViewModel(pokedexApplication.pokemonRepository) }
        }
    }
}
