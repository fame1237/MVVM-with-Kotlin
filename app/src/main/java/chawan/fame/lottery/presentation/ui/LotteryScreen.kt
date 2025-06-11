package chawan.fame.lottery.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import chawan.fame.lottery.presentation.viewmodel.LotteryUiState
import chawan.fame.lottery.presentation.viewmodel.LotteryViewModel

@Composable
fun LotteryScreen(viewModel: LotteryViewModel) {
    LaunchedEffect(Unit) { viewModel.loadPrediction() }
    val uiState by viewModel.uiState.collectAsState()
    LotteryContent(uiState)
}

@Composable
private fun LotteryContent(state: LotteryUiState) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (state) {
            LotteryUiState.Loading -> Text("Loading...", style = MaterialTheme.typography.bodyLarge)
            is LotteryUiState.Success -> {
                val data = state.data
                Text("Predicted Last 2 digits: ${'$'}{data.predictedLast2}")
                Text("Predicted Last 3 digits: ${'$'}{data.predictedLast3}")
            }
            is LotteryUiState.Error -> Text("Error: ${'$'}{state.message}")
        }
    }
}
