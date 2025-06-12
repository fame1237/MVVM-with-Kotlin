package co.storylog.pinto.data.repository

import co.storylog.pinto.domain.analysis.LotteryResult
import kotlinx.coroutines.flow.Flow

interface LotteryRepository {
    fun getResults(): Flow<List<LotteryResult>>
}
