package dadm.jromsev.sportnew.data.player

import dadm.jromsev.sportnew.data.player.model.RemotePlayerDto
import retrofit2.http.GET
import retrofit2.http.Query

interface PlayerRetrofit {
    @GET("searchplayers.php")
    suspend fun getPlayer(
        @Query("p") name: String,
        @Query("s") sport: String
    ): RemotePlayerDto
}