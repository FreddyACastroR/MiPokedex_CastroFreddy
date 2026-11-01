package freddy.castro.mipokedex.data

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import freddy.castro.mipokedex.data.preferences.ARCHIVO
import freddy.castro.mipokedex.data.preferences.POKEMON_DISPLAY
import freddy.castro.mipokedex.data.preferences.USER
import freddy.castro.mipokedex.data.preferences.USERNAME
import freddy.castro.mipokedex.domain.User

/**
 * Cuentas de la app y lo que se guarda en el teléfono: la sesión y cómo se ven los Pokémon.
 * Se guarda con SharedPreferences, así sigue ahí aunque se cierre la app. El [Context] solo
 * hace falta para abrir el archivo, por eso no se queda guardado.
 */
class Users(context: Context) {

    // La app no tiene servidor, así que las cuentas viven aquí
    private val userList = listOf(
        User("ash@pokedex.com", "pikachu", "Ash"),
        User("misty@pokedex.com", "starmie", "Misty"),
        User("brock@pokedex.com", "onix", "Brock"),
    )

    private val shared: SharedPreferences =
        context.getSharedPreferences(ARCHIVO, Context.MODE_PRIVATE)

    /**
     * El usuario con ese correo, o null si no hay. No distingue mayúsculas ni espacios en los
     * extremos; la contraseña sí distingue.
     */
    fun getUserByMail(mail: String): User? =
        userList.find { it.email.equals(mail.trim(), ignoreCase = true) }

    /** El usuario si el correo y la contraseña coinciden, y su sesión queda guardada; si no, null. */
    fun loginService(email: String, password: String): User? {
        val user = getUserByMail(email)?.takeIf { it.password == password } ?: return null
        saveUser(user.email, user.name)
        return user
    }

    fun saveUser(email: String, name: String) {
        // edit {} abre el editor y al terminar guarda los cambios con apply()
        shared.edit {
            putString(USER, email)
            putString(USERNAME, name)
        }
    }

    /** Hay sesión si quedó guardado un usuario con su nombre. */
    fun isLoggedIn(): Boolean =
        shared.getString(USER, null) != null && shared.getString(USERNAME, null) != null

    /** Borra la sesión. La vista elegida se queda: es preferencia del teléfono, no de la sesión. */
    fun logout() {
        shared.edit {
            remove(USER)
            remove(USERNAME)
        }
    }

    /** Si el menú muestra los Pokémon en tabla; la primera vez, en lista. */
    fun isGridDisplay(): Boolean = shared.getBoolean(POKEMON_DISPLAY, false)

    fun saveGridDisplay(grid: Boolean) {
        shared.edit { putBoolean(POKEMON_DISPLAY, grid) }
    }
}
