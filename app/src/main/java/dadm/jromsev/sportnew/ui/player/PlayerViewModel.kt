package dadm.jromsev.sportnew.ui.player

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dadm.jromsev.sportnew.data.player.PlayerRepository
import dadm.jromsev.sportnew.ui.domain.model.Player
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val repository: PlayerRepository
) : ViewModel(){

    private val _players = MutableLiveData<List<Player>>()
    val players: LiveData<List<Player>> get() = _players

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading.asStateFlow()

    private val _errorState = MutableStateFlow<Throwable?>(null)
    val errorState: StateFlow<Throwable?> get() = _errorState.asStateFlow()

    fun getNewPlayers(name: String, sport: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.getNewPlayers(name, sport)
            result.fold(
                onSuccess = { playersList ->

                    _players.value = playersList

                    if (playersList.isEmpty()) {
                        _errorState.value = Throwable("No se han encontrado jugadores")
                    }
                },
                onFailure = { error -> _errorState.value = error }
            )
        }
        _isLoading.value = false
    }
}