package chawan.fame.lottery.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = LotteryResultEntity.TABLE_NAME)
data class LotteryResultEntity(
    @PrimaryKey
    @ColumnInfo(name = COLUMN_ID)
    val id: Long,
    @ColumnInfo(name = COLUMN_DRAW_DATE)
    val drawDate: String,
    @ColumnInfo(name = COLUMN_LAST2)
    val last2: String,
    @ColumnInfo(name = COLUMN_LAST3)
    val last3: String
) {
    companion object {
        const val TABLE_NAME = "lottery_result"
        const val COLUMN_ID = "id"
        const val COLUMN_DRAW_DATE = "draw_date"
        const val COLUMN_LAST2 = "last2"
        const val COLUMN_LAST3 = "last3"
    }
}
