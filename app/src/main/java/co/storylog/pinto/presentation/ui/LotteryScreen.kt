package co.storylog.pinto.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.Preview
import co.storylog.pinto.presentation.viewmodel.LotteryUiState
import co.storylog.pinto.presentation.viewmodel.LotteryViewModel

@Composable
fun LotteryScreen(viewModel: LotteryViewModel) {
    val state = viewModel.state.collectAsState()
    when (val uiState = state.value) {
        LotteryUiState.Loading -> Text("Loading...")
        is LotteryUiState.Success -> LotteryContent(
            twoDigits = uiState.twoDigits,
            threeDigits = uiState.threeDigits,
            onRefresh = { viewModel.loadPredictions() }
        )
    }
}

@Composable
private fun LotteryContent(
    twoDigits: List<String>,
    threeDigits: List<String>,
    onRefresh: () -> Unit
) {
    Column {
        Text("Two-digit predictions:", style = MaterialTheme.typography.titleMedium)
        LazyColumn {
            items(twoDigits) { item ->
                Text(text = item)
            }
        }
        Text("Three-digit predictions:", style = MaterialTheme.typography.titleMedium)
        LazyColumn {
            items(threeDigits) { item ->
                Text(text = item)
            }
        }
        Button(onClick = onRefresh) {
            Text("Refresh")
        }
    }
}

@Preview
@Composable
private fun PreviewLotteryContent() {
    LotteryContent(
        twoDigits = listOf("12", "34"),
        threeDigits = listOf("123", "234"),
        onRefresh = {}
    )
}
