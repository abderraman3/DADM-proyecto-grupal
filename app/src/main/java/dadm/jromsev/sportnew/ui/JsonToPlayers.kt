package dadm.jromsev.sportnew.ui

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dadm.jromsev.sportnew.ui.domain.model.Player

class JsonToPlayers {
    private val moshi: Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val playerListType = Types.newParameterizedType(
        List::class.java,
        PlayerDto::class.java
    )

    private val jsonAdapter: JsonAdapter<List<PlayerDto>> = moshi.adapter(playerListType)

    fun convert(json: String): List<Player> {
        return try {
            val playerDtos = jsonAdapter.fromJson(json) ?: emptyList()
            playerDtos.map { dto ->
                Player(
                    player = dto.strPlayer,
                    team = dto.strTeam,
                    sport = dto.strSport,
                    image = dto.strThumb,
                    nationality = dto.strNationality,
                    dateBorn = dto.dateBorn,
                    status = dto.strStatus,
                    gender = dto.strGender,
                    position = dto.strPosition
                )
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}