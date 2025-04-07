package dadm.jromsev.sportnew.data.player.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RemotePlayerDto(
    val player: List<PlayerData>?
) {
    @JsonClass(generateAdapter = true)
    data class PlayerData(
        val idPlayer: String?,
        val strPlayer: String?,
        val strTeam: String?,
        val strSport: String?,
        val strThumb: String?,
        val strNationality: String?,
        val dateBorn: String?,
        val strPosition: String?
    )
}