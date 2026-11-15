package freddy.castro.mipokedex.data.sqlite

import android.database.Cursor
import freddy.castro.mipokedex.data.interfaces.IPokemonDAO
import freddy.castro.mipokedex.domain.Pokemon
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PokemonDAO(private val helper: DatabaseHelper) : IPokemonDAO {

    private val mapper = PokemonMapper()

    override suspend fun insertPokemon(pokemon: Pokemon) {
        withContext(Dispatchers.IO) {
            val db = helper.writableDatabase
            db.insert(Contract.Pokemons.TABLE_NAME, null, mapper.getContentValues(pokemon))
            db.close()
        }
    }

    override suspend fun getAllPokemons(): List<Pokemon> = withContext(Dispatchers.IO) {
        val db = helper.readableDatabase
        val query = "SELECT * FROM ${Contract.Pokemons.TABLE_NAME}"
        readPokemons(db.rawQuery(query, null))
    }

    override suspend fun getPokemonById(id: Int): Pokemon? = withContext(Dispatchers.IO) {
        val db = helper.readableDatabase
        val cursor = db.query(
            Contract.Pokemons.TABLE_NAME,
            null,
            "${Contract.Pokemons.COLUMN_ID} = ?",
            arrayOf(id.toString()),
            null,
            null,
            null,
        )
        cursor.use { if (it.moveToFirst()) mapper.getPokemon(it) else null }
    }

    override suspend fun getFavoritePokemons(): List<Pokemon> = emptyList()

    override suspend fun getPokemonsByType(type: String): List<Pokemon> =
        withContext(Dispatchers.IO) {
            val db = helper.readableDatabase
            val cursor = db.query(
                Contract.Pokemons.TABLE_NAME,
                null,
                "${Contract.Pokemons.COLUMN_NAME_TYPE} LIKE ?",
                arrayOf("%$type%"),
                null,
                null,
                null,
            )
            readPokemons(cursor)
        }

    override suspend fun updatePokemon(pokemon: Pokemon) {
        withContext(Dispatchers.IO) {
            val db = helper.writableDatabase
            db.update(
                Contract.Pokemons.TABLE_NAME,
                mapper.getContentValues(pokemon),
                "${Contract.Pokemons.COLUMN_NAME_NUMBER} = ?",
                arrayOf(pokemon.number.toString()),
            )
            db.close()
        }
    }

    override suspend fun deletePokemon(pokemon: Pokemon) {
        withContext(Dispatchers.IO) {
            val db = helper.writableDatabase
            db.delete(
                Contract.Pokemons.TABLE_NAME,
                "${Contract.Pokemons.COLUMN_NAME_NUMBER} = ?",
                arrayOf(pokemon.number.toString()),
            )
            db.close()
        }
    }

    private fun readPokemons(cursor: Cursor): List<Pokemon> = cursor.use {
        buildList {
            while (it.moveToNext()) {
                add(mapper.getPokemon(it))
            }
        }
    }
}
