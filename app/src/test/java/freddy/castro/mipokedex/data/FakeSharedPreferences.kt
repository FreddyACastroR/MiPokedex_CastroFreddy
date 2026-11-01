package freddy.castro.mipokedex.data

import android.content.ContextWrapper
import android.content.SharedPreferences

/**
 * SharedPreferences en memoria para las pruebas, que corren sin Android. Igual que las de
 * verdad, lo que se pone en el editor no se ve hasta guardarlo con apply() o commit().
 */
class FakeSharedPreferences : SharedPreferences {

    private val values = mutableMapOf<String, Any>()

    @Suppress("UNCHECKED_CAST")
    private fun <T> read(key: String?, defValue: T): T = values[key] as T? ?: defValue

    override fun getAll(): Map<String, *> = values.toMap()
    override fun getString(key: String?, defValue: String?): String? = read(key, defValue)
    override fun getStringSet(key: String?, defValues: MutableSet<String>?): MutableSet<String>? =
        read(key, defValues)
    override fun getInt(key: String?, defValue: Int): Int = read(key, defValue)
    override fun getLong(key: String?, defValue: Long): Long = read(key, defValue)
    override fun getFloat(key: String?, defValue: Float): Float = read(key, defValue)
    override fun getBoolean(key: String?, defValue: Boolean): Boolean = read(key, defValue)
    override fun contains(key: String?): Boolean = key in values

    override fun edit(): SharedPreferences.Editor = Editor()

    override fun registerOnSharedPreferenceChangeListener(
        listener: SharedPreferences.OnSharedPreferenceChangeListener?,
    ) = Unit

    override fun unregisterOnSharedPreferenceChangeListener(
        listener: SharedPreferences.OnSharedPreferenceChangeListener?,
    ) = Unit

    private inner class Editor : SharedPreferences.Editor {
        // Un valor null quiere decir que se borra, igual que putString(clave, null)
        private val changes = mutableMapOf<String, Any?>()
        private var clear = false

        private fun put(key: String?, value: Any?): SharedPreferences.Editor {
            changes[checkNotNull(key)] = value
            return this
        }

        override fun putString(key: String?, value: String?) = put(key, value)
        override fun putStringSet(key: String?, values: MutableSet<String>?) = put(key, values)
        override fun putInt(key: String?, value: Int) = put(key, value)
        override fun putLong(key: String?, value: Long) = put(key, value)
        override fun putFloat(key: String?, value: Float) = put(key, value)
        override fun putBoolean(key: String?, value: Boolean) = put(key, value)
        override fun remove(key: String?) = put(key, null)

        override fun clear(): SharedPreferences.Editor {
            clear = true
            return this
        }

        override fun commit(): Boolean {
            apply()
            return true
        }

        override fun apply() {
            if (clear) values.clear()
            changes.forEach { (key, value) ->
                if (value == null) values.remove(key) else values[key] = value
            }
        }
    }
}

/** Context que solo sabe abrir [prefs], que es lo único que pide [Users]. */
class FakeContext(private val prefs: SharedPreferences) : ContextWrapper(null) {
    override fun getSharedPreferences(name: String?, mode: Int): SharedPreferences = prefs
}
