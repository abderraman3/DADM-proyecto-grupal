package dadm.jromsev.sportnew.domain.model.repository

import dadm.jromsev.sportnew.domain.model.Player

interface PlayerRepository {
    suspend fun getNewPlayers(name: String, sport: String): Result<List<Player>>
}