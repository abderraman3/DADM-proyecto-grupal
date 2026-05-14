package dadm.jromsev.sportnew.ui.event

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dadm.jromsev.sportnew.R
import dadm.jromsev.sportnew.domain.model.repository.SportEventRepository
import dadm.jromsev.sportnew.domain.model.SportEvent
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
class SportEventViewModel @Inject constructor(
    private val repository: SportEventRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _events = MutableLiveData<List<SportEvent>>()
    val events: LiveData<List<SportEvent>> get() = _events
    //Mapa para temporada de cada una liga
    private val sportSeasonMap = mapOf(
        "Serie A" to R.array.seasons_serie_a,
        "La Liga" to R.array.seasons_la_liga,
        "Premier League" to R.array.seasons_premier_league,
        "NBA" to R.array.seasons_nba,
        "NHL" to R.array.seasons_nhl,
        "NFL" to R.array.seasons_nfl
    )

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading.asStateFlow()

    private val _errorState = MutableStateFlow<Throwable?>(null)
    val errorState: StateFlow<Throwable?> get() = _errorState.asStateFlow()

    fun getEventsBySeason(leagueId: String, season: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.getEventsBySeason(leagueId, season)
            result.fold(
                onSuccess = { eventsList ->
                    _events.value = eventsList
                    if (eventsList.isEmpty()) {
                        _errorState.value = Throwable(context.getString(R.string.no_events_found))
                    }
                },
                onFailure = { error -> _errorState.value = error }
            )
            _isLoading.value = false
        }
    }

    fun getMultipleEventsBySeason(leagueId: String, seasons: List<String>) {
        viewModelScope.launch {
            _isLoading.value = true
            val deferredResults = seasons.map { season ->
                async {
                    repository.getEventsBySeason(leagueId, season)
                }
            }
            val results = deferredResults.awaitAll()
            val allEvents = mutableListOf<SportEvent>()
            var anyError: Throwable? = null

            results.forEach { result ->
                result.fold(
                    onSuccess = { events -> allEvents.addAll(events) },
                    onFailure = { error -> anyError = error }
                )
            }

            _events.value = allEvents
            if (allEvents.isEmpty()) {
                val errorMessage = context.getString(R.string.no_events_found)
                _errorState.value = anyError ?: Throwable(errorMessage)
            } else {
                _errorState.value = null
            }
            _isLoading.value = false
        }
    }

    fun getAllEventsByLeague(leagueId: String, selectedSport: String) {
        viewModelScope.launch {
            _isLoading.value = true

            val seasons = context.resources.getStringArray(sportSeasonMap[selectedSport] ?: R.array.seasons_nba).toList()

            val deferredResults = seasons.map { season ->
                async {
                    repository.getEventsBySeason(leagueId, season)
                }
            }

            val results = deferredResults.awaitAll()
            val allEvents = mutableListOf<SportEvent>()
            var anyError: Throwable? = null

            results.forEach { result ->
                result.fold(
                    onSuccess = { events -> allEvents.addAll(events) },
                    onFailure = { error -> anyError = error }
                )
            }

            _events.value = allEvents
            if (allEvents.isEmpty()) {
                val errorMessage = context.getString(R.string.no_events_found)
                _errorState.value = anyError ?: Throwable(errorMessage)
            } else {
                _errorState.value = null
            }
            _isLoading.value = false
        }
    }

    fun searchEventsByQuery(eventName: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.searchEvents(eventName)
            result.fold(
                onSuccess = { eventsList ->
                    _events.value = eventsList
                    if (eventsList.isEmpty()) {
                        _errorState.value = Throwable(context.getString(R.string.no_events_found))
                    } else {
                        _errorState.value = null
                    }
                },
                onFailure = { error ->
                    _errorState.value = error
                }
            )
            _isLoading.value = false
        }
    }
    fun getEventsByRound(leagueId: String, season: String, round: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.getEventsByRound(leagueId, season, round)
            result.fold(
                onSuccess = { eventsList ->
                    _events.value = eventsList
                    if (eventsList.isEmpty()) {
                        _errorState.value = Throwable(context.getString(R.string.no_events_found))
                    } else {
                        _errorState.value = null
                    }
                },
                onFailure = { error ->
                    _errorState.value = error
                }
            )
            _isLoading.value = false
        }
    }
}

