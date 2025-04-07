package dadm.jromsev.sportnew.data.player

import dadm.jromsev.sportnew.ui.domain.model.Player

interface PlayerRepository {
    abstract val it: Any

    suspend fun getNewPlayers(name: String, sport: String): Result<List<Player>>
}