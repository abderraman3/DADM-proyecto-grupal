package dadm.jromsev.sportnew.ui.player

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dadm.jromsev.sportnew.R
import dadm.jromsev.sportnew.data.player.PlayerRepository
import dadm.jromsev.sportnew.ui.domain.model.Player
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val repository: PlayerRepository,
    @ApplicationContext private val context: Context
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
                        _errorState.value = Throwable(context.getString(R.string.no_players_found))
                    }
                },
                onFailure = { error -> _errorState.value = error }
            )
        }
        _isLoading.value = false
    }

    fun getNewPlayersMultiple(name: String, sports: List<String>) {
        viewModelScope.launch {
            _isLoading.value = true
            val deferredResults = sports.map { sport ->
                async {
                    repository.getNewPlayers(name, sport)
                }
            }
            val results = deferredResults.awaitAll()
            val allPlayers = mutableListOf<Player>()
            var anyError: Throwable? = null

            results.forEach { result ->
                result.fold(
                    onSuccess = { players -> allPlayers.addAll(players) },
                    onFailure = { error -> anyError = error }
                )
            }

            _players.value = allPlayers
            if (allPlayers.isEmpty()) {
                val errorMessage = context.getString(R.string.no_players_found)
                _errorState.value = anyError ?: Throwable(errorMessage)
            } else {
                _errorState.value = null
            }
            _isLoading.value = false
        }
    }

    fun getNewPlayersAll(name: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val sports = context.resources.getStringArray(R.array.sports_values).toList()
            val deferredResults = sports.map { sport ->
                async {
                    repository.getNewPlayers(name, sport)
                }
            }
            val results = deferredResults.awaitAll()
            val allPlayers = mutableListOf<Player>()
            var anyError: Throwable? = null

            results.forEach { result ->
                result.fold(
                    onSuccess = { players ->
                        allPlayers.addAll(players)
                    },
                    onFailure = { error ->
                        anyError = error
                    }
                )
            }

            _players.value = allPlayers
            if (allPlayers.isEmpty()) {
                val errorMessage = context.getString(R.string.no_players_found)
                _errorState.value = anyError ?: Throwable(errorMessage)
            } else {
                _errorState.value = null
            }
            _isLoading.value = false
        }
    }
}