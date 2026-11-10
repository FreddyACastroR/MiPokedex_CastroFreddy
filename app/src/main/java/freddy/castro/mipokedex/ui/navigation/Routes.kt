package freddy.castro.mipokedex.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
data object Login

@Serializable
data object PokemonList

@Serializable
data object PokemonHunt

@Serializable
data object WhoIsThatPokemon

@Serializable
data object Profile

@Serializable
data class PokemonDetail(val pokemon: Int)
