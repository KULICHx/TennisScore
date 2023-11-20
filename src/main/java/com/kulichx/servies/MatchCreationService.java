package com.kulichx.servies;

import com.kulichx.dao.PlayersDao;
import com.kulichx.entity.Matches;
import com.kulichx.entity.Players;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MatchCreationService {

    private final PlayersDao playersDao;
    private final Map<UUID, Matches> currentMatches;

    public MatchCreationService(PlayersDao playersDao) {
        this.playersDao = playersDao;
        this.currentMatches = new HashMap<>();
    }

    public UUID newMatch(String player1Name, String player2Name) {
        // Получаем или создаем игроков
        Players player1 = playersDao.getOrCreatePlayer(player1Name);
        Players player2 = playersDao.getOrCreatePlayer(player2Name);

        // Создаем матч
        Matches match = new Matches(player1, player2);

        // Помещаем матч в коллекцию текущих матчей с использованием UUID
        UUID matchId = UUID.randomUUID();
        currentMatches.put(matchId, match);

        return matchId;
    }

    public Matches getMatch(UUID matchId) {
        return currentMatches.get(matchId);
    }
}
