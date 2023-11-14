package com.kulichx.controllers;

import com.kulichx.dao.MatchesDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "MatchScoreController", value = "/match-score")
public class MatchScoreController extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Получение параметров из запроса, например, UUID матча
        String uuid = request.getParameter("uuid");

        // Получение информации о матче по UUID из коллекции или базы данных
        MatchesDao.get(uuid).ifPresent(match -> {
            // Установка атрибутов для передачи информации в JSP
            request.setAttribute("p1name", match.getPlayer1().getName());
            request.setAttribute("p2name", match.getPlayer2().getName());
            request.setAttribute("p1score", match.getP1Score());
            request.setAttribute("p2score", match.getP2Score());
            request.setAttribute("p1game", match.getP1Game());
            request.setAttribute("p2game", match.getP2Game());
            request.setAttribute("p1set", match.getP1Set());
            request.setAttribute("p2set", match.getP2Set());
        });

        // Передача управления JSP для отображения результатов матча
        request.getRequestDispatcher("match-score.jsp").forward(request, response);
    }
}
