package dadm.jromsev.sportnew.data.player

import dadm.jromsev.sportnew.data.player.model.RemotePlayerDto
import retrofit2.Response

interface PlayerDataSource {
    suspend fun getPlayers(name: String): Response<RemotePlayerDto>
}