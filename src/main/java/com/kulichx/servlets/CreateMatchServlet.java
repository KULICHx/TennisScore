package com.kulichx.servlets;

import com.kulichx.dao.MatchesDao;
import com.kulichx.dao.PlayersDao;
import com.kulichx.entity.Matches;
import com.kulichx.entity.Players;
import com.kulichx.servies.MatchGeneratorService;
import com.kulichx.util.HibernateUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@WebServlet(name = "CreateMatchServlet", value = "/new-match")
public class CreateMatchServlet extends HttpServlet {

    private static final PlayersDao pd = new PlayersDao();
    private static final MatchesDao matchesDao = new MatchesDao();
    private static final MatchGeneratorService matchService = new MatchGeneratorService(matchesDao);
    private static final Logger logger = LoggerFactory.getLogger(CreateMatchServlet.class);
    private static final String MATCH_SCORE_PAGE_URL = "/match-score?id=";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("new-match.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String player1Name = request.getParameter("player1");
        String player2Name = request.getParameter("player2");

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            try {
                if (player1Name.equals(player2Name)) {
                    response.sendRedirect(MATCH_SCORE_PAGE_URL + "invalid");
                    return;
                }

                Players player1 = pd.getPlayerByName(session, player1Name);
                Players player2 = pd.getPlayerByName(session, player2Name);

                if (player1 == null) {
                    player1 = pd.createPlayer(session, player1Name);
                }
                if (player2 == null) {
                    player2 = pd.createPlayer(session, player2Name);
                }

                Matches match = matchService.createNewMatch(player1, player2);

                transaction.commit();

                response.sendRedirect(MATCH_SCORE_PAGE_URL + match.getId());  // Используем id матча, если он у вас есть
            } catch (Exception e) {
                transaction.rollback();
                logger.error("Error processing new match request", e);
                response.sendRedirect(MATCH_SCORE_PAGE_URL + "error");
            }
        }
    }
}
