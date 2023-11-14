package com.kulichx.servies;// MatchGeneratorService
import com.kulichx.dao.MatchesDao;
import com.kulichx.entity.Matches;
import com.kulichx.entity.Players;

public class MatchGeneratorService {

    private final MatchesDao matchesDao;

    public MatchGeneratorService(MatchesDao matchesDao) {
        this.matchesDao = matchesDao;
    }

    public Matches createNewMatch(Players player1, Players player2) {
        Matches match = new Matches();
        match.setPlayer1(player1);
        match.setPlayer2(player2);

        matchesDao.save(match);
        return match;
    }
}
