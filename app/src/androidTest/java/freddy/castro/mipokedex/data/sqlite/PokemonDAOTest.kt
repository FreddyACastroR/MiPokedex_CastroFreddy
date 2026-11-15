package freddy.castro.mipokedex.data.sqlite

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import freddy.castro.mipokedex.R
import freddy.castro.mipokedex.data.bulbasaur
import freddy.castro.mipokedex.data.pokemonList
import freddy.castro.mipokedex.domain.Pokemon
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

private const val TEST_DATABASE = "pokedex_test.db"

@RunWith(AndroidJUnit4::class)
class PokemonDAOTest {

    private val context = ApplicationProvider.getApplicationContext<Context>()

    private val charmander = pokemonList.first { it.name == "Charmander" }

    private val gengar = pokemonList.first { it.name == "Gengar" }

    private lateinit var helper: DatabaseHelper

    private lateinit var dao: PokemonDAO

    @Before
    fun setUp() {
        context.deleteDatabase(TEST_DATABASE)
        helper = DatabaseHelper(context, TEST_DATABASE)
        dao = PokemonDAO(helper)
    }

    @After
    fun tearDown() {
        helper.close()
        context.deleteDatabase(TEST_DATABASE)
    }

    @Test
    fun getAllPokemons_withoutSavingAny_isEmpty() = runBlocking {
        assertTrue(dao.getAllPokemons().isEmpty())
    }

    @Test
    fun insertPokemon_keepsTheSavedColumnsAndDefaultsTheRest() = runBlocking {
        dao.insertPokemon(bulbasaur)

        assertEquals(listOf(stored(bulbasaur)), dao.getAllPokemons())
    }

    @Test
    fun insertPokemon_afterClosingTheDatabase_keepsAllOfThem() = runBlocking {
        dao.insertPokemon(bulbasaur)
        dao.insertPokemon(charmander)

        assertEquals(listOf(stored(bulbasaur), stored(charmander)), dao.getAllPokemons())
    }

    @Test
    fun getPokemonById_findsTheRowWithThatId() = runBlocking {
        dao.insertPokemon(bulbasaur)
        dao.insertPokemon(charmander)

        assertEquals(stored(charmander), dao.getPokemonById(2))
    }

    @Test
    fun getPokemonById_withAnUnknownId_isNull() = runBlocking {
        dao.insertPokemon(bulbasaur)

        assertNull(dao.getPokemonById(99))
    }

    @Test
    fun getPokemonsByType_findsPartOfACompositeType() = runBlocking {
        dao.insertPokemon(bulbasaur)
        dao.insertPokemon(charmander)
        dao.insertPokemon(gengar)

        assertEquals(listOf(stored(bulbasaur), stored(gengar)), dao.getPokemonsByType("Poison"))
    }

    @Test
    fun updatePokemon_changesTheRowWithThatNumber() = runBlocking {
        val renamed = bulbasaur.copy(name = "Bulba", description = "Otra descripción")
        dao.insertPokemon(bulbasaur)
        dao.insertPokemon(charmander)

        dao.updatePokemon(renamed)

        assertEquals(listOf(stored(renamed), stored(charmander)), dao.getAllPokemons())
    }

    @Test
    fun deletePokemon_removesOnlyTheRowWithThatNumber() = runBlocking {
        dao.insertPokemon(bulbasaur)
        dao.insertPokemon(charmander)

        dao.deletePokemon(bulbasaur)

        assertEquals(listOf(stored(charmander)), dao.getAllPokemons())
    }

    @Test
    fun getFavoritePokemons_isEmptyBecauseTheTableHasNoFavorites() = runBlocking {
        dao.insertPokemon(charmander)

        assertTrue(dao.getFavoritePokemons().isEmpty())
    }

    @Test
    fun theSavedPokemonsSurviveANewHelper() = runBlocking {
        dao.insertPokemon(bulbasaur)
        helper.close()

        helper = DatabaseHelper(context, TEST_DATABASE)

        assertEquals(listOf(stored(bulbasaur)), PokemonDAO(helper).getAllPokemons())
    }

    private fun stored(pokemon: Pokemon) = pokemon.copy(
        height = 0f,
        weight = 0f,
        favorite = false,
        ability = "",
        image = R.drawable.wtp,
    )
}
