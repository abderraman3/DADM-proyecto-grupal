package dadm.jromsev.sportnew.domain.model.repository

import dadm.jromsev.sportnew.data.network.ConnectivityChecker
import dadm.jromsev.sportnew.data.player.PlayerDataSource
import dadm.jromsev.sportnew.data.player.model.PlayerDto
import dadm.jromsev.sportnew.domain.model.Player
import dadm.jromsev.sportnew.data.player.model.toDomain
import dadm.jromsev.sportnew.utils.NoInternetException
import javax.inject.Inject

class PlayerRepositoryImpl @Inject constructor(
    private val playerDataSource: PlayerDataSource,
    private val connectivityChecker: ConnectivityChecker
) : PlayerRepository {
    override suspend fun getNewPlayers(name: String, sport: String): Result<List<Player>> {
        return if (connectivityChecker.isConnectionAvailable()) {

            val response = playerDataSource.getPlayers(name)

            val remoteDto = response.body()

            val allRemotePlayers: MutableList<PlayerDto> = mutableListOf()

            remoteDto?.player?.let { allRemotePlayers.addAll(it) }

            if (allRemotePlayers.isNotEmpty()) {

                val convertedPlayers: List<Player> = allRemotePlayers.map { playerDto ->
                    playerDto.toDomain()
                }

                val filteredPlayers: List<Player> = convertedPlayers.filter { player ->
                    player.sport.equals(sport, ignoreCase = true) && player.status.equals(
                        "Active",
                        ignoreCase = true
                    )
                }

                if (filteredPlayers.isNotEmpty()) {
                    Result.success(filteredPlayers)
                } else {
                    Result.success(emptyList())
                }
            } else {
                Result.success(emptyList())
            }

        } else {
            Result.failure(NoInternetException())
        }
    }
    }

