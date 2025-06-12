package chawan.fame.lottery.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface LotteryResultDao {
    @Query("SELECT * FROM ${LotteryResultEntity.TABLE_NAME}")
    fun getAll(): Flow<List<LotteryResultEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<LotteryResultEntity>)
}
