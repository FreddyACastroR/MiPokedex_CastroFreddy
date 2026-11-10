package freddy.castro.mipokedex.ui.viewmodel

import freddy.castro.mipokedex.data.PokemonRepository
import freddy.castro.mipokedex.data.pokemonList
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class PokemonViewModelTest {

    private fun viewModel(captureChance: Float = 1f) =
        PokemonViewModel(PokemonRepository(pokemonList), captureChance)

    @Test
    fun favorites_onlyContainsFavoritePokemon() {
        val favorites = viewModel().favorites

        assertEquals(pokemonList.count { it.favorite }, favorites.size)
        assertTrue(favorites.all { it.favorite })
    }

    @Test
    fun favorites_followTheFavoritesOfTheRepository() {
        val repository = PokemonRepository(pokemonList)
        val viewModel = PokemonViewModel(repository)
        val bulbasaur = pokemonList.first()

        repository.toggleFavorite(bulbasaur.number)
        assertTrue(viewModel.favorites.any { it.number == bulbasaur.number })

        repository.toggleFavorite(bulbasaur.number)
        assertFalse(viewModel.favorites.any { it.number == bulbasaur.number })
    }

    @Test
    fun searchPokemon_showsAWildPokemonFromTheList() {
        val viewModel = viewModel()

        viewModel.searchPokemon()

        val wild = viewModel.pokedexState.wildPokemon
        assertNotNull(wild)
        assertTrue(wild in pokemonList)
    }

    @Test
    fun capturePokemon_success_addsItToTheTeam() {
        val viewModel = viewModel(captureChance = 1f)
        viewModel.searchPokemon()
        val wild = viewModel.pokedexState.wildPokemon

        viewModel.capturePokemon()

        val state = viewModel.pokedexState
        assertNull(state.wildPokemon)
        assertEquals(wild, state.lastCaptured)
        assertNull(state.escaped)
        assertEquals(listOf(wild), state.team)
    }

    @Test
    fun capturePokemon_failure_letsItEscape() {
        val viewModel = viewModel(captureChance = 0f)
        viewModel.searchPokemon()
        val wild = viewModel.pokedexState.wildPokemon

        viewModel.capturePokemon()

        val state = viewModel.pokedexState
        assertNull(state.wildPokemon)
        assertNull(state.lastCaptured)
        assertEquals(wild, state.escaped)
        assertTrue(state.team.isEmpty())
    }

    @Test
    fun capturePokemon_withoutWildPokemon_doesNothing() {
        val viewModel = viewModel()

        viewModel.capturePokemon()

        assertEquals(PokedexState(), viewModel.pokedexState)
    }

    @Test
    fun team_putsTheNewestCaptureFirst() {
        val viewModel = viewModel(captureChance = 1f)

        viewModel.searchPokemon()
        viewModel.capturePokemon()
        viewModel.searchPokemon()
        val second = viewModel.pokedexState.wildPokemon
        viewModel.capturePokemon()

        assertEquals(2, viewModel.pokedexState.team.size)
        assertEquals(second, viewModel.pokedexState.team.first())
    }

    @Test
    fun searchPokemon_clearsThePreviousResult() {
        val viewModel = viewModel(captureChance = 0f)
        viewModel.searchPokemon()
        viewModel.capturePokemon()

        viewModel.searchPokemon()

        assertNull(viewModel.pokedexState.escaped)
        assertNull(viewModel.pokedexState.lastCaptured)
    }

    @Test
    fun searchPokemon_withoutPokemon_doesNothing() {
        val viewModel = PokemonViewModel(PokemonRepository(emptyList()))

        viewModel.searchPokemon()

        assertEquals(PokedexState(), viewModel.pokedexState)
    }

    @Test
    fun whoIsThat_startsWithAMysteryAndFourDifferentOptions() {
        val state = viewModel().whoIsThatState

        val mystery = state.mystery
        assertNotNull(mystery)
        assertTrue(mystery in pokemonList)
        assertEquals(4, state.options.size)
        assertEquals(4, state.options.map { it.number }.distinct().size)
        assertTrue(mystery in state.options)
        assertFalse(state.revealed)
        assertEquals(0, state.rounds)
    }

    @Test
    fun guessPokemon_right_countsAHit() {
        val viewModel = viewModel()
        val mystery = viewModel.whoIsThatState.mystery!!

        viewModel.guessPokemon(mystery)

        val state = viewModel.whoIsThatState
        assertTrue(state.revealed)
        assertTrue(state.isCorrect)
        assertEquals(1, state.hits)
        assertEquals(1, state.rounds)
    }

    @Test
    fun guessPokemon_wrong_countsTheRoundButNotAHit() {
        val viewModel = viewModel()
        val state = viewModel.whoIsThatState
        val wrong = state.options.first { it != state.mystery }

        viewModel.guessPokemon(wrong)

        val answered = viewModel.whoIsThatState
        assertTrue(answered.revealed)
        assertFalse(answered.isCorrect)
        assertEquals(0, answered.hits)
        assertEquals(1, answered.rounds)
    }

    @Test
    fun guessPokemon_onlyTheFirstAnswerCounts() {
        val viewModel = viewModel()
        val state = viewModel.whoIsThatState
        val wrong = state.options.first { it != state.mystery }

        viewModel.guessPokemon(wrong)
        viewModel.guessPokemon(state.mystery!!)

        assertEquals(wrong, viewModel.whoIsThatState.guess)
        assertEquals(0, viewModel.whoIsThatState.hits)
        assertEquals(1, viewModel.whoIsThatState.rounds)
    }

    @Test
    fun guessPokemon_stillRight_afterMarkingItAsFavorite() {
        val repository = PokemonRepository(pokemonList)
        val viewModel = PokemonViewModel(repository)
        val mystery = viewModel.whoIsThatState.mystery!!

        repository.toggleFavorite(mystery.number)
        viewModel.guessPokemon(repository.getPokemon(mystery.number)!!)

        assertTrue(viewModel.whoIsThatState.isCorrect)
    }

    @Test
    fun nextMysteryPokemon_hidesAnotherOneAndKeepsTheScore() {
        val viewModel = viewModel()
        val first = viewModel.whoIsThatState.mystery!!
        viewModel.guessPokemon(first)

        viewModel.nextMysteryPokemon()

        val state = viewModel.whoIsThatState
        assertNotEquals(first.number, state.mystery!!.number)
        assertFalse(state.revealed)
        assertEquals(1, state.hits)
        assertEquals(1, state.rounds)
    }

    @Test
    fun whoIsThat_withFewPokemon_offersAllOfThem() {
        val two = pokemonList.take(2)
        val state = PokemonViewModel(PokemonRepository(two)).whoIsThatState

        assertEquals(two.toSet(), state.options.toSet())
    }

    @Test
    fun whoIsThat_withoutPokemon_hasNothingToGuess() {
        val viewModel = PokemonViewModel(PokemonRepository(emptyList()))

        viewModel.nextMysteryPokemon()
        viewModel.guessPokemon(pokemonList.first())

        assertEquals(WhoIsThatPokemonState(), viewModel.whoIsThatState)
    }
}
