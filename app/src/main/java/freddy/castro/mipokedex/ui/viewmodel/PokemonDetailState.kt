package freddy.castro.mipokedex.ui.viewmodel

import freddy.castro.mipokedex.domain.Pokemon

enum class PartnerStatus { LOADING, NOT_PARTNER, PARTNER }

data class PokemonDetailState(
    val pokemon: Pokemon,
    val previous: Pokemon,
    val next: Pokemon,
    val partnerStatus: PartnerStatus = PartnerStatus.LOADING,
)
