package com.kulichx.servies;

import com.kulichx.dao.MatchesDao;
import com.kulichx.dao.PlayersDao;
import com.kulichx.entity.Matches;
import com.kulichx.entity.Players;

public class MatchCreationService {

    private final MatchesDao matchesDao;
    private final PlayersDao playersDao;

    public MatchCreationService(MatchesDao matchesDao, PlayersDao playersDao) {
        this.matchesDao = matchesDao;
        this.playersDao = playersDao;
    }

    public int createNewMatch(String player1Name, String player2Name) {
        Players player1 = playersDao.getPlayerByName(player1Name);
        Players player2 = playersDao.getPlayerByName(player2Name);

        if (player1 == null) {
            player1 = playersDao.createPlayer(player1Name);
        }
        if (player2 == null) {
            player2 = playersDao.createPlayer(player2Name);
        }

        Matches match = new Matches(player1, player2);
        matchesDao.save(match);

        // Возвращаем идентификатор созданного матча
        return match.getId();
    }
}
