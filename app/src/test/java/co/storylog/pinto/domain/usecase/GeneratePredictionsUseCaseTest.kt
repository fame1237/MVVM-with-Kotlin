package co.storylog.pinto.domain.usecase

import co.storylog.pinto.data.repository.FakeLotteryRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class GeneratePredictionsUseCaseTest {

    @Test
    fun `invoke returns predictions`() = runTest {
        val repo = FakeLotteryRepository()
        val useCase = GeneratePredictionsUseCase(repo)

        val result = useCase().first()

        assertEquals("12", result.twoDigits.first())
        assertEquals("234", result.threeDigits.first())
    }
}
