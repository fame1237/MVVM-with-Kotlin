package co.storylog.pinto.domain.analysis

import org.junit.Assert.assertEquals
import org.junit.Test

class LotteryAnalysisTest {

    private val sampleData = listOf(
        LotteryResult("2024-06-01", "12", listOf("123", "456")),
        LotteryResult("2024-05-16", "34", listOf("234", "567")),
        LotteryResult("2024-05-01", "12", listOf("789", "123")),
        LotteryResult("2024-04-16", "56", listOf("234", "890"))
    )

    @Test
    fun `calculateDigitFrequency returns correct counts`() {
        val freq = calculateDigitFrequency(sampleData)
        assertEquals(3, freq[1])
        assertEquals(4, freq[2])
        assertEquals(2, freq[3])
    }

    @Test
    fun `getHotNumbers returns digits sorted by frequency`() {
        val hot = getHotNumbers(sampleData, recentDraws = 4)
        assertEquals(10, hot.size)
        // First digit should be '2' because it appears most often in sampleData
        assertEquals(2, hot.first())
    }

    @Test
    fun `getTopPairs returns most frequent pairs`() {
        val pairs = getTopPairs(sampleData, top = 2)
        assertEquals(listOf("12", "34"), pairs)
    }

    @Test
    fun `prediction engine returns top numbers`() {
        val twoDigit = LotteryPredictionEngine.predictTwoDigit(sampleData)
        assertEquals("12", twoDigit.first())
        val threeDigit = LotteryPredictionEngine.predictThreeDigit(sampleData)
        assertEquals("234", threeDigit.first())
    }
}
