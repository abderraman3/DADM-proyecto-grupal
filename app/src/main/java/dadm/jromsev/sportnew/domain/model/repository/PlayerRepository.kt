package dadm.jromsev.sportnew.domain.model.repository

import dadm.jromsev.sportnew.domain.model.Player

interface PlayerRepository {
    suspend fun getNewPlayers(name: String, sport: String): Result<List<Player>>
    suspend fun getPlayerByName(name: String): Player?
    suspend fun insertPlayer(player: Player)
    suspend fun deletePlayer(player: Player)
    suspend fun getAllPlayers(): List<Player>
}