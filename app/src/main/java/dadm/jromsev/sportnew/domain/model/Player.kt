package dadm.jromsev.sportnew.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

//Data class para los jugadores
@Parcelize
data class Player(
    val player: String,
    val team: String,
    val sport: String,
    val image: String?,
    val nationality: String,
    val dateBorn: String,
    val status: String?,
    val gender: String,
    val position: String,
    val relevance: String?
) : Parcelable