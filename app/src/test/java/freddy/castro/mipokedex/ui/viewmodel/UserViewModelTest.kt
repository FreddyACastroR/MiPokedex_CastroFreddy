package freddy.castro.mipokedex.ui.viewmodel

import freddy.castro.mipokedex.data.FakeContext
import freddy.castro.mipokedex.data.FakeSharedPreferences
import freddy.castro.mipokedex.data.Users
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/** Las cuentas son las de [Users]; las preferencias viven en memoria. */
class UserViewModelTest {

    private val prefs = FakeSharedPreferences()

    // Otro ViewModel sobre las mismas preferencias es como volver a abrir la app
    private fun viewModel() = UserViewModel(Users(FakeContext(prefs)))

    @Test
    fun login_withValidData_hasNoError() {
        val viewModel = viewModel()

        viewModel.login("ash@pokedex.com", "pikachu")

        assertFalse(viewModel.loginError)
        assertEquals("", viewModel.errorDescription)
        assertTrue(viewModel.isLoggedIn())
    }

    @Test
    fun login_withWrongData_showsTheError() {
        val viewModel = viewModel()

        viewModel.login("ash@pokedex.com", "charmander")

        assertTrue(viewModel.loginError)
        assertEquals(LOGIN_ERROR, viewModel.errorDescription)
        assertFalse(viewModel.isLoggedIn())
    }

    @Test
    fun login_afterAnError_clearsIt() {
        val viewModel = viewModel()
        viewModel.login("gary@pokedex.com", "eevee")

        viewModel.login("ash@pokedex.com", "pikachu")

        assertFalse(viewModel.loginError)
        assertEquals("", viewModel.errorDescription)
    }

    @Test
    fun isLoggedIn_survivesReopeningTheApp() {
        viewModel().login("misty@pokedex.com", "starmie")

        assertTrue(viewModel().isLoggedIn())
    }

    @Test
    fun logout_endsTheSession() {
        val viewModel = viewModel()
        viewModel.login("brock@pokedex.com", "onix")

        viewModel.logout()

        assertFalse(viewModel.isLoggedIn())
    }

    @Test
    fun changeDisplay_isRememberedAfterReopeningTheApp() {
        val viewModel = viewModel()
        assertFalse(viewModel.gridDisplay)

        viewModel.changeDisplay(true)

        assertTrue(viewModel.gridDisplay)
        assertTrue(viewModel().gridDisplay)
    }
}
