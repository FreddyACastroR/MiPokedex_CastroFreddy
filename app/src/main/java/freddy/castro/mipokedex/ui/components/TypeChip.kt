package freddy.castro.mipokedex.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import freddy.castro.mipokedex.data.PokemonType
import freddy.castro.mipokedex.ui.theme.Dimens
import freddy.castro.mipokedex.ui.theme.MiPokedexTheme

/** Etiqueta redondeada con el nombre del tipo, pintada del color de ese tipo. */
@Composable
fun TypeChip(type: PokemonType, modifier: Modifier = Modifier) {
    Text(
        text = stringResource(type.label),
        modifier = modifier
            .background(type.color, CircleShape)
            .padding(
                horizontal = Dimens.ChipPaddingHorizontal,
                vertical = Dimens.ChipPaddingVertical,
            ),
        color = Color.Black,
        style = MaterialTheme.typography.labelLarge,
    )
}

@Preview
@Composable
private fun TypeChipPreview() {
    MiPokedexTheme {
        TypeChip(PokemonType.ELECTRIC)
    }
}
