package com.kulichx.service;

import com.kulichx.entity.Matches;

import com.kulichx.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class FinishedMatchesPersistenceService {

    public void saveMatch(Matches match) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            // Сохранение матча в базе данных
            session.saveOrUpdate(match);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }
}
