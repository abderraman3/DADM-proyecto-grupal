package dadm.jromsev.sportnew.ui.domain.model

data class Player (
    val player: String,
    val team: String,
    val sport: String,
    val image: String?,
    val nationality: String,
    val dateBorn: String,
    val status: String?,
    val gender: String,
    val position: String
)