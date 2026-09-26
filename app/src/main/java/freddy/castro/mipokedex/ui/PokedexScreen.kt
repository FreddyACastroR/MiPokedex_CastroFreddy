package freddy.castro.mipokedex.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import freddy.castro.mipokedex.R
import freddy.castro.mipokedex.data.PokedexData
import freddy.castro.mipokedex.data.PokedexEntry
import freddy.castro.mipokedex.data.Pokemon
import freddy.castro.mipokedex.ui.components.PokedexCard
import freddy.castro.mipokedex.ui.components.PokedexHeader
import freddy.castro.mipokedex.ui.components.PokedexNavigation
import freddy.castro.mipokedex.ui.components.PokemonStats
import freddy.castro.mipokedex.ui.components.TypeChip
import freddy.castro.mipokedex.ui.theme.Dimens
import freddy.castro.mipokedex.ui.theme.MiPokedexTheme

// Fracciones del alto de la pantalla (debajo de la barra de estado)
private const val CARD_TOP = 0.29f
private const val IMAGE_TOP = 0.10f
private const val IMAGE_BOTTOM = 0.335f

// La descripción queda a un cuarto del espacio libre (-1 = arriba, 1 = abajo)
private val DescriptionAlignment = BiasAlignment.Vertical(-0.5f)

/**
 * Pantalla de la Pokédex. Las capas se dibujan en orden: la tarjeta tapa la parte de abajo
 * de la pokébola, y el encabezado y la imagen del Pokémon quedan encima de la tarjeta.
 */
@Composable
fun PokedexScreen(entry: PokedexEntry, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding(),
    ) {
        Image(
            painter = painterResource(R.drawable.ic_pokeball),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = Dimens.PokeballOffsetX, y = Dimens.PokeballOffsetY)
                .size(Dimens.PokeballSize),
        )
        PokedexCard(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxHeight(1f - CARD_TOP),
        ) {
            TypeChip(entry.type, Modifier.padding(top = Dimens.MarginTypeTop))
            PokemonStats(entry, Modifier.padding(top = Dimens.MarginMedium))
            Text(
                text = stringResource(entry.description),
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = Dimens.MarginDescription)
                    .wrapContentHeight(DescriptionAlignment),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
            )
            PokedexNavigation(previous = entry.previous, next = entry.next)
        }
        PokedexHeader(entry.pokemon)
        PokemonPortrait(entry.pokemon)
    }
}

/** Imagen principal: va del 10 % al 33.5 % del alto, encimada sobre el borde de la tarjeta. */
@Composable
private fun PokemonPortrait(pokemon: Pokemon, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.weight(IMAGE_TOP))
        Image(
            painter = painterResource(pokemon.image),
            contentDescription = stringResource(R.string.cd_pokemon_image, stringResource(pokemon.name)),
            modifier = Modifier
                .weight(IMAGE_BOTTOM - IMAGE_TOP)
                .aspectRatio(1f),
        )
        Spacer(Modifier.weight(1f - IMAGE_BOTTOM))
    }
}

@Preview(showSystemUi = true)
@Composable
private fun PokedexScreenPreview() {
    MiPokedexTheme {
        PokedexScreen(PokedexData.pikachuEntry)
    }
}
