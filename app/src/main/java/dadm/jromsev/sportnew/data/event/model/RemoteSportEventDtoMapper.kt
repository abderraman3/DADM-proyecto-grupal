package dadm.jromsev.sportnew.data.event.model

import dadm.jromsev.sportnew.domain.model.SportEvent

fun SportEventDto.toSportEvent(): SportEvent {
    return SportEvent(
        idEvent = idEvent,
        eventName = strEvent,
        sport = strSport,
        leagueName = strLeague,
        season = strSeason,
        homeTeam = strHomeTeam,
        awayTeam = strAwayTeam,
        homeScore = intHomeScore,
        awayScore = intAwayScore,
        dateEvent = dateEvent,
        timeEvent = strTime,
        status = strStatus,
        leagueBadge = strLeagueBadge
    )
}

fun RemoteSportEventDto.toSportEvents(): List<SportEvent> {
    return events?.map { it.toSportEvent() } ?: emptyList()
}
