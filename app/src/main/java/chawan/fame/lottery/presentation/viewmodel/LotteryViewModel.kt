package chawan.fame.lottery.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import chawan.fame.lottery.domain.usecases.PredictLotteryUseCase
import chawan.fame.lottery.presentation.model.LotteryUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface LotteryUiState {
    object Loading : LotteryUiState
    data class Success(val data: LotteryUiModel) : LotteryUiState
    data class Error(val message: String) : LotteryUiState
}

class LotteryViewModel(
    private val predictLotteryUseCase: PredictLotteryUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<LotteryUiState>(LotteryUiState.Loading)
    val uiState: StateFlow<LotteryUiState> = _uiState.asStateFlow()

    fun loadPrediction() {
        viewModelScope.launch {
            try {
                predictLotteryUseCase().collect { prediction ->
                    _uiState.value = LotteryUiState.Success(
                        LotteryUiModel(
                            prediction.last2,
                            prediction.last3
                        )
                    )
                }
            } catch (e: Exception) {
                _uiState.value = LotteryUiState.Error(e.message ?: "Unknown")
            }
        }
    }
}
