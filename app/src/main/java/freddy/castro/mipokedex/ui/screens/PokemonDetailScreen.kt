package freddy.castro.mipokedex.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import freddy.castro.mipokedex.R
import freddy.castro.mipokedex.data.bulbasaur
import freddy.castro.mipokedex.data.pokemonList
import freddy.castro.mipokedex.domain.Pokemon
import freddy.castro.mipokedex.ui.components.PokedexCard
import freddy.castro.mipokedex.ui.components.PokedexHeader
import freddy.castro.mipokedex.ui.components.PokedexNavigation
import freddy.castro.mipokedex.ui.components.PokemonStats
import freddy.castro.mipokedex.ui.components.TypeChips
import freddy.castro.mipokedex.ui.theme.Dimens
import freddy.castro.mipokedex.ui.theme.MiPokedexTheme
import freddy.castro.mipokedex.ui.viewmodel.PartnerStatus
import freddy.castro.mipokedex.ui.viewmodel.PokemonDetailState
import freddy.castro.mipokedex.ui.viewmodel.PokemonDetailViewModel

private const val CARD_TOP = 0.29f
private const val IMAGE_TOP = 0.10f
private const val IMAGE_BOTTOM = 0.335f

private val DescriptionAlignment = BiasAlignment.Vertical(-0.5f)

@Composable
fun PokemonDetailScreen(
    pokemonNumber: Int,
    onNeighborClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PokemonDetailViewModel = viewModel(
        factory = PokemonDetailViewModel.factory(pokemonNumber),
    ),
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val partnerMessage = stringResource(R.string.partner_selected)
    LaunchedEffect(viewModel.partnerSelected) {
        if (viewModel.partnerSelected) {
            snackbarHostState.showSnackbar(partnerMessage)
            viewModel.onPartnerMessageShown()
        }
    }
    PokemonDetailContent(
        detail = viewModel.detailState,
        snackbarHostState = snackbarHostState,
        onFavoriteClick = viewModel::toggleFavorite,
        onSelectPartnerClick = viewModel::selectPartner,
        onNeighborClick = { neighbor -> onNeighborClick(neighbor.number) },
        modifier = modifier,
    )
}

@Composable
private fun PokemonDetailContent(
    detail: PokemonDetailState?,
    snackbarHostState: SnackbarHostState,
    onFavoriteClick: () -> Unit,
    onSelectPartnerClick: () -> Unit,
    onNeighborClick: (Pokemon) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding(),
    ) {
        if (detail == null) {
            Text(
                text = stringResource(R.string.pokemon_not_found),
                modifier = Modifier.align(Alignment.Center),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleLarge,
            )
        } else {
            PokemonDetail(
                detail = detail,
                onFavoriteClick = onFavoriteClick,
                onSelectPartnerClick = onSelectPartnerClick,
                onNeighborClick = onNeighborClick,
            )
        }
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding(),
        )
    }
}

@Composable
private fun BoxScope.PokemonDetail(
    detail: PokemonDetailState,
    onFavoriteClick: () -> Unit,
    onSelectPartnerClick: () -> Unit,
    onNeighborClick: (Pokemon) -> Unit,
) {
    val pokemon = detail.pokemon
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
        TypeChips(pokemon.type, Modifier.padding(top = Dimens.MarginTypeTop))
        PokemonStats(pokemon, Modifier.padding(top = Dimens.MarginMedium))
        PartnerSlot(
            status = detail.partnerStatus,
            onSelectPartnerClick = onSelectPartnerClick,
            modifier = Modifier.padding(top = Dimens.MarginMedium),
        )
        Text(
            text = pokemon.description,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = Dimens.MarginDescription)
                .wrapContentHeight(DescriptionAlignment),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge,
        )
        PokedexNavigation(
            previous = detail.previous,
            next = detail.next,
            onPokemonClick = onNeighborClick,
        )
    }
    PokedexHeader(pokemon, onFavoriteClick)
    PokemonPortrait(pokemon)
}

@Composable
private fun PartnerSlot(
    status: PartnerStatus,
    onSelectPartnerClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.height(Dimens.PartnerSlotHeight),
        contentAlignment = Alignment.Center,
    ) {
        val colors = MaterialTheme.colorScheme
        when (status) {
            PartnerStatus.PARTNER -> Text(
                text = stringResource(R.string.partner_badge),
                modifier = Modifier
                    .border(Dimens.PartnerBorderWidth, colors.primary, CircleShape)
                    .padding(horizontal = Dimens.MarginMedium, vertical = Dimens.MarginSmall),
                color = colors.onSurface,
                style = MaterialTheme.typography.labelLarge,
            )
            PartnerStatus.NOT_PARTNER -> Button(
                onClick = onSelectPartnerClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = colors.primary,
                    contentColor = colors.onSurface,
                ),
            ) {
                Text(stringResource(R.string.select_partner))
            }
            PartnerStatus.LOADING -> Unit
        }
    }
}

@Composable
private fun PokemonPortrait(pokemon: Pokemon, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.weight(IMAGE_TOP))
        Image(
            painter = painterResource(pokemon.image),
            contentDescription = stringResource(R.string.cd_pokemon_image, pokemon.name),
            modifier = Modifier
                .weight(IMAGE_BOTTOM - IMAGE_TOP)
                .aspectRatio(1f),
        )
        Spacer(Modifier.weight(1f - IMAGE_BOTTOM))
    }
}

@Composable
private fun PokemonDetailPreview(partnerStatus: PartnerStatus) {
    MiPokedexTheme {
        PokemonDetailContent(
            detail = PokemonDetailState(
                pokemon = bulbasaur,
                previous = pokemonList.last(),
                next = pokemonList[1],
                partnerStatus = partnerStatus,
            ),
            snackbarHostState = remember { SnackbarHostState() },
            onFavoriteClick = {},
            onSelectPartnerClick = {},
            onNeighborClick = {},
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun PokemonDetailScreenPreview() {
    PokemonDetailPreview(PartnerStatus.NOT_PARTNER)
}

@Preview(showSystemUi = true)
@Composable
private fun PokemonDetailScreenPartnerPreview() {
    PokemonDetailPreview(PartnerStatus.PARTNER)
}
