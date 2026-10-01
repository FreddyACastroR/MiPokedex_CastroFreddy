package freddy.castro.mipokedex.data

import freddy.castro.mipokedex.R
import freddy.castro.mipokedex.domain.Pokemon

/** Un solo Pokémon, para los Previews de los componentes. */
val bulbasaur = Pokemon(
    name = "Bulbasaur",
    number = 1,
    type = "Grass/Poison",
    description = "There is a plant seed on its back from the day this Pokémon is born.",
    height = 0.7f,
    weight = 6.9f,
    favorite = false,
    ability = "Overgrow",
    image = R.drawable.bulbasaur,
)

val pokemonList = listOf(
    bulbasaur,
    Pokemon("Charmander", 4, "Fire", "The flame on its tail shows the strength of its life force.", 0.6f, 8.5f, true, "Blaze", R.drawable.charmander),
    Pokemon("Squirtle", 7, "Water", "After birth, its back swells and hardens into a shell.", 0.5f, 9.0f, false, "Torrent", R.drawable.squirtle),
    Pokemon("Pikachu", 25, "Electric", "When several of these Pokémon gather, their electricity could build and cause lightning storms.", 0.4f, 6.0f, true, "Static", R.drawable.pikachu),
    Pokemon("Jigglypuff", 39, "Normal/Fairy", "Uses its alluring eyes to enrapture its foe, then sings a pleasant melody to lull them to sleep.", 0.5f, 5.5f, false, "Cute Charm", R.drawable.jigglypuff),
    Pokemon("Gengar", 94, "Ghost/Poison", "Under a full moon, this Pokémon likes to mimic the shadows of people and laugh at their fright.", 1.5f, 40.5f, true, "Cursed Body", R.drawable.gengar),
    Pokemon("Snorlax", 143, "Normal", "Its stomach is said to be so strong that it can even eat moldy or rotten food.", 2.1f, 460.0f, false, "Immunity", R.drawable.snorlax),
    Pokemon("Mewtwo", 150, "Psychic", "It was created by a scientist after years of horrific gene-splicing and DNA engineering experiments.", 2.0f, 122.0f, true, "Pressure", R.drawable.mewtwo),
    Pokemon("Lucario", 448, "Fighting/Steel", "By catching the aura emanating from others, it can read their thoughts and movements.", 1.2f, 54.0f, true, "Steadfast", R.drawable.lucario),
    Pokemon("Mimikyu", 778, "Ghost/Fairy", "Its actual appearance is unknown. A scholar who saw what was under its rag overwhelmed by terror and died.", 0.2f, 0.7f, false, "Disguise", R.drawable.mimikyu),
)
