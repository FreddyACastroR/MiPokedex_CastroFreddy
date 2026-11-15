package freddy.castro.mipokedex.ui.viewmodel

import freddy.castro.mipokedex.data.FakePokemonDAO
import freddy.castro.mipokedex.data.bulbasaur
import freddy.castro.mipokedex.data.sqlite.PokemonSQLRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class AddPokemonViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository = PokemonSQLRepository(FakePokemonDAO())

    @Test
    fun pokemons_startWithTheOnesAlreadySaved() = runTest {
        repository.addPokemon(bulbasaur)

        val viewModel = AddPokemonViewModel(repository)

        assertEquals(listOf(bulbasaur), viewModel.pokemons.value)
    }

    @Test
    fun pokemonSaved_isFalseBeforeSavingOne() {
        assertFalse(AddPokemonViewModel(repository).pokemonSaved)
    }

    @Test
    fun savePokemon_savesItAndReloadsTheList() {
        val viewModel = AddPokemonViewModel(repository)

        viewModel.savePokemon("Pikachu", "Electric", 25, "Ratón eléctrico")

        val saved = viewModel.pokemons.value.single()
        assertEquals("Pikachu", saved.name)
        assertEquals("Electric", saved.type)
        assertEquals(25, saved.number)
        assertEquals("Ratón eléctrico", saved.description)
        assertTrue(viewModel.pokemonSaved)
    }

    @Test
    fun savePokemon_removesTheSpacesAroundTheText() {
        val viewModel = AddPokemonViewModel(repository)

        viewModel.savePokemon("  Pikachu ", " Electric ", 25, " Ratón eléctrico ")

        val saved = viewModel.pokemons.value.single()
        assertEquals("Pikachu", saved.name)
        assertEquals("Electric", saved.type)
        assertEquals("Ratón eléctrico", saved.description)
    }

    @Test
    fun savePokemon_keepsThePreviousOnes() {
        val viewModel = AddPokemonViewModel(repository)

        viewModel.savePokemon("Pikachu", "Electric", 25, "")
        viewModel.savePokemon("Gengar", "Ghost/Poison", 94, "")

        assertEquals(listOf("Pikachu", "Gengar"), viewModel.pokemons.value.map { it.name })
    }
}
