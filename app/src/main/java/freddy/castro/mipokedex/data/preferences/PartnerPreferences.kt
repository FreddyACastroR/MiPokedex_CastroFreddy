package freddy.castro.mipokedex.data.preferences

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey

const val PARTNER_FILE = "companero"

fun partnerNumberKey(email: String): Preferences.Key<Int> =
    intPreferencesKey("partnerNumber_$email")

fun partnerSinceKey(email: String): Preferences.Key<Long> =
    longPreferencesKey("partnerSince_$email")
