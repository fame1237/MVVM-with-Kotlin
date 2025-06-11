package co.storylog.pinto.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.storylog.pinto.domain.usecase.GeneratePredictionsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LotteryViewModel(
    private val generatePredictionsUseCase: GeneratePredictionsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<LotteryUiState>(LotteryUiState.Loading)
    val state: StateFlow<LotteryUiState> = _state.asStateFlow()

    init {
        loadPredictions()
    }

    fun loadPredictions() {
        viewModelScope.launch {
            generatePredictionsUseCase()
                .collect { result ->
                    _state.value = LotteryUiState.Success(result.twoDigits, result.threeDigits)
                }
        }
    }
}

sealed interface LotteryUiState {
    object Loading : LotteryUiState
    data class Success(
        val twoDigits: List<String>,
        val threeDigits: List<String>
    ) : LotteryUiState
}
