package chawan.fame.lottery.presentation

import chawan.fame.lottery.domain.usecases.PredictLotteryUseCase
import chawan.fame.lottery.domain.usecases.Prediction
import chawan.fame.lottery.presentation.viewmodel.LotteryUiState
import chawan.fame.lottery.presentation.viewmodel.LotteryViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LotteryViewModelTest {

    @Test
    fun `loadPrediction emits success state`() = runTest {
        val useCase = mockk<PredictLotteryUseCase>()
        coEvery { useCase.invoke() } returns flowOf(Prediction("11", "111"))
        val viewModel = LotteryViewModel(useCase)

        viewModel.loadPrediction()

        assertTrue(viewModel.uiState.value is LotteryUiState.Success)
    }
}
