package freddy.castro.mipokedex.ui.navigation

import kotlinx.serialization.Serializable

/** Menú con todos los Pokémon. */
@Serializable
data object PokemonList

/** Cacería: se buscan Pokémon salvajes en la hierba y se intenta atraparlos. */
@Serializable
data object PokemonHunt

/** Detalle de un Pokémon; [pokemon] es su número de la Pokédex. */
@Serializable
data class PokemonDetail(val pokemon: Int)
