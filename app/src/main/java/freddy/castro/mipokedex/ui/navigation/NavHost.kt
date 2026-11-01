package freddy.castro.mipokedex.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import freddy.castro.mipokedex.domain.Pokemon
import freddy.castro.mipokedex.ui.screens.LoginScreen
import freddy.castro.mipokedex.ui.screens.MenuPokedexScreen
import freddy.castro.mipokedex.ui.screens.PokemonDetailScreen
import freddy.castro.mipokedex.ui.screens.PokemonHuntScreen
import freddy.castro.mipokedex.ui.viewmodel.PokemonViewModel
import freddy.castro.mipokedex.ui.viewmodel.UserViewModel

/**
 * Rutas de la app: empieza en el login, que manda directo al menú si quedó una sesión
 * guardada, y del menú se abre la cacería o el detalle de cada Pokémon. Los ViewModel se
 * piden aquí, fuera del NavHost, para que sean de la actividad y las pantallas compartan los
 * mismos; si cada pantalla pidiera el suyo, el equipo se perdería al salir de la cacería.
 */
@Composable
fun PokedexNavHost(
    innerPadding: PaddingValues,
    viewModel: PokemonViewModel = viewModel(),
    userViewModel: UserViewModel = viewModel(factory = UserViewModel.Factory),
) {
    val navController = rememberNavController()
    // Con dos toques seguidos no se abre la misma pantalla dos veces
    val openDetail = { pokemon: Pokemon ->
        navController.navigate(PokemonDetail(pokemon.number)) { launchSingleTop = true }
    }
    // Sin el login en la pila, "atrás" en el menú cierra la app
    val openMenu = {
        navController.navigate(PokemonList) {
            popUpTo<Login> { inclusive = true }
            launchSingleTop = true
        }
    }
    NavHost(navController = navController, startDestination = Login) {
        composable<Login> {
            // Se revisa una sola vez, al llegar: si la sesión se inicia aquí mismo, el que
            // lleva al menú es el botón
            val isLoggedIn = remember { userViewModel.isLoggedIn() }
            LoginScreen(
                isLoggedIn = isLoggedIn,
                loginError = userViewModel.loginError,
                errorDescription = userViewModel.errorDescription,
                onLoginClick = { mail, password ->
                    userViewModel.login(mail, password)
                    if (!userViewModel.loginError) openMenu()
                },
                onLoggedIn = openMenu,
            )
        }
        composable<PokemonList> {
            MenuPokedexScreen(
                innerPadding = innerPadding,
                pokemonList = viewModel.pokemons,
                favoriteList = viewModel.favorites,
                grid = userViewModel.gridDisplay,
                onGridChange = userViewModel::changeDisplay,
                onPokemonClick = openDetail,
                onHuntClick = { navController.navigate(PokemonHunt) { launchSingleTop = true } },
                onLogoutClick = {
                    userViewModel.logout()
                    // Se vacía toda la pila: "atrás" en el login cierra la app y no regresa
                    // a la Pokédex
                    navController.navigate(Login) {
                        popUpTo(navController.graph.id) { inclusive = true }
                    }
                },
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
