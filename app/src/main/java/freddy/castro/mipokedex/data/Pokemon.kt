package freddy.castro.mipokedex.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import freddy.castro.mipokedex.R
import freddy.castro.mipokedex.ui.theme.TypeElectric

/** Tipo de Pokémon: su nombre y el color de su etiqueta. */
enum class PokemonType(@StringRes val label: Int, val color: Color) {
    ELECTRIC(R.string.type_electric, TypeElectric),
}

/** Lo mínimo para mostrar un Pokémon: nombre, número e imagen. */
data class Pokemon(
    @StringRes val name: Int,
    val number: Int,
    @DrawableRes val image: Int,
)

/** Ficha completa que muestra la Pokédex, junto con el Pokémon anterior y el siguiente. */
data class PokedexEntry(
    val pokemon: Pokemon,
    val type: PokemonType,
    @StringRes val height: Int,
    @StringRes val weight: Int,
    @StringRes val ability: Int,
    @StringRes val description: Int,
    val previous: Pokemon,
    val next: Pokemon,
)
