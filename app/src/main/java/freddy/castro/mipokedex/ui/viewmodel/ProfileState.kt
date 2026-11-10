package freddy.castro.mipokedex.ui.viewmodel

import freddy.castro.mipokedex.domain.Pokemon

data class ProfileState(
    val name: String = "",
    val email: String = "",
    val partner: Pokemon? = null,
    val partnerDays: Int = 0,
    val isLoading: Boolean = true,
)
