package freddy.castro.mipokedex.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import freddy.castro.mipokedex.data.pokemonList
import freddy.castro.mipokedex.domain.Pokemon
import freddy.castro.mipokedex.ui.theme.Dimens
import freddy.castro.mipokedex.ui.theme.MiPokedexTheme

/** Hacia dónde lleva el vecino; define la flecha y de qué lado del texto va. */
enum class NavDirection(@DrawableRes val arrow: Int) {
    PREVIOUS(R.drawable.ic_arrow_prev),
    NEXT(R.drawable.ic_arrow_next),
}

/** Pokémon anterior en la esquina izquierda y siguiente en la derecha; al tocarlos se abren. */
@Composable
fun PokedexNavigation(
    previous: Pokemon,
    next: Pokemon,
    onPokemonClick: (Pokemon) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(Dimens.MarginSmall),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        NeighborPokemon(previous, NavDirection.PREVIOUS, onClick = { onPokemonClick(previous) })
        NeighborPokemon(next, NavDirection.NEXT, onClick = { onPokemonClick(next) })
    }
}

/** Imagen de un Pokémon vecino con su nombre, número y la flecha de navegación. */
@Composable
fun NeighborPokemon(
    pokemon: Pokemon,
    direction: NavDirection,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimens.MarginSmall),
    ) {
        Image(
            painter = painterResource(pokemon.image),
            contentDescription = stringResource(R.string.cd_pokemon_image, pokemon.name),
            modifier = Modifier.size(Dimens.NeighborImageSize),
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dimens.NavArrowSpacing),
        ) {
            val arrow = painterResource(direction.arrow)
            if (direction == NavDirection.PREVIOUS) Image(arrow, contentDescription = null)
            Text(
                text = stringResource(R.string.neighbor_label, pokemon.name, pokemon.number),
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyMedium,
            )
            if (direction == NavDirection.NEXT) Image(arrow, contentDescription = null)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PokedexNavigationPreview() {
    MiPokedexTheme {
        PokedexNavigation(
            previous = pokemonList.last(),
            next = pokemonList[1],
            onPokemonClick = {},
        )
    }
}
