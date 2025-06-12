package co.storylog.pinto.domain.analysis

/**
 * Data model representing a lottery result. Only the last two-digit and
 * last three-digit prizes are captured for simplicity.
 */
data class LotteryResult(
    val drawDate: String,
    val lastTwoDigits: String,
    val lastThreeDigits: List<String>
)

/**
 * Counts digit frequency (0-9) from the given results.
 */
fun calculateDigitFrequency(results: List<LotteryResult>): Map<Int, Int> {
    val counts = IntArray(10)
    results.forEach { result ->
        (listOf(result.lastTwoDigits) + result.lastThreeDigits).forEach { number ->
            number.forEach { char ->
                val digit = char.digitToIntOrNull() ?: return@forEach
                counts[digit]++
            }
        }
    }
    return counts.mapIndexed { index, value -> index to value }.toMap()
}

/**
 * Returns digits ordered by descending frequency within the last [recentDraws]
 * results to identify "hot" numbers.
 */
fun getHotNumbers(results: List<LotteryResult>, recentDraws: Int = 10): List<Int> {
    val subset = results.takeLast(recentDraws)
    return calculateDigitFrequency(subset)
        .toList()
        .sortedByDescending { it.second }
        .map { it.first }
}

/**
 * Returns digits ordered by ascending frequency within the last [recentDraws]
 * results to identify "cold" numbers.
 */
fun getColdNumbers(results: List<LotteryResult>, recentDraws: Int = 10): List<Int> {
    val subset = results.takeLast(recentDraws)
    return calculateDigitFrequency(subset)
        .toList()
        .sortedBy { it.second }
        .map { it.first }
}

/**
 * Calculates the most common two-digit pairs from all results.
 */
fun getTopPairs(results: List<LotteryResult>, top: Int = 5): List<String> {
    val pairCount = mutableMapOf<String, Int>()
    results.forEach { result ->
        val pair = result.lastTwoDigits
        pairCount[pair] = pairCount.getOrDefault(pair, 0) + 1
    }
    return pairCount
        .toList()
        .sortedByDescending { it.second }
        .take(top)
        .map { it.first }
}

/**
 * Simple prediction engine combining the above analyses to provide candidate
 * two-digit and three-digit numbers.
 */
object LotteryPredictionEngine {
    fun predictTwoDigit(results: List<LotteryResult>): List<String> {
        // Use top appearing pairs as naive prediction
        return getTopPairs(results)
    }

    fun predictThreeDigit(results: List<LotteryResult>): List<String> {
        val freqMap = mutableMapOf<String, Int>()
        results.flatMap { it.lastThreeDigits }.forEach { num ->
            freqMap[num] = freqMap.getOrDefault(num, 0) + 1
        }
        return freqMap
            .toList()
            .sortedByDescending { it.second }
            .take(5)
            .map { it.first }
    }
}

