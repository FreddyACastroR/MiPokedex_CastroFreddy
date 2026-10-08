package freddy.castro.mipokedex.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import freddy.castro.mipokedex.ui.theme.Dimens
import freddy.castro.mipokedex.ui.theme.MiPokedexTheme
import freddy.castro.mipokedex.utilities.getColorByType

/** Una etiqueta por cada tipo del Pokémon: "Grass/Poison" son dos. */
@Composable
fun TypeChips(types: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(Dimens.MarginSmall),
    ) {
        types.split('/').forEach { type ->
            TypeChip(type)
        }
    }
}

/** Etiqueta redondeada con el nombre del tipo, pintada del color de ese tipo. */
@Composable
fun TypeChip(type: String, modifier: Modifier = Modifier) {
    val colors = getColorByType(type)
    Text(
        text = type,
        modifier = modifier
            .background(colors.first, CircleShape)
            .padding(
                horizontal = Dimens.ChipPaddingHorizontal,
                vertical = Dimens.ChipPaddingVertical,
            ),
        color = colors.second,
        style = MaterialTheme.typography.labelSmall,
    )
}

@Preview
@Composable
private fun TypeChipsPreview() {
    MiPokedexTheme {
        TypeChips("Grass/Poison")
    }
}
