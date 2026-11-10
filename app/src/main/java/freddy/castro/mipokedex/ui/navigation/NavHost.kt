package freddy.castro.mipokedex.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import freddy.castro.mipokedex.ui.screens.LoginScreen
import freddy.castro.mipokedex.ui.screens.MenuPokedexScreen
import freddy.castro.mipokedex.ui.screens.PokemonDetailScreen
import freddy.castro.mipokedex.ui.screens.PokemonHuntScreen
import freddy.castro.mipokedex.ui.screens.ProfileScreen
import freddy.castro.mipokedex.ui.screens.WhoIsThatPokemonScreen
import freddy.castro.mipokedex.ui.viewmodel.PokemonViewModel
import freddy.castro.mipokedex.ui.viewmodel.UserViewModel

@Composable
fun PokedexNavHost(
    innerPadding: PaddingValues,
    viewModel: PokemonViewModel = viewModel(factory = PokemonViewModel.Factory),
    userViewModel: UserViewModel = viewModel(factory = UserViewModel.Factory),
) {
    val navController = rememberNavController()
    val openDetail = { pokemonNumber: Int ->
        navController.navigate(PokemonDetail(pokemonNumber)) { launchSingleTop = true }
    }
    val openMenu = {
        navController.navigate(PokemonList) {
            popUpTo<Login> { inclusive = true }
            launchSingleTop = true
        }
    }
    NavHost(navController = navController, startDestination = Login) {
        composable<Login> {
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
                onGuessClick = {
                    navController.navigate(WhoIsThatPokemon) { launchSingleTop = true }
                },
                onHuntClick = { navController.navigate(PokemonHunt) { launchSingleTop = true } },
                onProfileClick = { navController.navigate(Profile) { launchSingleTop = true } },
                onLogoutClick = {
                    userViewModel.logout()
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
        composable<WhoIsThatPokemon> {
            WhoIsThatPokemonScreen(
                innerPadding = innerPadding,
                state = viewModel.whoIsThatState,
                onGuessClick = viewModel::guessPokemon,
                onNextClick = viewModel::nextMysteryPokemon,
            )
        }
        composable<Profile> {
            ProfileScreen()
        }
        composable<PokemonDetail> { backStackEntry ->
            val detail: PokemonDetail = backStackEntry.toRoute()
            PokemonDetailScreen(
                pokemonNumber = detail.pokemon,
                onNeighborClick = { neighborNumber ->
                    navController.navigate(PokemonDetail(neighborNumber)) {
                        popUpTo<PokemonDetail> { inclusive = true }
                    }
                },
            )
        }
    }
}
