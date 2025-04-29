package dadm.jromsev.sportnew.domain.model.repository

import dadm.jromsev.sportnew.data.event.SportEventDataSource
import dadm.jromsev.sportnew.domain.model.SportEvent
import dadm.jromsev.sportnew.utils.NoInternetException
import dadm.jromsev.sportnew.data.event.model.toSportEvent
import dadm.jromsev.sportnew.data.network.ConnectivityChecker
import javax.inject.Inject

class SportEventRepositoryImpl @Inject constructor(
    private val sportEventDataSource: SportEventDataSource,
    private val connectivityChecker: ConnectivityChecker
) : SportEventRepository {

    override suspend fun getEventsBySeason(leagueId: String, season: String): Result<List<SportEvent>> {
        return if (connectivityChecker.isConnectionAvailable()) {

            val response = sportEventDataSource.getEventsBySeason(leagueId, season)

            if (response.isSuccessful) {
                val remoteDto = response.body()

                val allSportEvents = remoteDto?.events?.map { it.toSportEvent() } ?: emptyList()

                if (allSportEvents.isNotEmpty()) {
                    Result.success(allSportEvents)
                } else {
                    Result.success(emptyList())
                }
            } else {
                Result.failure(Exception("Error fetching events"))
            }

        } else {
            Result.failure(NoInternetException())
        }
    }

    override suspend fun searchEvents(eventName: String): Result<List<SportEvent>> {
        return if (connectivityChecker.isConnectionAvailable()) {

            val response = sportEventDataSource.searchEvents(eventName)

            if (response.isSuccessful) {
                val remoteDto = response.body()

                val sportEvents = remoteDto?.events?.map { it.toSportEvent() } ?: emptyList()

                Result.success(sportEvents)
            } else {
                Result.failure(Exception("Error searching events"))
            }

        } else {
            Result.failure(NoInternetException())
        }
    }
}