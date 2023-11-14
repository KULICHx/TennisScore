package com.kulichx.dao;

import com.kulichx.entity.Players;
import com.kulichx.util.HibernateUtil;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;

public class PlayersDao implements DaoInterface<Players> {

    @Override
    public Optional<Players> findById(Integer id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Optional<Players> toReturn = Optional.of(session.get(Players.class, id));
        session.close();
        return toReturn;
    }

    public Optional<Players> findByName(String name){

        Session session = HibernateUtil.getSessionFactory().openSession();
        String hql = "FROM Players WHERE name = :name";
        List<Players> PlayersList = session.createQuery(hql).setParameter("name", name).getResultList();
        int countPlayers = PlayersList.size();
        if (countPlayers == 1) {
            return Optional.of(PlayersList.get(0));
        } else {
            return Optional.empty();
        }
    }


    @Override
    public void save(Players entity) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.persist(entity);
        tx1.commit();
        session.close();
    }

    @Override
    public void update(Players entity) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.merge(entity);
        tx1.commit();
        session.close();
    }

    @Override
    public void delete(Players entity) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.remove(entity);
        tx1.commit();
        session.close();

    }

    @Override
    public List<Players> findAll() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        CriteriaBuilder cb = (CriteriaBuilder) session.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery(Players.class);
        Root rootEntry = cq.from(Players.class);
        CriteriaQuery all = cq.select(rootEntry);
        TypedQuery allQuery = (TypedQuery) session.createQuery(String.valueOf(all));

        List<Players> Players = (List<Players>) allQuery.getResultList();
        session.close();
        return Players;
    }

}
