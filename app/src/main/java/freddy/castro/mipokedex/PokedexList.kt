package freddy.castro.mipokedex

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import freddy.castro.mipokedex.data.pokemonList
import freddy.castro.mipokedex.ui.components.MenuPokedex
import freddy.castro.mipokedex.ui.theme.MiPokedexTheme

class PokedexList : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // La pantalla siempre es clara, así que los íconos de las barras del sistema van oscuros
        val lightBars = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
        enableEdgeToEdge(statusBarStyle = lightBars, navigationBarStyle = lightBars)
        setContent {
            MiPokedexTheme {
                // El fondo del tema es amarillo; la lista va sobre blanco
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = MaterialTheme.colorScheme.surface,
                ) { innerPadding ->
                    MenuPokedex(pokemonList, innerPadding)
                }
            }
        }
    }
}
