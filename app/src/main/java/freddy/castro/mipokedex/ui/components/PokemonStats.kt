package freddy.castro.mipokedex.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import freddy.castro.mipokedex.R
import freddy.castro.mipokedex.data.bulbasaur
import freddy.castro.mipokedex.domain.Pokemon
import freddy.castro.mipokedex.ui.theme.Dimens
import freddy.castro.mipokedex.ui.theme.MiPokedexTheme

// Reparto del ancho: etiqueta | valor | habilidad
private const val LABEL_WEIGHT = 0.27f
private const val VALUE_WEIGHT = 0.285f
private const val ABILITY_WEIGHT = 0.445f

/** Altura y Peso a la izquierda; Habilidad a la derecha, centrada respecto a ambas. */
@Composable
fun PokemonStats(pokemon: Pokemon, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier.weight(LABEL_WEIGHT + VALUE_WEIGHT),
            verticalArrangement = Arrangement.spacedBy(Dimens.StatsRowSpacing),
        ) {
            StatRow(
                label = R.string.label_height,
                value = stringResource(R.string.pokemon_height, pokemon.height),
            )
            StatRow(
                label = R.string.label_weight,
                value = stringResource(R.string.pokemon_weight, pokemon.weight),
            )
        }
        StatColumn(
            label = R.string.label_ability,
            value = pokemon.ability,
            modifier = Modifier
                .weight(ABILITY_WEIGHT)
                .padding(end = Dimens.MarginMedium),
        )
    }
}

/** Estadística con la etiqueta a la izquierda del valor, alineados por su línea base. */
@Composable
fun StatRow(@StringRes label: Int, value: String, modifier: Modifier = Modifier) {
    Row(modifier) {
        StatLabel(
            text = label,
            modifier = Modifier
                .weight(LABEL_WEIGHT)
                .alignByBaseline()
                .padding(end = Dimens.StatsGap),
            textAlign = TextAlign.End,
        )
        StatValue(
            text = value,
            modifier = Modifier
                .weight(VALUE_WEIGHT)
                .alignByBaseline()
                .padding(start = Dimens.StatsGap),
        )
    }
}

/** Estadística con la etiqueta arriba del valor. */
@Composable
fun StatColumn(@StringRes label: Int, value: String, modifier: Modifier = Modifier) {
    Column(modifier) {
        StatLabel(text = label)
        StatValue(text = value)
    }
}

@Composable
private fun StatLabel(
    @StringRes text: Int,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Start,
) {
    Text(
        text = stringResource(text),
        modifier = modifier,
        color = MaterialTheme.colorScheme.secondary,
        fontWeight = FontWeight.Bold,
        textAlign = textAlign,
        style = MaterialTheme.typography.headlineSmall,
    )
}

@Composable
private fun StatValue(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        modifier = modifier,
        color = MaterialTheme.colorScheme.onSurface,
        style = MaterialTheme.typography.headlineSmall,
    )
}

@Preview(showBackground = true)
@Composable
private fun PokemonStatsPreview() {
    MiPokedexTheme {
        PokemonStats(bulbasaur)
    }
}
