package freddy.castro.mipokedex.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import freddy.castro.mipokedex.data.Users

/** Lo que se muestra debajo del botón cuando el correo o la contraseña no coinciden. */
const val LOGIN_ERROR = "Usuario o contraseña incorrectos"

/**
 * Sesión del usuario y sus preferencias. Es de la actividad, igual que [PokemonViewModel]: el
 * login la inicia y el menú la cierra y cambia la vista de los Pokémon.
 */
class UserViewModel(private val users: Users) : ViewModel() {

    /** Si el último intento de iniciar sesión falló. */
    var loginError by mutableStateOf(false)
        private set

    var errorDescription by mutableStateOf("")
        private set

    /** Si el menú muestra los Pokémon en tabla; se lee de lo que eligió la última vez. */
    var gridDisplay by mutableStateOf(users.isGridDisplay())
        private set

    /** Si los datos coinciden la sesión queda guardada; si no, se avisa con [loginError]. */
    fun login(mail: String, password: String) {
        val user = users.loginService(mail, password)
        if (user == null) {
            loginError = true
            errorDescription = LOGIN_ERROR
        } else {
            loginError = false
            errorDescription = ""
        }
    }

    fun isLoggedIn(): Boolean = users.isLoggedIn()

    fun logout() {
        users.logout()
    }

    fun changeDisplay(grid: Boolean) {
        gridDisplay = grid
        users.saveGridDisplay(grid)
    }

    companion object {
        // Users necesita un Context para abrir las preferencias; el de la aplicación vive
        // lo mismo que la app, así que no se queda guardada ninguna pantalla
        val Factory = viewModelFactory {
            initializer { UserViewModel(Users(checkNotNull(this[APPLICATION_KEY]))) }
        }
    }
}
