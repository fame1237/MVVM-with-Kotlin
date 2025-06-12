package chawan.fame.lottery.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [LotteryResultEntity::class], version = 1)
abstract class LotteryDatabase : RoomDatabase() {
    abstract fun lotteryResultDao(): LotteryResultDao
}
