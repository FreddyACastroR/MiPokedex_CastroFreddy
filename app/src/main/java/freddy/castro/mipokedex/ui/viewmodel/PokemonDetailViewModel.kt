package freddy.castro.mipokedex.ui.viewmodel

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import freddy.castro.mipokedex.data.PartnerRepository
import freddy.castro.mipokedex.data.PokemonRepository
import freddy.castro.mipokedex.domain.Partner
import freddy.castro.mipokedex.pokedexApplication
import java.time.LocalDate
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class PokemonDetailViewModel(
    private val number: Int,
    private val pokemonRepository: PokemonRepository,
    private val partnerRepository: PartnerRepository,
    private val email: String?,
    private val today: () -> LocalDate = LocalDate::now,
) : ViewModel() {

    private var partnerStatus by mutableStateOf(PartnerStatus.LOADING)

    private var selectPartnerJob: Job? = null

    val detailState: PokemonDetailState? by derivedStateOf { getPokemonDetail() }

    var partnerSelected by mutableStateOf(false)
        private set

    init {
        if (email != null) {
            viewModelScope.launch {
                partnerRepository.getPartner(email).collect { partner ->
                    partnerStatus = if (partner?.number == number) {
                        PartnerStatus.PARTNER
                    } else {
                        PartnerStatus.NOT_PARTNER
                    }
                }
            }
        }
    }

    private fun getPokemonDetail(): PokemonDetailState? {
        val pokemons = pokemonRepository.pokemons
        val index = pokemons.indexOfFirst { it.number == number }
        if (index == -1) return null
        return PokemonDetailState(
            pokemon = pokemons[index],
            previous = pokemons[(index - 1).mod(pokemons.size)],
            next = pokemons[(index + 1).mod(pokemons.size)],
            partnerStatus = partnerStatus,
        )
    }

    fun toggleFavorite() {
        pokemonRepository.toggleFavorite(number)
    }

    fun selectPartner() {
        if (email == null || partnerStatus != PartnerStatus.NOT_PARTNER) return
        if (selectPartnerJob?.isActive == true) return
        selectPartnerJob = viewModelScope.launch {
            partnerRepository.savePartner(email, Partner(number, today()))
            partnerSelected = true
        }
    }

    fun onPartnerMessageShown() {
        partnerSelected = false
    }

    companion object {
        fun factory(number: Int) = viewModelFactory {
            initializer {
                val app = pokedexApplication
                PokemonDetailViewModel(
                    number = number,
                    pokemonRepository = app.pokemonRepository,
                    partnerRepository = app.partnerRepository,
                    email = app.users.getUserEmail(),
                )
            }
        }
    }
}
