package chawan.fame.testmvvm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import co.storylog.pinto.data.repository.FakeLotteryRepository
import co.storylog.pinto.domain.usecase.GeneratePredictionsUseCase
import co.storylog.pinto.presentation.ui.LotteryScreen
import co.storylog.pinto.presentation.viewmodel.LotteryViewModel

class MainActivity : ComponentActivity() {

    private val viewModel by lazy {
        val repository = FakeLotteryRepository()
        val useCase = GeneratePredictionsUseCase(repository)
        LotteryViewModel(useCase)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LotteryScreen(viewModel)
        }
    }
}
