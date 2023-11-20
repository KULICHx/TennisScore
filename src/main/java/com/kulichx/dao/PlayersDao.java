package com.kulichx.dao;

import com.kulichx.entity.Players;
import com.kulichx.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

public class PlayersDao {
    private final SessionFactory sessionFactory;

    public PlayersDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Players save(Players player) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            if (player.getId() == 0) {
                session.persist(player);
            } else {
                session.merge(player);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace(); // Лучше логировать ошибку
        } finally {
            session.close();
        }

        return player;
    }


    public Players getOrCreatePlayer(String playerName) {
        // Получение сессии Hibernate
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        Players player = getPlayerByName(playerName);

        if (player == null) {
            // Игрок не существует, создаем нового
            player = new Players();
            player.setName(playerName);

            // Проверка, что имя не является null или пустой строкой
            if (player.getName() != null && !player.getName().isEmpty()) {
                session.saveOrUpdate(player);
            } else {
                System.out.println("erooooor");
            }
        }

        session.getTransaction().commit();
        return player;
    }

    public Players getPlayerByName(String playerName) {
        // Получение сессии Hibernate
        Session session = HibernateUtil.getSessionFactory().openSession(); // Изменено на openSession()

        try {
            session.beginTransaction();

            // Поиск игрока по имени
            Query<Players> query = session.createQuery("from Players where name = :name", Players.class);
            query.setParameter("name", playerName);
            Players player = query.uniqueResult();

            session.getTransaction().commit();
            return player;
        } catch (Exception e) {
            if (session.getTransaction() != null && session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            throw e;
        } finally {
            session.close();
        }
    }
}