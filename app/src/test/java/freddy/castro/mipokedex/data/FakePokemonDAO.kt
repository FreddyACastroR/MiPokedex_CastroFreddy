package freddy.castro.mipokedex.data

import freddy.castro.mipokedex.data.interfaces.IPokemonDAO
import freddy.castro.mipokedex.domain.Pokemon

class FakePokemonDAO : IPokemonDAO {

    private val rows = mutableMapOf<Int, Pokemon>()

    private var nextId = 1

    val pokemons: List<Pokemon> get() = rows.values.toList()

    override suspend fun insertPokemon(pokemon: Pokemon) {
        rows[nextId++] = pokemon
    }

    override suspend fun getAllPokemons(): List<Pokemon> = pokemons

    override suspend fun getPokemonById(id: Int): Pokemon? = rows[id]

    override suspend fun getFavoritePokemons(): List<Pokemon> = emptyList()

    override suspend fun getPokemonsByType(type: String): List<Pokemon> =
        pokemons.filter { it.type.contains(type, ignoreCase = true) }

    override suspend fun updatePokemon(pokemon: Pokemon) {
        rows.replaceAll { _, stored -> if (stored.number == pokemon.number) pokemon else stored }
    }

    override suspend fun deletePokemon(pokemon: Pokemon) {
        rows.values.removeAll { it.number == pokemon.number }
    }
}
