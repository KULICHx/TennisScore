package com.kulichx.service;

import com.kulichx.entity.Matches;

public class MatchScoreCalculationService {

    private FinishedMatchesPersistenceService finishedMatchesPersistenceService;
    private OngoingMatchesService ongoingMatchesService;

    // Метод для обновления счета матча
    public void updateScore(Matches match, int winningPlayerId) {

        if (match == null) {
            // Обработка случая, когда match равен null
            System.out.println("Match is null. Cannot update score.");
            return;
        }

        // Получение текущего счета матча
        if (match.getMatchScore() == null) {
            // Обработка случая, когда matchScore равен null
            System.out.println("Match score is null. Cannot update score.");
            return;
        }

        // Получение текущего счета матча
        int player1Score = match.getMatchScore().getPlayer1Score();
        int player2Score = match.getMatchScore().getPlayer2Score();

        // Обновление счета в зависимости от того, какой игрок выиграл очко
        if (winningPlayerId == match.getPlayer1().getId()) {
            player1Score++;
            match.setWinner(match.getPlayer1());
        } else if (winningPlayerId == match.getPlayer2().getId()) {
            player2Score++;
            match.setWinner(match.getPlayer2());
        }
        match.getMatchScore().setPlayer1Score(player1Score);
        match.getMatchScore().setPlayer2Score(player2Score);

    }

}
