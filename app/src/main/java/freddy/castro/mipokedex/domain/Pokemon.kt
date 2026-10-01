package freddy.castro.mipokedex.domain

import androidx.annotation.DrawableRes

/** Pokémon con todos los datos que muestra la lista de la Pokédex. */
data class Pokemon(
    val name: String,
    val number: Int,
    val type: String,
    val description: String,
    val height: Float,
    val weight: Float,
    val favorite: Boolean,
    val ability: String,
    @DrawableRes val image: Int,
)
