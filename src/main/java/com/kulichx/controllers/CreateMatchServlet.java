package com.kulichx.controllers;

import com.kulichx.dao.MatchesDao;
import com.kulichx.dao.PlayersDao;
import com.kulichx.servies.MatchCreationService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "CreateMatchServlet", value = "/new-match")
public class CreateMatchServlet extends HttpServlet {

    public static final PlayersDao playersDao = new PlayersDao();
    private static final MatchesDao matchesDao = new MatchesDao();
    private static final MatchCreationService matchCreationService = new MatchCreationService(matchesDao, playersDao);

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String player1Name = request.getParameter("player1");
        String player2Name = request.getParameter("player2");

        try {
            // Создаем новый матч с использованием MatchCreationService
            int matchId = matchCreationService.createNewMatch(player1Name, player2Name);

            // Отправляем идентификатор матча в ответе
            response.getWriter().write(String.valueOf(matchId));
        } catch (Exception e) {
            handleMatchCreationError(response);
        }
    }

    private void handleMatchCreationError(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // Установка статуса 500
        response.getWriter().write("error");
    }

}
