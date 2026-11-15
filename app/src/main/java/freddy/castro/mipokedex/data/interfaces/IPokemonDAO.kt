package freddy.castro.mipokedex.data.interfaces

import freddy.castro.mipokedex.domain.Pokemon

interface IPokemonDAO {

    suspend fun insertPokemon(pokemon: Pokemon)

    suspend fun getAllPokemons(): List<Pokemon>

    suspend fun getPokemonById(id: Int): Pokemon?

    suspend fun getFavoritePokemons(): List<Pokemon>

    suspend fun getPokemonsByType(type: String): List<Pokemon>

    suspend fun updatePokemon(pokemon: Pokemon)

    suspend fun deletePokemon(pokemon: Pokemon)
}
