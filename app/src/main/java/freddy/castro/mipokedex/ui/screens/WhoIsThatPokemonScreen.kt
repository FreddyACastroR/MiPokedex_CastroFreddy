package freddy.castro.mipokedex.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import freddy.castro.mipokedex.R
import freddy.castro.mipokedex.data.pokemonList
import freddy.castro.mipokedex.domain.Pokemon
import freddy.castro.mipokedex.ui.theme.Dimens
import freddy.castro.mipokedex.ui.theme.Green
import freddy.castro.mipokedex.ui.theme.MiPokedexTheme
import freddy.castro.mipokedex.ui.theme.Silhouette
import freddy.castro.mipokedex.ui.viewmodel.WhoIsThatPokemonState

// El fondo es 4:3; el PNG es cuadrado y las franjas transparentes de arriba y abajo se recortan
private const val BACKGROUND_RATIO = 4f / 3f

// Centro del destello claro del fondo, como fracción del ancho y del alto de la imagen
private const val BURST_CENTER_X = 0.31f
private const val BURST_CENTER_Y = 0.36f

// Alto del Pokémon como fracción del alto de la imagen: cabe en el destello
private const val MYSTERY_SIZE = 0.6f

// Lo que tarda en desvanecerse la sombra al revelar al Pokémon
private const val REVEAL_MILLIS = 600

// Los nombres se acomodan de dos en dos
private const val OPTIONS_PER_ROW = 2

// Se crea una sola vez en lugar de en cada recomposición
private val GuessShape = RoundedCornerShape(Dimens.GuessCornerRadius)

/**
 * "¿Quién es ese Pokémon?": el marcador, la silueta sobre el fondo del programa de televisión,
 * los nombres para adivinar y el botón para pasar al siguiente. Al contestar se revela el
 * Pokémon y se pinta de verde el nombre correcto y de rojo el que se eligió mal. Toda la
 * lógica vive en el ViewModel; esta pantalla solo muestra [state] y avisa de los toques.
 */
@Composable
fun WhoIsThatPokemonScreen(
    innerPadding: PaddingValues,
    state: WhoIsThatPokemonState,
    onGuessClick: (Pokemon) -> Unit,
    onNextClick: () -> Unit,
) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
    ) {
        if (maxWidth > maxHeight) {
            // Acostada, la imagen ocuparía toda la altura y los nombres quedarían abajo, fuera
            // de la vista: mejor la imagen a la izquierda y lo demás a la derecha
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(Dimens.MarginMedium),
                horizontalArrangement = Arrangement.spacedBy(Dimens.MarginLarge),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                state.mystery?.let { mystery ->
                    MysteryPokemon(mystery, state.revealed, Modifier.weight(1f))
                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(Dimens.MarginMedium),
                ) {
                    Score(state)
                    Answers(state, onGuessClick, onNextClick)
                }
            }
        } else {
            // Parada todo va en una columna; si no cabe, se desliza
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Column(
                    modifier = Modifier
                        .widthIn(max = Dimens.GuessMaxWidth)
                        .padding(Dimens.MarginMedium),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(Dimens.MarginMedium),
                ) {
                    Score(state)
                    state.mystery?.let { mystery -> MysteryPokemon(mystery, state.revealed) }
                    Answers(state, onGuessClick, onNextClick)
                }
            }
        }
    }
}

@Composable
private fun Score(state: WhoIsThatPokemonState) {
    Text(
        text = stringResource(R.string.guess_score, state.hits, state.rounds),
        modifier = Modifier.fillMaxWidth(),
        style = MaterialTheme.typography.titleMedium,
    )
}

/**
 * El mensaje, los nombres y el botón para el siguiente. Se acomodan en la columna de quien lo
 * llama: debajo de la imagen si la pantalla está parada o junto a ella si está acostada.
 */
@Composable
private fun ColumnScope.Answers(
    state: WhoIsThatPokemonState,
    onGuessClick: (Pokemon) -> Unit,
    onNextClick: () -> Unit,
) {
    GuessMessage(state)
    GuessOptions(state = state, onGuessClick = onGuessClick)
    // Amarillo como la Pokédex; onPrimary es blanco y no se lee sobre el amarillo
    Button(
        onClick = onNextClick,
        modifier = Modifier.fillMaxWidth(),
        enabled = state.revealed,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onSurface,
        ),
    ) {
        Text(stringResource(R.string.guess_next))
    }
}

/**
 * El fondo de "¿Quién es ese Pokémon?" con el Pokémon en medio del destello: en sombra mientras
 * no se contesta y con sus colores cuando ya se [revealed].
 */
