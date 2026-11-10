package freddy.castro.mipokedex.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import freddy.castro.mipokedex.data.Users
import freddy.castro.mipokedex.pokedexApplication

const val LOGIN_ERROR = "Usuario o contraseña incorrectos"

class UserViewModel(private val users: Users) : ViewModel() {

    var loginError by mutableStateOf(false)
        private set

    var errorDescription by mutableStateOf("")
        private set

    var gridDisplay by mutableStateOf(users.isGridDisplay())
        private set

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
        val Factory = viewModelFactory {
            initializer { UserViewModel(pokedexApplication.users) }
        }
    }
}
