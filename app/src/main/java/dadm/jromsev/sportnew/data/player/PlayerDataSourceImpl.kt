package dadm.jromsev.sportnew.data.player

import dadm.jromsev.sportnew.data.player.model.RemotePlayerDto
import retrofit2.Response
import javax.inject.Inject

class PlayerDataSourceImpl @Inject constructor(
    private val service: PlayerRetrofit
) : PlayerDataSource {
    override suspend fun getPlayer(name: String, sport: String): Response<RemotePlayerDto> {
        return try {
            Response.success(service.getPlayer(name, sport))
        } catch (e: Exception) {
            Response.error(500, okhttp3.ResponseBody.create(null, e.message ?: "Unknown error"))
        }
    }
}