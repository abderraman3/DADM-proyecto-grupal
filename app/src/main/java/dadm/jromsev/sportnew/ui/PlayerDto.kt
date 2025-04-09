package dadm.jromsev.sportnew.ui

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PlayerDto(
    val idPlayer: String,
    val idTeam: String,
    val strPlayer: String,
    val strTeam: String,
    val strSport: String,
    val strThumb: String?,
    val strCutout: String?,
    val strNationality: String,
    val dateBorn: String,
    val strStatus: String?,
    val strGender: String,
    val strPosition: String,
    val relevance: String
)
