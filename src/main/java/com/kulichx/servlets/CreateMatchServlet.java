package com.kulichx.servlets;

import com.kulichx.entity.Matches;
import com.kulichx.entity.Players;
import com.kulichx.util.HibernateUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@WebServlet(name = "CreateMatchServlet", value = "/new-match")
public class    CreateMatchServlet extends HttpServlet {

    private static final ConcurrentMap<UUID, Matches> currentMatches = new ConcurrentHashMap<>();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("new-match.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Получаем параметры из формы
        String player1Name = request.getParameter("player1");
        String player2Name = request.getParameter("player2");

        // Открываем сессию Hibernate
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Начинаем транзакцию
            Transaction transaction = session.beginTransaction();

            try {
                // Получаем объекты Players из базы данных по именам
                Players player1 = getPlayerByName(session, player1Name);
                Players player2 = getPlayerByName(session, player2Name);

                // Проверяем существование игроков в таблице Players и создаем их, если не существуют
                if (player1 == null) {
                    player1 = createPlayer(session, player1Name);
                }
                if (player2 == null) {
                    player2 = createPlayer(session, player2Name);
                }

                // Создаем объект Matches
                Matches match = new Matches();
                match.setPlayer1(player1);
                match.setPlayer2(player2);

                // Сохраняем матч в коллекцию текущих матчей
                UUID matchId = UUID.randomUUID();
                currentMatches.put(matchId, match);

                // Фиксируем транзакцию
                transaction.commit();

                // Редирект на страницу /match-score?uuid=$match_id
                response.sendRedirect("/match-score?uuid=" + matchId);
            } catch (Exception e) {
                // В случае ошибки откатываем транзакцию
                transaction.rollback();
                e.printStackTrace();
            }
        }
    }

    private Players getPlayerByName(Session session, String playerName) {
        String hql = "FROM Players WHERE name = :name";
        List<Players> playerList = session.createQuery(hql, Players.class)
                .setParameter("name", playerName)
                .getResultList();
        if (!playerList.isEmpty()) {
            return playerList.get(0);
        } else {
            return null;
        }
    }

        private Players createPlayer(Session session, String playerName) {
            // Создаем нового игрока и сохраняем его в базе данных
            Players newPlayer = new Players();
            newPlayer.setName(playerName);
            session.persist(newPlayer);
            return newPlayer;
        }
}
