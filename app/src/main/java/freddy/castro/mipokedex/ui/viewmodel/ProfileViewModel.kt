package freddy.castro.mipokedex.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import freddy.castro.mipokedex.data.PartnerRepository
import freddy.castro.mipokedex.data.PokemonRepository
import freddy.castro.mipokedex.data.Users
import freddy.castro.mipokedex.pokedexApplication
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import kotlinx.coroutines.launch

class ProfileViewModel(
    users: Users,
    private val pokemonRepository: PokemonRepository,
    partnerRepository: PartnerRepository,
    private val today: () -> LocalDate = LocalDate::now,
) : ViewModel() {

    private val email = users.getUserEmail()

    var profileState by mutableStateOf(
        ProfileState(name = users.getUserName().orEmpty(), email = email.orEmpty()),
    )
        private set

    init {
        if (email == null) {
            profileState = profileState.copy(isLoading = false)
        } else {
            viewModelScope.launch {
                partnerRepository.getPartner(email).collect { partner ->
                    profileState = profileState.copy(
                        partner = partner?.let { pokemonRepository.getPokemon(it.number) },
                        partnerDays = partner?.let { daysSince(it.since) } ?: 0,
                        isLoading = false,
                    )
                }
            }
        }
    }

    private fun daysSince(date: LocalDate): Int =
        ChronoUnit.DAYS.between(date, today()).toInt().coerceAtLeast(0)

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val app = pokedexApplication
                ProfileViewModel(app.users, app.pokemonRepository, app.partnerRepository)
            }
        }
    }
}
