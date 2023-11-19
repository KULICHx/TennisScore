package com.kulichx.dao;


import com.kulichx.entity.Matches;
import com.kulichx.util.HibernateUtil;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.*;

import static com.kulichx.controllers.CreateMatchServlet.playersDao;

public class MatchesDao implements DaoInterface<Matches> {

    @Override
    public Optional<Matches> findById(Integer id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        return Optional.of(session.get(Matches.class, id));
    }

    @Override
    public void save(Matches entity) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            // Сохраняем связанных игроков перед сохранением матча
            playersDao.save(entity.getPlayer1());
            playersDao.save(entity.getPlayer2());

            session.saveOrUpdate(entity);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace(); // Поменяйте это на логирование в продакшене
        } finally {
            session.close();
        }
    }




    @Override
    public void update(Matches entity) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.merge(entity);
        tx1.commit();
        session.close();
    }

    @Override
    public void delete(Matches entity) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.remove(entity);
        tx1.commit();
        session.close();
    }

    @Override
    public List<Matches> findAll() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        CriteriaBuilder cb = (CriteriaBuilder) session.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery(Matches.class);
        Root rootEntry = cq.from(Matches.class);
        CriteriaQuery all = cq.select(rootEntry);
        TypedQuery allQuery = (TypedQuery) session.createQuery(String.valueOf(all));
        List<Matches> Matcheses = (List<Matches>) allQuery.getResultList();
        session.close();
        return Matcheses;
    }

    public List<Matches> findMatchesByPlayer(String name){
        Session session = HibernateUtil.getSessionFactory().openSession();
        String hql = "FROM Matches WHERE player1.name = :name or player2.name = :name";
        List<Matches> Matcheses = session.createQuery(hql).setParameter("name", name).getResultList();

        return Matcheses;
    }

    private static Map<String, Matches> ongoingMatches = new HashMap<String, Matches>();

    public static String put(Matches match) {
        String uuid = UUID.randomUUID().toString();
        while (ongoingMatches.containsKey(uuid)) {
            uuid = UUID.randomUUID().toString();
        }
        ongoingMatches.put(uuid, match);

        // Add this line to remove the match from ongoingMatches after saving to the database
        matchDao.save(match);

        Optional<Matches> optionalMatch = MatchesDao.get(uuid);
        if (optionalMatch.isPresent()) {
            ongoingMatches.remove(uuid);
            return uuid;
        } else {
            // Handle the case when the match is not found
            // You can throw an exception or return an appropriate value
            return null;
        }
    }


    public static void remove(String uuid){
        ongoingMatches.remove(uuid);
    }

    public static Optional<Matches> get(String uuid){
        if (ongoingMatches.containsKey(uuid)){
            return Optional.of(ongoingMatches.get(uuid));
        } else {
            return Optional.empty();
        }

    }
    private static MatchesDao matchDao = new MatchesDao();

    public static void persist(String uuid){
        if (MatchesDao.get(uuid).isEmpty()){
            return;
        }
        matchDao.save(MatchesDao.get(uuid).get());
        MatchesDao.remove(uuid);
    }

    public static Matches get(int id){
        Optional<Matches> optionalMatch = matchDao.findById(id);
        if (optionalMatch.isPresent()){
            return optionalMatch.get();
        } else {
            return null;
        }
    }

    public static List<Matches> getFiltered(String filter){
        return matchDao.findMatchesByPlayer(filter);
    }

    public static List<Matches> getAll(){
        return matchDao.findAll();
    }
}