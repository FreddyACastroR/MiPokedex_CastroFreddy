package freddy.castro.mipokedex.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class UsersTest {

    private val prefs = FakeSharedPreferences()

    private fun users() = Users(FakeContext(prefs))

    @Test
    fun getUserEmailAndName_comeFromTheSavedSession() {
        users().loginService("ash@pokedex.com", "pikachu")

        assertEquals("ash@pokedex.com", users().getUserEmail())
        assertEquals("Ash", users().getUserName())
    }

    @Test
    fun getUserEmailAndName_withoutSession_areNull() {
        val users = users()
        users.loginService("misty@pokedex.com", "starmie")

        users.logout()

        assertNull(users.getUserEmail())
        assertNull(users.getUserName())
    }

    @Test
    fun getUserByMail_ignoresCaseAndSurroundingSpaces() {
        val user = users().getUserByMail("  Ash@Pokedex.com ")

        assertEquals("Ash", user?.name)
    }

    @Test
    fun getUserByMail_unknownMail_isNull() {
        assertNull(users().getUserByMail("gary@pokedex.com"))
    }

    @Test
    fun loginService_withValidData_returnsTheUserAndSavesTheSession() {
        val users = users()

        assertNotNull(users.loginService("ash@pokedex.com", "pikachu"))
        assertTrue(users.isLoggedIn())
    }

    @Test
    fun loginService_passwordIsCaseSensitive() {
        val users = users()

        assertNull(users.loginService("ash@pokedex.com", "Pikachu"))
        assertFalse(users.isLoggedIn())
    }

    @Test
    fun loginService_withWrongData_doesNotSaveASession() {
        val users = users()

        assertNull(users.loginService("ash@pokedex.com", "charmander"))
        assertNull(users.loginService("gary@pokedex.com", "pikachu"))
        assertFalse(users.isLoggedIn())
    }

    @Test
    fun session_isStillThereAfterReopeningTheApp() {
        users().loginService("misty@pokedex.com", "starmie")

        assertTrue(users().isLoggedIn())
    }

    @Test
    fun logout_endsTheSession() {
        val users = users()
        users.loginService("brock@pokedex.com", "onix")

        users.logout()

        assertFalse(users.isLoggedIn())
        assertFalse(users().isLoggedIn())
    }

    @Test
    fun gridDisplay_startsAsListAndIsSaved() {
        assertFalse(users().isGridDisplay())

        users().saveGridDisplay(true)

        assertTrue(users().isGridDisplay())
    }

    @Test
    fun logout_keepsTheChosenDisplay() {
        val users = users()
        users.loginService("ash@pokedex.com", "pikachu")
        users.saveGridDisplay(true)

        users.logout()

        assertTrue(users.isGridDisplay())
    }
}
