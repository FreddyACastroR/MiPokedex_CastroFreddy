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

private const val BACKGROUND_RATIO = 4f / 3f

private const val BURST_CENTER_X = 0.31f
private const val BURST_CENTER_Y = 0.36f

private const val MYSTERY_SIZE = 0.6f

private const val REVEAL_MILLIS = 600

private const val OPTIONS_PER_ROW = 2

private val GuessShape = RoundedCornerShape(Dimens.GuessCornerRadius)

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

@Composable
private fun ColumnScope.Answers(
    state: WhoIsThatPokemonState,
    onGuessClick: (Pokemon) -> Unit,
    onNextClick: () -> Unit,
) {
    GuessMessage(state)
    GuessOptions(state = state, onGuessClick = onGuessClick)
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

@Composable
private fun MysteryPokemon(pokemon: Pokemon, revealed: Boolean, modifier: Modifier = Modifier) {
    val fading by animateFloatAsState(
        targetValue = if (revealed) 0f else 1f,
        animationSpec = if (revealed) tween(REVEAL_MILLIS) else snap(),
        label = "silhouette",
    )
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
        Box(
            modifier = Modifier
                .fillMaxWidth(2 * BURST_CENTER_X)
                .fillMaxHeight(2 * BURST_CENTER_Y),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                painter = painterResource(pokemon.image),
                contentDescription = if (revealed) {
                    stringResource(R.string.cd_pokemon_image, pokemon.name)
                } else {
                    stringResource(R.string.cd_mystery_pokemon)
                },
                modifier = Modifier
                    .fillMaxHeight(MYSTERY_SIZE / (2 * BURST_CENTER_Y))
                    .aspectRatio(1f),
                colorFilter = ColorFilter.tint(
                    Silhouette.copy(alpha = shadowAlpha),
                    BlendMode.SrcAtop,
                ),
            )
        }
    }
}

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
        modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite },
        color = MaterialTheme.colorScheme.onSurface,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.titleMedium,
    )
}

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
