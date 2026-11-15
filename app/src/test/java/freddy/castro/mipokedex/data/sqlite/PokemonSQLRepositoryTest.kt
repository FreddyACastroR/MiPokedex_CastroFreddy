package freddy.castro.mipokedex.data.sqlite

import freddy.castro.mipokedex.data.FakePokemonDAO
import freddy.castro.mipokedex.data.bulbasaur
import freddy.castro.mipokedex.data.pokemonList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PokemonSQLRepositoryTest {

    private val dao = FakePokemonDAO()

    private val repository = PokemonSQLRepository(dao)

    private val charmander = pokemonList.first { it.name == "Charmander" }

    @Test
    fun addPokemon_savesItThroughTheDao() = runTest {
        repository.addPokemon(bulbasaur)

        assertEquals(listOf(bulbasaur), repository.getPokemons())
    }

    @Test
    fun addPokemon_withoutName_isNotSaved() = runTest {
        repository.addPokemon(bulbasaur.copy(name = "  "))

        assertTrue(dao.pokemons.isEmpty())
    }

    @Test
    fun addPokemon_withoutAValidNumber_isNotSaved() = runTest {
        repository.addPokemon(bulbasaur.copy(number = 0))

        assertTrue(dao.pokemons.isEmpty())
    }

    @Test
    fun getPokemonsByType_ignoresTheSpacesAround() = runTest {
        repository.addPokemon(bulbasaur)
        repository.addPokemon(charmander)

        assertEquals(listOf(bulbasaur), repository.getPokemonsByType(" Poison "))
    }

    @Test
    fun updatePokemon_replacesTheOneWithThatNumber() = runTest {
        val renamed = bulbasaur.copy(name = "Bulba")
        repository.addPokemon(bulbasaur)

        repository.updatePokemon(renamed)

        assertEquals(listOf(renamed), repository.getPokemons())
    }

    @Test
    fun updatePokemon_withoutName_keepsTheSavedOne() = runTest {
        repository.addPokemon(bulbasaur)

        repository.updatePokemon(bulbasaur.copy(name = ""))

        assertEquals(listOf(bulbasaur), repository.getPokemons())
    }

    @Test
    fun deletePokemon_removesTheOneWithThatId() = runTest {
        repository.addPokemon(bulbasaur)
        repository.addPokemon(charmander)

        repository.deletePokemon(1)

        assertEquals(listOf(charmander), repository.getPokemons())
    }

    @Test
    fun deletePokemon_withAnUnknownId_removesNothing() = runTest {
        repository.addPokemon(bulbasaur)

        repository.deletePokemon(99)

        assertEquals(listOf(bulbasaur), repository.getPokemons())
    }
}
