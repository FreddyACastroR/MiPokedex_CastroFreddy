package freddy.castro.mipokedex.ui.viewmodel

import freddy.castro.mipokedex.data.pokemonList
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/** Con probabilidad 1 la pokébola siempre atrapa y con 0 el Pokémon siempre escapa. */
class PokemonViewModelTest {

    private fun viewModel(captureChance: Float = 1f) =
        PokemonViewModel(pokemonList, captureChance)

    @Test
    fun favorites_onlyContainsFavoritePokemon() {
        val favorites = viewModel().favorites

        assertEquals(pokemonList.count { it.favorite }, favorites.size)
        assertTrue(favorites.all { it.favorite })
    }

    @Test
    fun toggleFavorite_addsAndRemovesFromFavorites() {
        val viewModel = viewModel()
        val bulbasaur = pokemonList.first()

        viewModel.toggleFavorite(bulbasaur.number)
        assertTrue(viewModel.favorites.any { it.number == bulbasaur.number })
        assertTrue(viewModel.detailOf(bulbasaur.number)!!.pokemon.favorite)

        viewModel.toggleFavorite(bulbasaur.number)
        assertFalse(viewModel.favorites.any { it.number == bulbasaur.number })
    }

    @Test
    fun detailOf_wrapsAroundAtTheEnds() {
        val viewModel = viewModel()

        val first = viewModel.detailOf(pokemonList.first().number)!!
        assertEquals(pokemonList.last(), first.previous)
        assertEquals(pokemonList[1], first.next)

        val last = viewModel.detailOf(pokemonList.last().number)!!
        assertEquals(pokemonList.first(), last.next)
    }

    @Test
    fun detailOf_unknownNumber_isNull() {
        assertNull(viewModel().detailOf(-1))
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
}
