package chawan.fame.lottery.domain.usecases

import chawan.fame.lottery.data.local.LotteryResultEntity
import chawan.fame.lottery.data.repository.LotteryRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PredictLotteryUseCaseTest {

    @Test
    fun `invoke returns most frequent digits`() = runTest {
        val repo = mockk<LotteryRepository>()
        coEvery { repo.getHistory() } returns flowOf(
            listOf(
                LotteryResultEntity(1, "2024-01-01", "12", "123"),
                LotteryResultEntity(2, "2024-02-01", "12", "999"),
                LotteryResultEntity(3, "2024-03-01", "34", "123")
            )
        )
        val useCase = PredictLotteryUseCase(repo)

        val result = useCase().first()
        assertEquals("12", result.last2)
        assertEquals("123", result.last3)
    }
}
