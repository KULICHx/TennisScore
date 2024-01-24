package com.kulichx.controllers;

import com.kulichx.dao.MatchesDao;
import com.kulichx.entity.Matches;
import com.kulichx.service.FinishedMatchesPersistenceService;
import com.kulichx.service.MatchScoreCalculationService;
import com.kulichx.service.OngoingMatchesService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.UUID;

@WebServlet(name = "MatchScoreServlet", value = "/match-score")
public class MatchScoreServlet extends HttpServlet {

    private OngoingMatchesService ongoingMatchesService;
    private MatchScoreCalculationService matchScoreCalculationService;
    private FinishedMatchesPersistenceService finishedMatchesPersistenceService;

    @Override
    public void init() throws ServletException {
        // Инициализация сервисов
        ongoingMatchesService = new OngoingMatchesService();  // Ваш сервис
        matchScoreCalculationService = new MatchScoreCalculationService();  // Ваш сервис
        finishedMatchesPersistenceService = new FinishedMatchesPersistenceService();  // Ваш сервис
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Получение параметров запроса (UUID матча и id выигравшего игрока)
            UUID matchId = UUID.fromString(request.getParameter("uuid"));
            int winningPlayerId = Integer.parseInt(request.getParameter("winningPlayerId"));

            // Получение экземпляра матча из OngoingMatchesService
            Matches currentMatch = ongoingMatchesService.getMatchById(matchId);

            // Обновление счета матча через MatchScoreCalculationService
            matchScoreCalculationService.updateScore(currentMatch, winningPlayerId);

            // Проверка, завершен ли матч
            if (MatchesDao.isFinished(currentMatch)) {

                // Сохранение законченного матча в базу данных
                finishedMatchesPersistenceService.saveMatch(currentMatch);

                ongoingMatchesService.removeMatch(matchId);

                // Рендеринг финального счета
                // renderFinalScore(response, currentMatch);
                response.getWriter().write("Match finished");
            } else {
                // Рендеринг таблицы счета матча
                // renderMatchScore(response, currentMatch);
                response.getWriter().write("Match score updated");
            }
        } catch (Exception e) {
            // Обработка ошибок (логгирование и т. д.)
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error");
        }
    }
}
