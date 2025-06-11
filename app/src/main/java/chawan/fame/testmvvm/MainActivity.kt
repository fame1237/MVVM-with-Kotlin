package chawan.fame.testmvvm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import chawan.fame.lottery.domain.usecases.PredictLotteryUseCase
import chawan.fame.lottery.presentation.ui.LotteryScreen
import chawan.fame.lottery.presentation.viewmodel.LotteryViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // In real project use DI for viewModel and repository
        val viewModel = LotteryViewModel(PredictLotteryUseCase(object : chawan.fame.lottery.data.repository.LotteryRepository {
            override fun getHistory() = kotlinx.coroutines.flow.flow { emit(emptyList<chawan.fame.lottery.data.local.LotteryResultEntity>()) }
            override suspend fun fetchAndCacheHistory() {}
        }))

        setContent {
            MaterialTheme {
                LotteryScreen(viewModel)
            }
        }
    }
}
