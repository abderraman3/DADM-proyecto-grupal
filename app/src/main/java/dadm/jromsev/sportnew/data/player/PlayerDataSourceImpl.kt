package dadm.jromsev.sportnew.data.player

import dadm.jromsev.sportnew.data.network.PlayerRetrofit
import dadm.jromsev.sportnew.data.player.model.RemotePlayerDto
import okhttp3.MediaType
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import javax.inject.Inject


// Implementación de la fuente de datos para obtener jugadores desde la API utilizando Retrofit.
// La clase maneja la llamada a la API y maneja las excepciones que puedan ocurrir.
class PlayerDataSourceImpl @Inject constructor(
    private val retrofit: Retrofit
) : PlayerDataSource {

    private val retrofitPlayerService: PlayerRetrofit =
        retrofit.create(PlayerRetrofit::class.java)

    // Recupera una lista de jugadores desde la API a través del servicio PlayerRetrofit. Si ocurre un error, devuelve un error 400 con el mensaje de la excepción.
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