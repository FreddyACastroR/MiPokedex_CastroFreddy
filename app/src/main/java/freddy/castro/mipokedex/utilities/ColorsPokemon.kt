package freddy.castro.mipokedex.utilities

import androidx.compose.ui.graphics.Color
import freddy.castro.mipokedex.ui.theme.Bug
import freddy.castro.mipokedex.ui.theme.Dark
import freddy.castro.mipokedex.ui.theme.DarkGray
import freddy.castro.mipokedex.ui.theme.Dragon
import freddy.castro.mipokedex.ui.theme.Electric
import freddy.castro.mipokedex.ui.theme.Fairy
import freddy.castro.mipokedex.ui.theme.Fight
import freddy.castro.mipokedex.ui.theme.Fire
import freddy.castro.mipokedex.ui.theme.Flying
import freddy.castro.mipokedex.ui.theme.Ghost
import freddy.castro.mipokedex.ui.theme.Grass
import freddy.castro.mipokedex.ui.theme.Ground
import freddy.castro.mipokedex.ui.theme.Ice
import freddy.castro.mipokedex.ui.theme.Normal
import freddy.castro.mipokedex.ui.theme.OffWhite
import freddy.castro.mipokedex.ui.theme.Poison
import freddy.castro.mipokedex.ui.theme.Psych
import freddy.castro.mipokedex.ui.theme.Rock
import freddy.castro.mipokedex.ui.theme.Water

/**
 * Color del tipo de Pokémon y el color del texto que va encima:
 * OffWhite si el color del tipo es oscuro, DarkGray si es claro.
 */
fun getColorByType(type: String): Pair<Color, Color> =
    // Con dos tipos ("Grass/Poison") manda el primero
    when (type.substringBefore('/')) {
        "Normal" -> Normal to OffWhite
        "Water" -> Water to OffWhite
        "Fire" -> Fire to OffWhite
        "Psych", "Psychic" -> Psych to OffWhite
        "Ghost" -> Ghost to OffWhite
        "Bug" -> Bug to OffWhite
        "Poison" -> Poison to OffWhite
        "Grass" -> Grass to OffWhite
        "Ground" -> Ground to OffWhite
        "Rock" -> Rock to OffWhite
        "Dragon" -> Dragon to OffWhite
        "Dark" -> Dark to OffWhite
        "Electric" -> Electric to DarkGray
        "Fairy" -> Fairy to DarkGray
        "Fight", "Fighting" -> Fight to DarkGray
        "Flying" -> Flying to DarkGray
        "Ice" -> Ice to DarkGray
        else -> Normal to OffWhite
    }
