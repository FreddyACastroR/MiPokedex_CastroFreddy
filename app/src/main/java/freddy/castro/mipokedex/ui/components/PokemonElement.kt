package freddy.castro.mipokedex.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import freddy.castro.mipokedex.R
import freddy.castro.mipokedex.data.bulbasaur
import freddy.castro.mipokedex.domain.Pokemon
import freddy.castro.mipokedex.ui.theme.Dimens
import freddy.castro.mipokedex.ui.theme.MiPokedexTheme
import freddy.castro.mipokedex.ui.theme.OffWhite
import freddy.castro.mipokedex.utilities.getColorByType

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
        NumberChip(pokemon.number.toString(), getColorByType(pokemon.type))
    }
}

/** Favorito: imagen con marco del color de su tipo, número en la esquina y nombre abajo. */
@Composable
fun FavoritePokemon(pokemon: Pokemon) {
    val colors = getColorByType(pokemon.type)
    Column(
        modifier = Modifier.padding(vertical = Dimens.FavoritePaddingVertical),
        verticalArrangement = Arrangement.spacedBy(Dimens.FavoriteSpacing),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box {
            Box(
                modifier = Modifier.border(
                    BorderStroke(
                        width = Dimens.FavoriteBorderWidth,
                        brush = Brush.sweepGradient(
                            colors = listOf(
                                colors.first,
                                OffWhite,
                                colors.first,
                                OffWhite,
                                colors.first,
                            ),
                        ),
                    ),
                ),
            ) {
                Image(
                    painter = painterResource(pokemon.image),
                    contentDescription = stringResource(R.string.cd_row_image, pokemon.name),
                    modifier = Modifier
                        .width(Dimens.FavoriteImageWidth)
                        .padding(Dimens.FavoriteImagePadding),
                )
            }
            NumberChip(
                text = pokemon.number.toString(),
                colors = colors,
                // offset lo saca de la esquina del marco sin cambiar el tamaño de la caja
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(Dimens.FavoriteChipOffsetX, Dimens.FavoriteChipOffsetY),
            )
        }
        Text(text = pokemon.name, style = MaterialTheme.typography.labelLarge)
    }
}

/** Celda de la tabla: imagen grande, nombre abajo y número en la esquina de arriba. */
@Composable
fun PokemonCell(pokemon: Pokemon) {
    Box(modifier = Modifier.padding(Dimens.CellPadding)) {
        Column(
            modifier = Modifier.padding(Dimens.CellPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(pokemon.image),
                contentDescription = stringResource(R.string.cd_row_image, pokemon.name),
                // Mide 150dp si cabe; en la tabla la celda es más angosta y la imagen
                // se encoge sin dejar de ser cuadrada
                modifier = Modifier
                    .widthIn(max = Dimens.CellImageSize)
                    .aspectRatio(1f)
                    .padding(Dimens.CellImagePadding),
            )
            Text(text = pokemon.name, style = MaterialTheme.typography.labelLarge)
        }
        NumberChip(
            text = pokemon.number.toString(),
            colors = getColorByType(pokemon.type),
            modifier = Modifier.align(Alignment.TopEnd),
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

@Preview(showBackground = true)
@Composable
private fun FavoritePokemonPreview() {
    MiPokedexTheme {
        FavoritePokemon(bulbasaur)
    }
}

@Preview(showBackground = true)
@Composable
private fun PokemonCellPreview() {
    MiPokedexTheme {
        PokemonCell(bulbasaur)
    }
}
