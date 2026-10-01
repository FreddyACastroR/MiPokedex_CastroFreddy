package freddy.castro.mipokedex.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import freddy.castro.mipokedex.R
import freddy.castro.mipokedex.data.PokedexData
import freddy.castro.mipokedex.data.PokemonSummary
import freddy.castro.mipokedex.ui.theme.Dimens
import freddy.castro.mipokedex.ui.theme.MiPokedexTheme

/** Nombre y número del Pokémon a la izquierda y la estrella de favorito a la derecha. */
@Composable
fun PokedexHeader(pokemon: PokemonSummary, modifier: Modifier = Modifier) {
    Box(modifier.fillMaxWidth()) {
        // El número queda alineado al final del nombre
        Column(
            modifier = Modifier.padding(start = Dimens.MarginLarge, top = Dimens.MarginLarge),
            horizontalAlignment = Alignment.End,
        ) {
            Text(
                text = stringResource(pokemon.name),
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.headlineLarge,
            )
            Text(
                text = stringResource(R.string.pokemon_number, pokemon.number),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.headlineSmall,
            )
        }
        Image(
            painter = painterResource(R.drawable.ic_star),
            contentDescription = stringResource(R.string.cd_favorite),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(Dimens.MarginSmall)
                .size(Dimens.FavoriteSize),
        )
    }
}

@Preview
@Composable
private fun PokedexHeaderPreview() {
    MiPokedexTheme {
        PokedexHeader(
            pokemon = PokedexData.pikachu,
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
        )
    }
}
