package dadm.jromsev.sportnew.data.event

import dadm.jromsev.sportnew.data.event.model.EventSearchResponseDto
import dadm.jromsev.sportnew.data.event.model.RemoteSportEventDto
import retrofit2.Response

interface SportEventDataSource {
    suspend fun getEventsBySeason(leagueId: String, season: String): Response<RemoteSportEventDto>
    suspend fun searchEvents(eventName: String): Response<EventSearchResponseDto>
    suspend fun getEventsByRound(leagueId: String, season: String, round: String ): Response<RemoteSportEventDto>
}