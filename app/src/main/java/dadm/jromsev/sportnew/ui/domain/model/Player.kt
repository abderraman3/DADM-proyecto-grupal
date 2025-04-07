package dadm.jromsev.sportnew.ui.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Player(
    val id: String,
    val name: String,
    val team: String,
    val sport: String,
    val thumb: String?,
    val nationality: String?,
    val birthDate: String?,
    val position: String?
) : Parcelable