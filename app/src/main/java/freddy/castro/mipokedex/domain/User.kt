package freddy.castro.mipokedex.domain

/** Entrenador que puede entrar a la Pokédex con su correo y contraseña. */
data class User(
    val email: String,
    val password: String,
    val name: String,
)