@Composable
private fun MysteryPokemon(pokemon: Pokemon, revealed: Boolean, modifier: Modifier = Modifier) {
    // Al revelarlo la sombra se desvanece poco a poco; al pasar al siguiente vuelve de golpe
    val fading by animateFloatAsState(
        targetValue = if (revealed) 0f else 1f,
        animationSpec = if (revealed) tween(REVEAL_MILLIS) else snap(),
        label = "silhouette",
    )
    // Sin revelar se usa la sombra completa, no la animada: así el Pokémon nuevo no se asoma
    // en el cuadro en que la animación todavía no regresa
    val shadowAlpha = if (revealed) fading else 1f
    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(BACKGROUND_RATIO)
            .clip(GuessShape),
    ) {
        Image(
            painter = painterResource(R.drawable.wtp),
            contentDescription = null,
            modifier = Modifier.matchParentSize(),
            contentScale = ContentScale.Crop,
        )
        // Esta caja tiene el mismo centro que el destello, y el Pokémon va en medio de ella
        Box(
            modifier = Modifier
                .fillMaxWidth(2 * BURST_CENTER_X)
                .fillMaxHeight(2 * BURST_CENTER_Y),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                painter = painterResource(pokemon.image),
                // El lector de pantalla tampoco dice el nombre antes de tiempo
                contentDescription = if (revealed) {
                    stringResource(R.string.cd_pokemon_image, pokemon.name)
                } else {
                    stringResource(R.string.cd_mystery_pokemon)
                },
                modifier = Modifier
                    .fillMaxHeight(MYSTERY_SIZE / (2 * BURST_CENTER_Y))
                    .aspectRatio(1f),
                // SrcAtop pinta la sombra solo donde está el Pokémon, y al bajarle la opacidad
                // se van viendo sus colores
                colorFilter = ColorFilter.tint(
                    Silhouette.copy(alpha = shadowAlpha),
                    BlendMode.SrcAtop,
                ),
            )
        }
    }
}

/** La pregunta mientras no se contesta; después, si se acertó y quién era. */
@Composable
private fun GuessMessage(state: WhoIsThatPokemonState) {
    val mystery = state.mystery
    val text = when {
        mystery == null || !state.revealed -> stringResource(R.string.guess_question)
        state.isCorrect -> stringResource(R.string.guess_correct, mystery.name)
        else -> stringResource(R.string.guess_wrong, mystery.name)
    }
    Text(
        text = text,
        // El lector de pantalla anuncia el resultado en cuanto cambia
        modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite },
        color = MaterialTheme.colorScheme.onSurface,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.titleMedium,
    )
}

/** Los nombres de dos en dos. Ya contestada la ronda no se pueden tocar. */
@Composable
private fun GuessOptions(
    state: WhoIsThatPokemonState,
    onGuessClick: (Pokemon) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(Dimens.MarginSmall),
    ) {
        state.options.chunked(OPTIONS_PER_ROW).forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(Dimens.MarginSmall)) {
                row.forEach { pokemon ->
                    OutlinedButton(
                        onClick = { onGuessClick(pokemon) },
                        modifier = Modifier.weight(1f),
                        enabled = !state.revealed,
                        colors = optionColors(state, pokemon),
                    ) {
                        Text(
                            text = pokemon.name,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
            }
        }
    }
}

/** Al contestar se pinta de verde el nombre correcto y de rojo el que se eligió mal. */
@Composable
private fun optionColors(state: WhoIsThatPokemonState, pokemon: Pokemon): ButtonColors {
    val colors = MaterialTheme.colorScheme
    return when {
        state.revealed && pokemon.number == state.mystery?.number ->
            ButtonDefaults.outlinedButtonColors(
                disabledContainerColor = Green,
                disabledContentColor = colors.onSurface,
            )
        state.revealed && pokemon.number == state.guess?.number ->
            ButtonDefaults.outlinedButtonColors(
                disabledContainerColor = colors.secondary,
                disabledContentColor = colors.onSecondary,
            )
        else -> ButtonDefaults.outlinedButtonColors(contentColor = colors.onSurface)
    }
}

@Preview(showBackground = true)
@Composable
private fun WhoIsThatPokemonScreenPreview() {
    MiPokedexTheme {
        WhoIsThatPokemonScreen(
            innerPadding = PaddingValues(),
            state = WhoIsThatPokemonState(
                mystery = pokemonList[3],
                options = pokemonList.take(4),
            ),
            onGuessClick = {},
            onNextClick = {},
        )
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, widthDp = 800, heightDp = 380)
@Composable
private fun WhoIsThatPokemonScreenWrongPreview() {
    MiPokedexTheme {
        WhoIsThatPokemonScreen(
            innerPadding = PaddingValues(),
            state = WhoIsThatPokemonState(
                mystery = pokemonList[3],
                options = pokemonList.take(4),
                guess = pokemonList[1],
                rounds = 3,
                hits = 2,
            ),
            onGuessClick = {},
            onNextClick = {},
        )
    }
}
