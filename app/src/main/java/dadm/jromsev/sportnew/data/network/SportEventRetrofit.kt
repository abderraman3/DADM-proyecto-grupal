package dadm.jromsev.sportnew.data.network

import dadm.jromsev.sportnew.data.event.model.EventSearchResponseDto
import dadm.jromsev.sportnew.data.event.model.RemoteSportEventDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SportEventRetrofit {
    @GET("api/v1/json/3/eventsseason.php")
    suspend fun getEventsBySeason(
        @Query("id") leagueId: String,
        @Query("s") season: String
    ): Response<RemoteSportEventDto>

    @GET("api/v1/json/3/searchevents.php")
    suspend fun searchEvents(
        @Query("e") eventName: String,
    ): Response<EventSearchResponseDto>
}