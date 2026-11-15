package freddy.castro.mipokedex.data.sqlite

import android.provider.BaseColumns

object Contract {

    object Pokemons : BaseColumns {
        const val TABLE_NAME = "pokemons"
        const val COLUMN_ID = BaseColumns._ID
        const val COLUMN_NAME_NUMBER = "number"
        const val COLUMN_NAME_NAME = "name"
        const val COLUMN_NAME_DESCRIPTION = "description"
        const val COLUMN_NAME_TYPE = "type"
    }
}
