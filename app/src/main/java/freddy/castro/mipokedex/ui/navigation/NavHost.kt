package freddy.castro.mipokedex.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import freddy.castro.mipokedex.ui.screens.MenuPokedexScreen
import freddy.castro.mipokedex.ui.screens.PokemonDetailScreen

/** Rutas de la app: empieza en el menú y de ahí se abre el detalle de cada Pokémon. */
@Composable
fun PokedexNavHost(innerPadding: PaddingValues) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = PokemonList) {
        composable<PokemonList> {
            MenuPokedexScreen(
                innerPadding = innerPadding,
                onPokemonClick = { pokemon ->
                    // Con dos toques seguidos no se abre el detalle dos veces
                    navController.navigate(PokemonDetail(pokemon.number)) {
                        launchSingleTop = true
                    }
                },
            )
        }
        composable<PokemonDetail> { backStackEntry ->
            // El argumento se guarda con el nombre del campo de PokemonDetail
            val pokemon = backStackEntry.arguments?.getInt("pokemon") ?: -1
            PokemonDetailScreen(
                pokemonId = pokemon,
                onNeighborClick = { neighbor ->
                    // El vecino reemplaza al detalle actual: "atrás" siempre regresa al menú
                    navController.navigate(PokemonDetail(neighbor.number)) {
                        popUpTo<PokemonDetail> { inclusive = true }
                    }
                },
            )
        }
    }
}
