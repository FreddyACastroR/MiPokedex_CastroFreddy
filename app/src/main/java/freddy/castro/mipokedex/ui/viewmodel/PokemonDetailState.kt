package freddy.castro.mipokedex.ui.viewmodel

import freddy.castro.mipokedex.domain.Pokemon

/** Lo que muestra el detalle: el Pokémon y sus vecinos en la lista. */
data class PokemonDetailState(
    val pokemon: Pokemon,
    val previous: Pokemon,
    val next: Pokemon,
)
