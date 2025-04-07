package dadm.jromsev.sportnew.data.player

import dadm.jromsev.sportnew.data.player.model.RemotePlayerDto
import okhttp3.MediaType
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import javax.inject.Inject

class PlayerDataSourceImpl @Inject constructor(
    private val retrofit: Retrofit
) : PlayerDataSource {

    private val retrofitPlayerService: PlayerRetrofit =
        retrofit.create(PlayerRetrofit::class.java)

    override suspend fun getPlayers(name: String): Response<RemotePlayerDto> {
        return try {
            retrofitPlayerService.getPlayers(name)
        } catch (e: Exception) {
            Response.error(
                400,
                ResponseBody.create(
                    MediaType.parse("text/plain"),
                    e.toString()
                )
            )
        }
    }
}