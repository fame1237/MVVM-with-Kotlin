package co.storylog.pinto.data.repository

import co.storylog.pinto.domain.analysis.LotteryResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeLotteryRepository : LotteryRepository {
    override fun getResults(): Flow<List<LotteryResult>> = flow {
        val data = listOf(
            LotteryResult("2024-06-01", "12", listOf("123", "456")),
            LotteryResult("2024-05-16", "34", listOf("234", "567")),
            LotteryResult("2024-05-01", "12", listOf("789", "123")),
            LotteryResult("2024-04-16", "56", listOf("234", "890"))
        )
        emit(data)
    }
}
