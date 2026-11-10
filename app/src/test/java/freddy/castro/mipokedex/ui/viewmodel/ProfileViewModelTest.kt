package freddy.castro.mipokedex.ui.viewmodel

import freddy.castro.mipokedex.data.FakeContext
import freddy.castro.mipokedex.data.FakeDataStore
import freddy.castro.mipokedex.data.FakeSharedPreferences
import freddy.castro.mipokedex.data.PartnerRepository
import freddy.castro.mipokedex.data.PokemonRepository
import freddy.castro.mipokedex.data.Users
import freddy.castro.mipokedex.data.pokemonList
import freddy.castro.mipokedex.domain.Partner
import java.time.LocalDate
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test

class ProfileViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val today = LocalDate.of(2026, 11, 10)
    private val users = Users(FakeContext(FakeSharedPreferences()))
    private val partnerRepository = PartnerRepository(FakeDataStore())

    private val pikachu = pokemonList.first { it.name == "Pikachu" }

    private fun viewModel() =
        ProfileViewModel(users, PokemonRepository(pokemonList), partnerRepository) { today }

    @Test
    fun profileState_showsTheTrainerWithTheSession() {
        users.loginService("ash@pokedex.com", "pikachu")

        val state = viewModel().profileState

        assertEquals("Ash", state.name)
        assertEquals("ash@pokedex.com", state.email)
    }

    @Test
    fun withoutPartner_hasNoPartnerToShow() {
        users.loginService("ash@pokedex.com", "pikachu")

        val state = viewModel().profileState

        assertFalse(state.isLoading)
        assertNull(state.partner)
    }

    @Test
    fun partnerDays_countsTheDaysSinceItWasSelected() = runTest {
        users.loginService("ash@pokedex.com", "pikachu")
        partnerRepository.savePartner("ash@pokedex.com", Partner(pikachu.number, today.minusDays(3)))

        val state = viewModel().profileState

        assertEquals(pikachu, state.partner)
        assertEquals(3, state.partnerDays)
    }

    @Test
    fun partnerSelectedToday_hasZeroDays() = runTest {
        users.loginService("ash@pokedex.com", "pikachu")
        partnerRepository.savePartner("ash@pokedex.com", Partner(pikachu.number, today))

        assertEquals(0, viewModel().profileState.partnerDays)
    }

    @Test
    fun partnerDays_coversMonthsAndLeapYears() = runTest {
        users.loginService("ash@pokedex.com", "pikachu")
        val since = LocalDate.of(2024, 2, 28)
        partnerRepository.savePartner("ash@pokedex.com", Partner(pikachu.number, since))

        assertEquals(986, viewModel().profileState.partnerDays)
    }

    @Test
    fun partnerOfAnotherTrainer_isNotShown() = runTest {
        partnerRepository.savePartner("ash@pokedex.com", Partner(pikachu.number, today))
        users.loginService("misty@pokedex.com", "starmie")

        assertNull(viewModel().profileState.partner)
    }

    @Test
    fun withoutSession_hasNothingToShow() {
        val state = viewModel().profileState

        assertFalse(state.isLoading)
        assertEquals("", state.name)
        assertNull(state.partner)
    }
}
