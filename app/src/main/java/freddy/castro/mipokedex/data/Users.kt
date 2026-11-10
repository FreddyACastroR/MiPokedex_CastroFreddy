package freddy.castro.mipokedex.data

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import freddy.castro.mipokedex.data.preferences.ARCHIVO
import freddy.castro.mipokedex.data.preferences.POKEMON_DISPLAY
import freddy.castro.mipokedex.data.preferences.USER
import freddy.castro.mipokedex.data.preferences.USERNAME
import freddy.castro.mipokedex.domain.User

class Users(context: Context) {

    private val userList = listOf(
        User("ash@pokedex.com", "pikachu", "Ash"),
        User("misty@pokedex.com", "starmie", "Misty"),
        User("brock@pokedex.com", "onix", "Brock"),
    )

    private val shared: SharedPreferences =
        context.getSharedPreferences(ARCHIVO, Context.MODE_PRIVATE)

    fun getUserByMail(mail: String): User? =
        userList.find { it.email.equals(mail.trim(), ignoreCase = true) }

    fun loginService(email: String, password: String): User? {
        val user = getUserByMail(email)?.takeIf { it.password == password } ?: return null
        saveUser(user.email, user.name)
        return user
    }

    fun saveUser(email: String, name: String) {
        shared.edit {
            putString(USER, email)
            putString(USERNAME, name)
        }
    }

    fun getUserEmail(): String? = shared.getString(USER, null)

    fun getUserName(): String? = shared.getString(USERNAME, null)

    fun isLoggedIn(): Boolean = getUserEmail() != null && getUserName() != null

    fun logout() {
        shared.edit {
            remove(USER)
            remove(USERNAME)
        }
    }

    fun isGridDisplay(): Boolean = shared.getBoolean(POKEMON_DISPLAY, false)

    fun saveGridDisplay(grid: Boolean) {
        shared.edit { putBoolean(POKEMON_DISPLAY, grid) }
    }
}
