package chawan.fame.lottery.data.repository

import chawan.fame.lottery.data.local.LotteryDatabase
import chawan.fame.lottery.data.local.LotteryResultEntity
import chawan.fame.lottery.data.remote.LotteryApiService
import kotlinx.coroutines.flow.Flow

class LotteryRepositoryImpl(
    private val api: LotteryApiService,
    private val db: LotteryDatabase
) : LotteryRepository {

    override fun getHistory(): Flow<List<LotteryResultEntity>> =
        db.lotteryResultDao().getAll()

    override suspend fun fetchAndCacheHistory() {
        val remote = api.getLotteryHistory()
        val entities = remote.map {
            LotteryResultEntity(
                id = it.id,
                drawDate = it.drawDate,
                last2 = it.last2,
                last3 = it.last3
            )
        }
        db.lotteryResultDao().insertAll(entities)
    }
}
