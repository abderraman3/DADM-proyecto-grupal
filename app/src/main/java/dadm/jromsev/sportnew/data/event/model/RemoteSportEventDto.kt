package dadm.jromsev.sportnew.data.event.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SportEventDto(
    val idEvent: String,
    val strEvent: String,
    val strSport: String,
    val strLeague: String,
    val strSeason: String,
    val strHomeTeam: String,
    val strAwayTeam: String,
    val intHomeScore: String?,
    val intAwayScore: String?,
    val dateEvent: String?,
    val strTime: String?,
    val strStatus: String?,
    val strLeagueBadge: String?
)


@JsonClass(generateAdapter = true)
data class RemoteSportEventDto(
    @Json(name="events")
    val events: List<SportEventDto>?
)

@JsonClass(generateAdapter = true)
data class EventSearchResponseDto(
    @Json(name = "event")
    val events: List<SportEventDto>?
)