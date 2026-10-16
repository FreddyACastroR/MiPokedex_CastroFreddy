package freddy.castro.mipokedex.ui.viewmodel

import freddy.castro.mipokedex.domain.Pokemon

/**
 * Estado de la cacería: el Pokémon que está en la hierba, cómo terminó el último intento
 * de captura y el equipo. A lo más uno de [lastCaptured] y [escaped] tiene valor.
 */
data class PokedexState(
    val wildPokemon: Pokemon? = null,
    val lastCaptured: Pokemon? = null,
    val escaped: Pokemon? = null,
    /** Los capturados, el más reciente primero; se puede atrapar el mismo Pokémon varias veces. */
    val team: List<Pokemon> = emptyList(),
)
