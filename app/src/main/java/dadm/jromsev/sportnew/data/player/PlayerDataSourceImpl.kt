package dadm.jromsev.sportnew.data.player

import dadm.jromsev.sportnew.data.player.model.RemotePlayerDto
import retrofit2.Response
import javax.inject.Inject

class PlayerDataSourceImpl @Inject constructor(
    private val service: PlayerRetrofit
) : PlayerDataSource {
    override suspend fun getPlayer(name: String, sport: String): Response<RemotePlayerDto> {
        return service.getPlayers(name, sport.takeIf { it.isNotEmpty() })
    }
}