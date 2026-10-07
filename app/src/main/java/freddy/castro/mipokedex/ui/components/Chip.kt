package freddy.castro.mipokedex.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import freddy.castro.mipokedex.ui.theme.Dimens
import freddy.castro.mipokedex.ui.theme.MiPokedexTheme
import freddy.castro.mipokedex.utilities.getColorByType

private val NumberFontSize = 12.sp

/** Círculo con el número del Pokémon: el primer color es el fondo y el segundo el del texto. */
@Composable
fun NumberChip(text: String, colors: Pair<Color, Color>, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .size(Dimens.NumberChipSize)
            .background(colors.first, CircleShape)
            .padding(Dimens.NumberChipPadding),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = text,
            // Tres cifras no caben en lo que deja el padding: en vez de partirse en dos
            // renglones, el texto se sale parejo hacia el padding y sigue centrado
            modifier = Modifier.wrapContentSize(unbounded = true),
            color = colors.second,
            fontSize = NumberFontSize,
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Black,
        )
    }
}

@Preview
@Composable
private fun NumberChipPreview() {
    MiPokedexTheme {
        NumberChip("1", getColorByType("Grass"))
    }
}
