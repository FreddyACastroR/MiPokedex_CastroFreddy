package freddy.castro.mipokedex.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// La Pokédex se ve igual en modo claro y oscuro, por eso hay un solo esquema de colores
private val PokedexColorScheme = lightColorScheme(
    primary = PokedexYellow,
    onPrimary = Color.White,          // nombre del Pokémon
    secondary = PokedexRed,           // etiquetas de las estadísticas
    onSecondary = Color.White,
    background = PokedexYellow,
    onBackground = TextNumber,        // número del Pokémon
    surface = Color.White,            // tarjeta
    onSurface = TextPrimary,          // valores y navegación
    onSurfaceVariant = TextSecondary, // descripción
)

@Composable
fun MiPokedexTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = PokedexColorScheme,
        typography = PokedexTypography,
        content = content,
    )
}
