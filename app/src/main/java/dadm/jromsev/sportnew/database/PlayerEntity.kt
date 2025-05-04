package dadm.jromsev.sportnew.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import dadm.jromsev.sportnew.domain.model.Player

// Entidad de base de datos que representa a un jugador, usada por Room para el almacenamiento local.
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

// Convierte un objeto del dominio Player a una entidad PlayerEntity para la base de datos.
fun Player.toEntity() = PlayerEntity(player, team, sport, image, nationality, dateBorn, status, gender, position, relevance)

// Convierte una entidad PlayerEntity a un objeto del dominio Player.
fun PlayerEntity.toPlayer() = Player(player, team, sport, image, nationality, dateBorn, status, gender, position, relevance)
