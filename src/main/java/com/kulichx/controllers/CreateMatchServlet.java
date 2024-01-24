package com.kulichx.controllers;

import com.kulichx.dao.PlayersDao;
import com.kulichx.entity.Matches;
import com.kulichx.service.MatchCreationService;
import com.kulichx.service.OngoingMatchesService;
import com.kulichx.util.HibernateUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.UUID;

@WebServlet(name = "CreateMatchServlet", value = "/new-match")
public class CreateMatchServlet extends HttpServlet {

    private MatchCreationService matchCreationService;
    private OngoingMatchesService ongoingMatchesService;

    @Override
    public void init() throws ServletException {
        // Инициализация сервиса
        matchCreationService = new MatchCreationService(new PlayersDao(HibernateUtil.getSessionFactory()));
        ongoingMatchesService = new OngoingMatchesService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Получение параметров запроса (имен игроков и других данных)
            String player1Name = request.getParameter("player1");
            String player2Name = request.getParameter("player2");

            // Создание матча и получение его UUID
            UUID matchId = matchCreationService.newMatch(player1Name, player2Name);

            Matches match = matchCreationService.getMatch(matchId);
            OngoingMatchesService.addMatch(matchId, match);

            // Отправка уникального UUID в качестве ответа
            response.getWriter().write(matchId.toString());
        } catch (Exception e) {
            // Обработка ошибок (логгирование и т. д.)
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error");
        }
    }
}
