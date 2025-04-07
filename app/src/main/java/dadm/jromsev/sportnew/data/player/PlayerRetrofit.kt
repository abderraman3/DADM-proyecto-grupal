package dadm.jromsev.sportnew.data.player

import dadm.jromsev.sportnew.data.player.model.RemotePlayerDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PlayerRetrofit {
    @GET("searchplayers.php")
    suspend fun getPlayers(
        @Query("p") playerName: String,
        @Query("s") sport: String? = null
    ): Response<RemotePlayerDto>
}