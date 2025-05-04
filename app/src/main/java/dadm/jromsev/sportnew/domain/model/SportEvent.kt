package dadm.jromsev.sportnew.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SportEvent(
    val idEvent: String,
    val eventName: String,
    val sport: String,
    val leagueName: String,
    val season: String?,
    val homeTeam: String?,
    val awayTeam: String?,
    val homeScore: String?,
    val awayScore: String?,
    val dateEvent: String?,
    val timeEvent: String?,
    val status: String?,
    val leagueBadge: String?
) : Parcelable