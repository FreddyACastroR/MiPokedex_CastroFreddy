package freddy.castro.mipokedex.ui.screens

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import freddy.castro.mipokedex.R
import freddy.castro.mipokedex.data.pokemonList
import freddy.castro.mipokedex.domain.Pokemon
import freddy.castro.mipokedex.ui.components.PokemonRow
import freddy.castro.mipokedex.ui.components.SectionLabel
import freddy.castro.mipokedex.ui.components.TypeChips
import freddy.castro.mipokedex.ui.theme.Dimens
import freddy.castro.mipokedex.ui.theme.Green
import freddy.castro.mipokedex.ui.theme.LightGreen
import freddy.castro.mipokedex.ui.theme.MiPokedexTheme
import freddy.castro.mipokedex.ui.viewmodel.PokedexState

// El Pokémon que escapó se ve deslavado
private const val ESCAPED_ALPHA = 0.35f

// Se crea una sola vez en lugar de en cada recomposición
private val EncounterShape = RoundedCornerShape(Dimens.EncounterCornerRadius)

/**
 * Pantalla de cacería: la hierba con el Pokémon salvaje arriba, los botones para buscar y
 * capturar, y abajo el equipo de Pokémon capturados. Toda la lógica vive en el ViewModel;
 * esta pantalla solo muestra [state] y avisa de los toques.
 */
@Composable
fun PokemonHuntScreen(
    innerPadding: PaddingValues,
    state: PokedexState,
    onSearchClick: () -> Unit,
    onCaptureClick: () -> Unit,
    onPokemonClick: (Pokemon) -> Unit,
) {
    // Todo se desliza junto: con la pantalla acostada la hierba no deja ver el equipo
    LazyColumn(modifier = Modifier.padding(innerPadding)) {
        item {
            SectionLabel(
                text = stringResource(R.string.label_tall_grass),
                modifier = Modifier.padding(top = Dimens.MarginMedium),
            )
        }
        item {
            TallGrass(
                state = state,
                modifier = Modifier
                    .padding(Dimens.MarginMedium)
                    .fillMaxWidth()
                    .height(Dimens.EncounterHeight),
            )
        }
        item {
            HuntButtons(
                canCapture = state.wildPokemon != null,
                onSearchClick = onSearchClick,
                onCaptureClick = onCaptureClick,
            )
        }
        item {
            SectionLabel(
                text = stringResource(R.string.label_team, state.team.size),
                modifier = Modifier.padding(top = Dimens.MarginLarge),
            )
        }
        if (state.team.isEmpty()) {
            item {
                Text(
                    text = stringResource(R.string.team_empty),
                    modifier = Modifier.padding(Dimens.MarginMedium),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        } else {
            // Sin llave: el mismo Pokémon puede estar varias veces en el equipo
            items(state.team) { pokemon ->
                PokemonRow(pokemon, onClick = { onPokemonClick(pokemon) })
            }
        }
    }
}

/** La hierba muestra al Pokémon salvaje, cómo salió el último intento o una indicación. */
@Composable
private fun TallGrass(state: PokedexState, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .background(LightGreen, EncounterShape)
            .border(Dimens.EncounterBorderWidth, Green, EncounterShape)
            .padding(Dimens.MarginMedium),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimens.MarginSmall, Alignment.CenterVertically),
    ) {
        val wild = state.wildPokemon
        val caught = state.lastCaptured
        val escaped = state.escaped
        when {
            wild != null -> {
                EncounterImage(wild.image, wild.name)
                EncounterMessage(stringResource(R.string.hunt_wild, wild.name))
                TypeChips(wild.type)
            }
            caught != null -> {
                EncounterImage(caught.image, caught.name)
                EncounterMessage(stringResource(R.string.hunt_caught, caught.name))
            }
            escaped != null -> {
                EncounterImage(escaped.image, escaped.name, Modifier.alpha(ESCAPED_ALPHA))
                EncounterMessage(stringResource(R.string.hunt_escaped, escaped.name))
            }
            else -> {
                Image(
                    painter = painterResource(R.drawable.ic_pokeball),
                    contentDescription = null,
                    modifier = Modifier.size(Dimens.EncounterIdleImageSize),
                )
                EncounterMessage(stringResource(R.string.hunt_idle))
            }
        }
    }
}

/** Imagen del Pokémon en la hierba: ocupa el espacio que dejan los textos y sigue cuadrada. */
@Composable
private fun ColumnScope.EncounterImage(
    @DrawableRes image: Int,
    name: String,
    modifier: Modifier = Modifier,
) {
    Image(
        painter = painterResource(image),
        contentDescription = stringResource(R.string.cd_pokemon_image, name),
        modifier = modifier
            .weight(1f)
            .aspectRatio(1f),
    )
}

@Composable
private fun EncounterMessage(text: String) {
    Text(
        text = text,
        color = MaterialTheme.colorScheme.onSurface,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.titleMedium,
    )
}

/** Buscar siempre se puede; capturar solo cuando hay un Pokémon en la hierba. */
@Composable
private fun HuntButtons(
    canCapture: Boolean,
    onSearchClick: () -> Unit,
    onCaptureClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.padding(horizontal = Dimens.MarginMedium),
        horizontalArrangement = Arrangement.spacedBy(Dimens.MarginMedium),
    ) {
        OutlinedButton(
            onClick = onSearchClick,
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.onSurface,
            ),
        ) {
            Text(stringResource(R.string.hunt_search))
        }
        // Rojo como la pokébola
        Button(
            onClick = onCaptureClick,
            modifier = Modifier.weight(1f),
            enabled = canCapture,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary,
            ),
        ) {
            Text(stringResource(R.string.hunt_capture))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PokemonHuntScreenPreview() {
    MiPokedexTheme {
        PokemonHuntScreen(
            innerPadding = PaddingValues(),
            state = PokedexState(
                wildPokemon = pokemonList[3],
                team = pokemonList.take(2),
            ),
            onSearchClick = {},
            onCaptureClick = {},
            onPokemonClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PokemonHuntScreenEmptyPreview() {
    MiPokedexTheme {
        PokemonHuntScreen(
            innerPadding = PaddingValues(),
            state = PokedexState(),
            onSearchClick = {},
            onCaptureClick = {},
            onPokemonClick = {},
        )
    }
}
