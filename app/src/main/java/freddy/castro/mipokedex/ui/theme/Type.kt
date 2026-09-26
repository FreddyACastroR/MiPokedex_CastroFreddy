package freddy.castro.mipokedex.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Un estilo por cada tipo de texto de la Pokédex; los componentes solo eligen cuál usar
val PokedexTypography = Typography(
    // Nombre del Pokémon
    headlineLarge = TextStyle(fontSize = 34.sp, fontWeight = FontWeight.Bold),
    // Número y estadísticas
    headlineSmall = TextStyle(fontSize = 24.sp),
    // Descripción
    titleLarge = TextStyle(fontSize = 20.sp),
    // Navegación entre Pokémon
    bodyMedium = TextStyle(fontSize = 14.sp),
    // Etiqueta del tipo
    labelLarge = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Bold),
)
