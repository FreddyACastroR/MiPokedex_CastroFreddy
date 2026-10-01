package freddy.castro.mipokedex.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
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
    // Nombre del Pokémon en la lista
    labelLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        lineHeight = 27.sp,
        letterSpacing = 0.5.sp,
    ),
    // Altura, peso y número en la lista
    labelMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.5.sp,
    ),
    // Etiqueta del tipo
    labelSmall = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Bold),
)
