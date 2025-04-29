package dadm.jromsev.sportnew.domain.model.repository

import dadm.jromsev.sportnew.domain.model.SportEvent

interface SportEventRepository {
    suspend fun getEventsBySeason(leagueId: String, season: String): Result<List<SportEvent>>
    suspend fun searchEvents(eventName: String): Result<List<SportEvent>>
}