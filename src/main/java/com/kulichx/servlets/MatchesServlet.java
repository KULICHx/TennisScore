package com.kulichx.servlets;

import com.kulichx.controllers.RecentMatchesController;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "MatchesServlet", value = "/matches")
public class MatchesServlet extends HttpServlet {
    RecentMatchesController recentMatchesController = new RecentMatchesController();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        recentMatchesController.handleGet(request);
        request.getRequestDispatcher("matches.jsp").forward(request, response);
    }
}