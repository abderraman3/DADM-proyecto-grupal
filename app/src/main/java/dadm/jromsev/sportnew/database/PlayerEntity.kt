package dadm.jromsev.sportnew.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import dadm.jromsev.sportnew.domain.model.Player

@Entity(tableName = "players")
data class PlayerEntity(
    @PrimaryKey val player: String,
    val team: String,
    val sport: String,
    val image: String?,
    val nationality: String,
    val dateBorn: String,
    val status: String?,
    val gender: String,
    val position: String,
    val relevance: String?
)

fun Player.toEntity() = PlayerEntity(player, team, sport, image, nationality, dateBorn, status, gender, position, relevance)
fun PlayerEntity.toPlayer() = Player(player, team, sport, image, nationality, dateBorn, status, gender, position, relevance)

