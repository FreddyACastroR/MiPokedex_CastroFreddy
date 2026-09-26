package freddy.castro.mipokedex

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import freddy.castro.mipokedex.data.PokedexData
import freddy.castro.mipokedex.ui.PokedexScreen
import freddy.castro.mipokedex.ui.theme.MiPokedexTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // La pantalla siempre es clara, así que los íconos de las barras del sistema van oscuros
        val lightBars = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
        enableEdgeToEdge(statusBarStyle = lightBars, navigationBarStyle = lightBars)
        setContent {
            MiPokedexTheme {
                PokedexScreen(PokedexData.pikachuEntry)
            }
        }
    }
}
