package chawan.fame.lottery.data.repository

import chawan.fame.lottery.data.local.LotteryResultEntity
import kotlinx.coroutines.flow.Flow

interface LotteryRepository {
    fun getHistory(): Flow<List<LotteryResultEntity>>
    suspend fun fetchAndCacheHistory()
}
