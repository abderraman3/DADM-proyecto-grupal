package dadm.jromsev.sportnew.data.player.model

import dadm.jromsev.sportnew.ui.domain.model.Player
import retrofit2.Response
import java.io.IOException

fun PlayerDto.toDomain() = Player(
    player= strPlayer,
    team= strTeam,
    sport= strSport,
    image= strCutout,
    nationality= strNationality,
    dateBorn= dateBorn,
    status= strStatus,
    gender= strGender,
    position= strPosition
)

fun Response<PlayerDto>.toDomain() =
    if (isSuccessful) Result.success((body() as PlayerDto).toDomain())
    else Result.failure(IOException())