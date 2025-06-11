package chawan.fame.lottery.data.remote

import retrofit2.http.GET

/**
 * API service for lottery results
 */
interface LotteryApiService {
    @GET("lottery/history.json")
    suspend fun getLotteryHistory(): List<LotteryResultDto>
}

data class LotteryResultDto(
    val id: Long,
    val drawDate: String,
    val last2: String,
    val last3: String
)
