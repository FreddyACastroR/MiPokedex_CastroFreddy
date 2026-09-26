package freddy.castro.mipokedex.data

import freddy.castro.mipokedex.R

object PokedexData {
    val arbok = Pokemon(R.string.arbok, 24, R.drawable.arbok)
    val pikachu = Pokemon(R.string.pikachu, 25, R.drawable.pikachu)
    val raichu = Pokemon(R.string.raichu, 26, R.drawable.raichu)

    val pikachuEntry = PokedexEntry(
        pokemon = pikachu,
        type = PokemonType.ELECTRIC,
        height = R.string.pikachu_height,
        weight = R.string.pikachu_weight,
        ability = R.string.pikachu_ability,
        description = R.string.pikachu_description,
        previous = arbok,
        next = raichu,
    )
}
