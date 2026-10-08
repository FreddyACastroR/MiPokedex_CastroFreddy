package freddy.castro.mipokedex.ui.navigation

import kotlinx.serialization.Serializable

/** Menú con todos los Pokémon. */
@Serializable
data object PokemonList

/** Detalle de un Pokémon; [pokemon] es su número de la Pokédex. */
@Serializable
data class PokemonDetail(val pokemon: Int)
