package freddy.castro.mipokedex.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import freddy.castro.mipokedex.R
import freddy.castro.mipokedex.data.bulbasaur
import freddy.castro.mipokedex.domain.Pokemon
import freddy.castro.mipokedex.ui.theme.Dimens
import freddy.castro.mipokedex.ui.theme.MiPokedexTheme

/**
 * Nombre y número del Pokémon a la izquierda y la estrella de favorito a la derecha;
 * al tocar la estrella se avisa con [onFavoriteClick].
 */
@Composable
fun PokedexHeader(pokemon: Pokemon, onFavoriteClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(modifier.fillMaxWidth()) {
        // El número queda alineado al final del nombre
        Column(
            modifier = Modifier.padding(start = Dimens.MarginLarge, top = Dimens.MarginLarge),
            horizontalAlignment = Alignment.End,
        ) {
            Text(
                text = pokemon.name,
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.headlineLarge,
            )
            Text(
                text = stringResource(R.string.pokemon_number, pokemon.number),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.headlineSmall,
            )
        }
        // Estrella rellena si es favorito y solo el contorno si no lo es. Funciona como
        // casilla: el lector de pantalla dice "Favorito" y si está marcada o no
        Image(
            painter = painterResource(
                if (pokemon.favorite) R.drawable.ic_star else R.drawable.ic_star_border,
            ),
            contentDescription = stringResource(R.string.cd_favorite),
            // El área táctil incluye el padding (48dp) y el efecto al tocar es redondo
            modifier = Modifier
                .align(Alignment.TopEnd)
                .clip(CircleShape)
                .toggleable(
                    value = pokemon.favorite,
                    role = Role.Checkbox,
                    onValueChange = { onFavoriteClick() },
                )
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
            pokemon = bulbasaur,
            onFavoriteClick = {},
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
        )
    }
}
