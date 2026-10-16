package freddy.castro.mipokedex.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import freddy.castro.mipokedex.domain.Pokemon
import freddy.castro.mipokedex.ui.screens.MenuPokedexScreen
import freddy.castro.mipokedex.ui.screens.PokemonDetailScreen
import freddy.castro.mipokedex.ui.screens.PokemonHuntScreen
import freddy.castro.mipokedex.ui.viewmodel.PokemonViewModel

/**
 * Rutas de la app: empieza en el menú, y de ahí se abre la cacería o el detalle de cada
 * Pokémon. El ViewModel se pide aquí, fuera del NavHost, para que sea de la actividad y
 * las tres pantallas compartan el mismo; si cada pantalla pidiera el suyo, el equipo se
 * perdería al salir de la cacería.
 */
@Composable
fun PokedexNavHost(innerPadding: PaddingValues, viewModel: PokemonViewModel = viewModel()) {
    val navController = rememberNavController()
    // Con dos toques seguidos no se abre la misma pantalla dos veces
    val openDetail = { pokemon: Pokemon ->
        navController.navigate(PokemonDetail(pokemon.number)) { launchSingleTop = true }
    }
    NavHost(navController = navController, startDestination = PokemonList) {
        composable<PokemonList> {
            MenuPokedexScreen(
                innerPadding = innerPadding,
                pokemonList = viewModel.pokemons,
                favoriteList = viewModel.favorites,
                onPokemonClick = openDetail,
                onHuntClick = { navController.navigate(PokemonHunt) { launchSingleTop = true } },
            )
        }
        composable<PokemonHunt> {
            PokemonHuntScreen(
                innerPadding = innerPadding,
                state = viewModel.pokedexState,
                onSearchClick = viewModel::searchPokemon,
                onCaptureClick = viewModel::capturePokemon,
                onPokemonClick = openDetail,
            )
        }
        composable<PokemonDetail> { backStackEntry ->
            val number = backStackEntry.toRoute<PokemonDetail>().pokemon
            PokemonDetailScreen(
                detail = viewModel.detailOf(number),
                onFavoriteClick = { viewModel.toggleFavorite(number) },
                onNeighborClick = { neighbor ->
                    // El vecino reemplaza al detalle actual: "atrás" regresa a donde se abrió
                    navController.navigate(PokemonDetail(neighbor.number)) {
                        popUpTo<PokemonDetail> { inclusive = true }
                    }
                },
            )
        }
    }
}
