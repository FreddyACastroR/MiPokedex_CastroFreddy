package freddy.castro.mipokedex.data.sqlite

import android.content.ContentValues
import android.database.Cursor
import freddy.castro.mipokedex.R
import freddy.castro.mipokedex.domain.Pokemon

class PokemonMapper {

    fun getPokemon(cursor: Cursor): Pokemon {
        val num = cursor.getInt(cursor.getColumnIndexOrThrow(Contract.Pokemons.COLUMN_NAME_NUMBER))
        val name = cursor.getString(
            cursor.getColumnIndexOrThrow(Contract.Pokemons.COLUMN_NAME_NAME),
        ).orEmpty()
        val description = cursor.getString(
            cursor.getColumnIndexOrThrow(Contract.Pokemons.COLUMN_NAME_DESCRIPTION),
        ).orEmpty()
        val type = cursor.getString(
            cursor.getColumnIndexOrThrow(Contract.Pokemons.COLUMN_NAME_TYPE),
        ).orEmpty()
        return Pokemon(
            name = name,
            number = num,
            type = type,
            description = description,
            height = 0f,
            weight = 0f,
            favorite = false,
            ability = "",
            image = R.drawable.wtp,
        )
    }

    fun getContentValues(pokemon: Pokemon): ContentValues = ContentValues().apply {
        put(Contract.Pokemons.COLUMN_NAME_NUMBER, pokemon.number)
        put(Contract.Pokemons.COLUMN_NAME_NAME, pokemon.name)
        put(Contract.Pokemons.COLUMN_NAME_DESCRIPTION, pokemon.description)
        put(Contract.Pokemons.COLUMN_NAME_TYPE, pokemon.type)
    }
}
