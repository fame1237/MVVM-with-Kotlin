package co.storylog.pinto.domain.usecase

import co.storylog.pinto.data.repository.LotteryRepository
import co.storylog.pinto.domain.analysis.LotteryPredictionEngine
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GeneratePredictionsUseCase(
    private val repository: LotteryRepository
) {
    operator fun invoke(): Flow<PredictionResult> {
        return repository.getResults().map { results ->
            PredictionResult(
                twoDigits = LotteryPredictionEngine.predictTwoDigit(results),
                threeDigits = LotteryPredictionEngine.predictThreeDigit(results)
            )
        }
    }
}

data class PredictionResult(
    val twoDigits: List<String>,
    val threeDigits: List<String>
)
