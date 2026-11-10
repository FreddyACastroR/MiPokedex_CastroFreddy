package freddy.castro.mipokedex.ui.viewmodel

import freddy.castro.mipokedex.data.FakeDataStore
import freddy.castro.mipokedex.data.PartnerRepository
import freddy.castro.mipokedex.data.PokemonRepository
import freddy.castro.mipokedex.data.pokemonList
import freddy.castro.mipokedex.domain.Partner
import java.time.LocalDate
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

private const val ASH = "ash@pokedex.com"
private const val MISTY = "misty@pokedex.com"

class PokemonDetailViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val today = LocalDate.of(2026, 11, 10)
    private val pokemonRepository = PokemonRepository(pokemonList)
    private val partnerRepository = PartnerRepository(FakeDataStore())

    private val bulbasaur = pokemonList.first()
    private val pikachu = pokemonList.first { it.name == "Pikachu" }

    private fun viewModel(number: Int, email: String? = ASH) =
        PokemonDetailViewModel(number, pokemonRepository, partnerRepository, email) { today }

    @Test
    fun detailState_isThePokemonWithThatNumberAndItsNeighbors() {
        val detail = viewModel(pikachu.number).detailState!!
        val index = pokemonList.indexOf(pikachu)

        assertEquals(pikachu, detail.pokemon)
        assertEquals(pokemonList[index - 1], detail.previous)
        assertEquals(pokemonList[index + 1], detail.next)
    }

    @Test
    fun detailState_wrapsAroundAtTheEnds() {
        val first = viewModel(bulbasaur.number).detailState!!
        assertEquals(pokemonList.last(), first.previous)

        val last = viewModel(pokemonList.last().number).detailState!!
        assertEquals(bulbasaur, last.next)
    }

    @Test
    fun detailState_unknownNumber_isNull() {
        assertNull(viewModel(-1).detailState)
    }

    @Test
    fun toggleFavorite_isSeenInTheDetailAndInTheMenu() {
        val viewModel = viewModel(bulbasaur.number)
        val menu = PokemonViewModel(pokemonRepository)

        viewModel.toggleFavorite()
        assertTrue(viewModel.detailState!!.pokemon.favorite)
        assertTrue(menu.favorites.any { it.number == bulbasaur.number })

        viewModel.toggleFavorite()
        assertFalse(viewModel.detailState!!.pokemon.favorite)
        assertFalse(menu.favorites.any { it.number == bulbasaur.number })
    }

    @Test
    fun withoutPartner_offersToSelectIt() {
        val viewModel = viewModel(pikachu.number)

        assertEquals(PartnerStatus.NOT_PARTNER, viewModel.detailState!!.partnerStatus)
        assertFalse(viewModel.partnerSelected)
    }

    @Test
    fun selectPartner_savesItWithTodaysDateAndShowsTheMessage() = runTest {
        val viewModel = viewModel(pikachu.number)

        viewModel.selectPartner()

        assertEquals(PartnerStatus.PARTNER, viewModel.detailState!!.partnerStatus)
        assertTrue(viewModel.partnerSelected)
        assertEquals(Partner(pikachu.number, today), partnerRepository.getPartner(ASH).first())
    }

    @Test
    fun onPartnerMessageShown_hidesTheMessageAndKeepsThePartner() {
        val viewModel = viewModel(pikachu.number)
        viewModel.selectPartner()

        viewModel.onPartnerMessageShown()

        assertFalse(viewModel.partnerSelected)
        assertEquals(PartnerStatus.PARTNER, viewModel.detailState!!.partnerStatus)
    }

    @Test
    fun partner_isRememberedWhenOpeningTheDetailAgain() {
        viewModel(pikachu.number).selectPartner()

        val reopened = viewModel(pikachu.number)

        assertEquals(PartnerStatus.PARTNER, reopened.detailState!!.partnerStatus)
        assertFalse(reopened.partnerSelected)
    }

    @Test
    fun anotherPokemon_isNotThePartner() {
        viewModel(pikachu.number).selectPartner()

        assertEquals(
            PartnerStatus.NOT_PARTNER,
            viewModel(bulbasaur.number).detailState!!.partnerStatus,
        )
    }

    @Test
    fun selectingAnotherPartner_replacesThePreviousOne() {
        val pikachuDetail = viewModel(pikachu.number)
        pikachuDetail.selectPartner()

        viewModel(bulbasaur.number).selectPartner()

        assertEquals(PartnerStatus.NOT_PARTNER, pikachuDetail.detailState!!.partnerStatus)
    }

    @Test
    fun selectPartner_whenItIsAlreadyThePartner_doesNotShowTheMessageAgain() {
        viewModel(pikachu.number).selectPartner()
        val viewModel = viewModel(pikachu.number)

        viewModel.selectPartner()

        assertFalse(viewModel.partnerSelected)
    }

    @Test
    fun eachTrainerHasTheirOwnPartner() {
        viewModel(pikachu.number, email = ASH).selectPartner()

        assertEquals(
            PartnerStatus.NOT_PARTNER,
            viewModel(pikachu.number, email = MISTY).detailState!!.partnerStatus,
        )
    }

    @Test
    fun withoutSession_cannotSelectAPartner() {
        val viewModel = viewModel(pikachu.number, email = null)

        viewModel.selectPartner()

        assertEquals(PartnerStatus.LOADING, viewModel.detailState!!.partnerStatus)
        assertFalse(viewModel.partnerSelected)
    }
}
