package freddy.castro.mipokedex.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import freddy.castro.mipokedex.R
import freddy.castro.mipokedex.data.bulbasaur
import freddy.castro.mipokedex.domain.Pokemon
import freddy.castro.mipokedex.ui.theme.Dimens
import freddy.castro.mipokedex.ui.theme.Green
import freddy.castro.mipokedex.ui.theme.MiPokedexTheme

// Reparto del ancho: los textos dentro de la fila, y altura/peso dentro de los textos
private const val INFO_WIDTH = 0.7f
private const val STATS_WIDTH = 0.85f

private val DescriptionFontSize = 10.sp
private val DescriptionLineHeight = 13.sp

/** Fila de la lista: imagen a la izquierda, datos al centro y número a la derecha. */
@Composable
fun PokemonRow(pokemon: Pokemon) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Dimens.RowPadding),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Image(
            painter = painterResource(pokemon.image),
            contentDescription = stringResource(R.string.cd_row_image, pokemon.name),
            modifier = Modifier
                .width(Dimens.RowImageWidth)
                .padding(Dimens.RowPadding),
        )
        Column(
            modifier = Modifier.fillMaxWidth(INFO_WIDTH),
            verticalArrangement = Arrangement.spacedBy(Dimens.RowTextSpacing),
        ) {
            Text(text = pokemon.name, style = MaterialTheme.typography.labelLarge)
            Text(
                text = pokemon.description,
                fontSize = DescriptionFontSize,
                lineHeight = DescriptionLineHeight,
            )
            Row(
                modifier = Modifier.fillMaxWidth(STATS_WIDTH),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = stringResource(R.string.row_height, pokemon.height),
                    style = MaterialTheme.typography.labelMedium,
                )
                Text(
                    text = stringResource(R.string.row_weight, pokemon.weight),
                    style = MaterialTheme.typography.labelMedium,
                )
            }
        }
        Text(
            text = pokemon.number.toString(),
            modifier = Modifier
                .align(Alignment.Top)
                .background(Green, CircleShape)
                .padding(
                    horizontal = Dimens.NumberPaddingHorizontal,
                    vertical = Dimens.NumberPaddingVertical,
                ),
            style = MaterialTheme.typography.labelMedium,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PokemonRowPreview() {
    MiPokedexTheme {
        PokemonRow(bulbasaur)
    }
}
