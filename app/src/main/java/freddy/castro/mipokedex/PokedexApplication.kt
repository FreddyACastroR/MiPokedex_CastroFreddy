package freddy.castro.mipokedex

import android.app.Application
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import freddy.castro.mipokedex.data.PartnerRepository
import freddy.castro.mipokedex.data.PokemonRepository
import freddy.castro.mipokedex.data.Users
import freddy.castro.mipokedex.data.sqlite.DatabaseHelper
import freddy.castro.mipokedex.data.sqlite.PokemonDAO
import freddy.castro.mipokedex.data.sqlite.PokemonSQLRepository

class PokedexApplication : Application() {

    val users by lazy { Users(this) }

    val pokemonRepository by lazy { PokemonRepository() }

    val partnerRepository by lazy { PartnerRepository(this) }

    private val databaseHelper by lazy { DatabaseHelper(this) }

    val pokemonSQLRepository by lazy { PokemonSQLRepository(PokemonDAO(databaseHelper)) }
}

val CreationExtras.pokedexApplication: PokedexApplication
    get() = checkNotNull(this[APPLICATION_KEY]) as PokedexApplication
