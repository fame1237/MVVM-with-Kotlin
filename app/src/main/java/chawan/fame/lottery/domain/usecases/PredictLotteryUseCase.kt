package chawan.fame.lottery.domain.usecases

import chawan.fame.lottery.data.repository.LotteryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PredictLotteryUseCase(private val repository: LotteryRepository) {
    /**
     * Predict next 2 and 3 digit numbers based on most frequent historical values.
     */
    operator fun invoke(): Flow<Prediction> = repository.getHistory().map { list ->
        val last2 = list.groupingBy { it.last2 }.eachCount().maxByOrNull { it.value }?.key ?: "--"
        val last3 = list.groupingBy { it.last3 }.eachCount().maxByOrNull { it.value }?.key ?: "---"
        Prediction(last2, last3)
    }
}

data class Prediction(val last2: String, val last3: String)
