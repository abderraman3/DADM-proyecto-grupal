package dadm.jromsev.sportnew.data.player

import dadm.jromsev.sportnew.ui.PlayerDto
import kotlin.random.Random

object PlayerDtoGenerator {
    private val names = listOf(
        "Luis", "Carlos", "Juan", "Lucia",
        "Pepe", "Manolo", "Batman", "Superman", "Wonder Woman"
    )

    fun generateRandomPlayers(count: Int = 10): List<PlayerDto> {
        return List(count) { generateRandomPlayer() }
    }

    private fun generateRandomPlayer(): PlayerDto {
        return PlayerDto(
            idPlayer = Random.nextInt(1, 101).toString(),
            idTeam = Random.nextInt(1, 101).toString(),
            strPlayer = names.random(),
            strTeam = Random.nextInt(1, 101).toString(),
            strSport = Random.nextInt(1, 101).toString(),
            strThumb = Random.nextInt(1, 101).toString(),
            strCutout = Random.nextInt(1, 101).toString(),
            strNationality = Random.nextInt(1, 101).toString(),
            dateBorn = Random.nextInt(1, 101).toString(),
            strStatus = Random.nextInt(1, 101).toString(),
            strGender = Random.nextInt(1, 101).toString(),
            strPosition = Random.nextInt(1, 101).toString(),
            relevance = Random.nextInt(1, 101).toString()
        )
    }

    fun generateRandomPlayersJson(count: Int = 10): String {
        val players = generateRandomPlayers(count)
        return """
            {
                "players": [
                    ${players.joinToString(",\n                    ") { playerToJson(it) }}
                ]
            }
        """.trimIndent()
    }

    private fun playerToJson(player: PlayerDto): String {
        return """
            {
                "idPlayer": "${player.idPlayer}",
                "idTeam": "${player.idTeam}",
                "strPlayer": "${player.strPlayer}",
                "strTeam": "${player.strTeam}",
                "strSport": "${player.strSport}",
                "strThumb": "${player.strThumb}",
                "strCutout": "${player.strCutout}",
                "strNationality": "${player.strNationality}",
                "dateBorn": "${player.dateBorn}",
                "strStatus": "${player.strStatus}",
                "strGender": "${player.strGender}",
                "strPosition": "${player.strPosition}",
                "relevance": "${player.relevance}"
            }
        """.trimIndent()
    }
}