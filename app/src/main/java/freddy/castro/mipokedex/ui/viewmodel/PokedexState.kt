package freddy.castro.mipokedex.ui.viewmodel

import freddy.castro.mipokedex.domain.Pokemon

data class PokedexState(
    val wildPokemon: Pokemon? = null,
    val lastCaptured: Pokemon? = null,
    val escaped: Pokemon? = null,
    val team: List<Pokemon> = emptyList(),
)
