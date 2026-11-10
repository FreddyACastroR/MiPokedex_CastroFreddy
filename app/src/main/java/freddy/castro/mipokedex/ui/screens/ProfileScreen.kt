package freddy.castro.mipokedex.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import freddy.castro.mipokedex.R
import freddy.castro.mipokedex.data.pokemonList
import freddy.castro.mipokedex.domain.Pokemon
import freddy.castro.mipokedex.ui.components.PokedexCard
import freddy.castro.mipokedex.ui.components.TypeChips
import freddy.castro.mipokedex.ui.theme.Dimens
import freddy.castro.mipokedex.ui.theme.MiPokedexTheme
import freddy.castro.mipokedex.ui.viewmodel.ProfileState
import freddy.castro.mipokedex.ui.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = viewModel(factory = ProfileViewModel.Factory),
) {
    ProfileContent(state = viewModel.profileState, modifier = modifier)
}

@Composable
private fun ProfileContent(state: ProfileState, modifier: Modifier = Modifier) {
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
        Column(modifier = Modifier.fillMaxSize()) {
            TrainerHeader(name = state.name, email = state.email)
            PokedexCard(
                modifier = Modifier
                    .padding(top = Dimens.MarginLarge)
                    .weight(1f),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(Dimens.MarginLarge),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(Dimens.MarginMedium),
                ) {
                    Text(
                        text = stringResource(R.string.label_partner),
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.headlineSmall,
                    )
                    val partner = state.partner
                    when {
                        state.isLoading -> Unit
                        partner == null -> NoPartner()
                        else -> PartnerInfo(partner, state.partnerDays)
                    }
                }
            }
        }
    }
}

@Composable
private fun TrainerHeader(name: String, email: String) {
    Column(
        modifier = Modifier.padding(
            start = Dimens.MarginLarge,
            top = Dimens.MarginLarge,
            end = Dimens.MarginLarge,
        ),
    ) {
        Text(
            text = stringResource(R.string.label_trainer),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleMedium,
        )
        Text(
            text = name,
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.headlineLarge,
        )
        Text(
            text = email,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleMedium,
        )
    }
}

@Composable
private fun PartnerInfo(partner: Pokemon, days: Int) {
    Image(
        painter = painterResource(partner.image),
        contentDescription = stringResource(R.string.cd_pokemon_image, partner.name),
        modifier = Modifier.size(Dimens.ProfilePartnerImageSize),
    )
    Text(
        text = partner.name,
        color = MaterialTheme.colorScheme.onSurface,
        style = MaterialTheme.typography.headlineSmall,
    )
    Text(
        text = stringResource(R.string.pokemon_number, partner.number),
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        style = MaterialTheme.typography.titleMedium,
    )
    TypeChips(partner.type)
    PartnerDaysSign(name = partner.name, days = days)
}

@Composable
private fun PartnerDaysSign(name: String, days: Int) {
    val text = if (days == 0) {
        stringResource(R.string.partner_since_today, name)
    } else {
        pluralStringResource(R.plurals.partner_days, days, name, days)
    }
    Text(
        text = text,
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primary, CircleShape)
            .padding(horizontal = Dimens.MarginMedium, vertical = Dimens.MarginSmall),
        color = MaterialTheme.colorScheme.onSurface,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.titleMedium,
    )
}

@Composable
private fun NoPartner() {
    Image(
        painter = painterResource(R.drawable.ic_pokeball),
        contentDescription = null,
        modifier = Modifier.size(Dimens.ProfileEmptyImageSize),
    )
    Text(
        text = stringResource(R.string.partner_empty),
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.bodyLarge,
    )
}

@Preview(showSystemUi = true)
@Composable
private fun ProfileScreenPreview() {
    MiPokedexTheme {
        ProfileContent(
            state = ProfileState(
                name = "Ash",
                email = "ash@pokedex.com",
                partner = pokemonList[3],
                partnerDays = 3,
                isLoading = false,
            ),
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun ProfileScreenEmptyPreview() {
    MiPokedexTheme {
        ProfileContent(
            state = ProfileState(name = "Misty", email = "misty@pokedex.com", isLoading = false),
        )
    }
}
