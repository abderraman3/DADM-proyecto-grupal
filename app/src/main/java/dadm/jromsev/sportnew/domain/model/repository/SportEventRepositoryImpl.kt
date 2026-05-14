package dadm.jromsev.sportnew.domain.model.repository

import dadm.jromsev.sportnew.data.event.SportEventDataSource
import dadm.jromsev.sportnew.domain.model.SportEvent
import dadm.jromsev.sportnew.utils.NoInternetException
import dadm.jromsev.sportnew.data.event.model.toSportEvent
import dadm.jromsev.sportnew.data.network.ConnectivityChecker
import javax.inject.Inject


// Implementación del repositorio de eventos deportivos. Maneja las llamadas a la fuente de datos y verifica la conexión.
class SportEventRepositoryImpl @Inject constructor(
    private val sportEventDataSource: SportEventDataSource,
    private val connectivityChecker: ConnectivityChecker
) : SportEventRepository {

    // Obtiene eventos deportivos según la liga y la temporada proporcionadas. Requiere conexión a internet.
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

    // Busca eventos deportivos por nombre utilizando la fuente de datos remota. Verifica si hay conexión.
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

    // Recupera eventos deportivos filtrando por liga, temporada y jornada. Solo funciona con conexión disponible.
    override suspend fun getEventsByRound(leagueId: String, season: String, round: String): Result<List<SportEvent>> {
        return if (connectivityChecker.isConnectionAvailable()) {
            val response = sportEventDataSource.getEventsByRound(leagueId, season, round)
            if (response.isSuccessful) {
                val remoteDto = response.body()
                val sportEvents = remoteDto?.events?.map { it.toSportEvent() } ?: emptyList()
                Result.success(sportEvents)
            } else {
                Result.failure(Exception("Error fetching events by round"))
            }

        } else {
            Result.failure(NoInternetException())
    }
}}