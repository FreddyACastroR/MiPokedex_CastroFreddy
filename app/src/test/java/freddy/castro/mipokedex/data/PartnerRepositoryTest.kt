package freddy.castro.mipokedex.data

import freddy.castro.mipokedex.domain.Partner
import java.time.LocalDate
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class PartnerRepositoryTest {

    private val repository = PartnerRepository(FakeDataStore())

    private val pikachu = Partner(25, LocalDate.of(2026, 11, 7))

    @Test
    fun getPartner_withoutSavingOne_isNull() = runTest {
        assertNull(repository.getPartner("ash@pokedex.com").first())
    }

    @Test
    fun savePartner_keepsTheNumberAndTheDate() = runTest {
        repository.savePartner("ash@pokedex.com", pikachu)

        assertEquals(pikachu, repository.getPartner("ash@pokedex.com").first())
    }

    @Test
    fun savePartner_replacesThePreviousOne() = runTest {
        val gengar = Partner(94, LocalDate.of(2026, 11, 10))
        repository.savePartner("ash@pokedex.com", pikachu)

        repository.savePartner("ash@pokedex.com", gengar)

        assertEquals(gengar, repository.getPartner("ash@pokedex.com").first())
    }

    @Test
    fun eachTrainerHasTheirOwnPartner() = runTest {
        repository.savePartner("ash@pokedex.com", pikachu)

        assertNull(repository.getPartner("misty@pokedex.com").first())
    }
}
