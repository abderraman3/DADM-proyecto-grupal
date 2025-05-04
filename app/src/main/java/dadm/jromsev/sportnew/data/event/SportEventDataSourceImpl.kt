package dadm.jromsev.sportnew.data.event

import dadm.jromsev.sportnew.data.event.model.RemoteSportEventDto
import dadm.jromsev.sportnew.data.network.SportEventRetrofit
import retrofit2.Response
import retrofit2.Retrofit
import android.util.Log // Importa il log per usarlo
import dadm.jromsev.sportnew.data.event.model.EventSearchResponseDto
import javax.inject.Inject

// Implementación del origen de datos para eventos deportivos. Maneja las llamadas a la API para obtener eventos por temporada, búsqueda y ronda.
class SportEventDataSourceImpl @Inject constructor(
    private val retrofit: Retrofit
) : SportEventDataSource {

    private val apiService = retrofit.create(SportEventRetrofit::class.java)

    // Obtiene eventos deportivos de la API según la liga y la temporada.
    override suspend fun getEventsBySeason(leagueId: String, season: String): Response<RemoteSportEventDto> {
        //Log.d("API_CALL", "Making API call for leagueId: $leagueId and season: $season") // Log all'inizio della chiamata
        val response = apiService.getEventsBySeason(leagueId, season)

        // Log della risposta grezza
        if (response.isSuccessful) {
            val body = response.body()?.let { it.toString() } ?: "No events data"
           // Log.d("API_RESPONSE", "Response body: $body") // Stampa il corpo della risposta
        } else {
            //Log.d("API_RESPONSE", "Error: ${response.code()}")
        }

        return response
    }

    // Busca eventos deportivos por nombre de evento utilizando la API.
    override suspend fun searchEvents(eventName: String): Response<EventSearchResponseDto> {
       // Log.d("API_CALL", "Searching events for query: $eventName")
        val response = apiService.searchEvents(eventName)

        if (response.isSuccessful) {
            val body = response.body()?.let { it.toString() } ?: "No events data"
            //Log.d("API_RESPONSE", "Search response body: $body")
        } else {
            //Log.d("API_RESPONSE", "Search error: ${response.code()}")
        }

        return response
    }
    // Obtiene eventos deportivos de la API según la liga, temporada y ronda.
    override suspend fun getEventsByRound(leagueId: String, season: String, round: String): Response<RemoteSportEventDto> {
        //Log.d("API_CALL", "Fetching events for leagueId: $leagueId, season: $season, round: $round")
        val response = apiService.getEventsByRound(leagueId, round, season)

        if (response.isSuccessful) {
            val body = response.body()?.let { it.toString() } ?: "No events data"
            //Log.d("API_RESPONSE", "Round events response body: $body")
        } else {
           // Log.d("API_RESPONSE", "Round events error: ${response.code()}")
        }

        return response
    }
}
