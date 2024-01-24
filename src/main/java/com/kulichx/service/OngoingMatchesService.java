package com.kulichx.service;

import com.kulichx.entity.Matches;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class OngoingMatchesService {

    // Коллекция текущих матчей (замените на свою реализацию)
    private static final Map<UUID, Matches> ongoingMatches = new HashMap<>();

    // Добавление матча в коллекцию
    public static void addMatch(UUID matchId, Matches match) {
        ongoingMatches.put(matchId, match);
    }

    // Получение матча по его UUID
    public Matches getMatchById(UUID matchId) {
        return ongoingMatches.get(matchId);
    }

    // Удаление матча из коллекции
    public void removeMatch(UUID matchId) {
        ongoingMatches.remove(matchId);
    }
}
