package freddy.castro.mipokedex

import android.app.Application
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import freddy.castro.mipokedex.data.PartnerRepository
import freddy.castro.mipokedex.data.PokemonRepository
import freddy.castro.mipokedex.data.Users

class PokedexApplication : Application() {

    val users by lazy { Users(this) }

    val pokemonRepository by lazy { PokemonRepository() }

    val partnerRepository by lazy { PartnerRepository(this) }
}

val CreationExtras.pokedexApplication: PokedexApplication
    get() = checkNotNull(this[APPLICATION_KEY]) as PokedexApplication
