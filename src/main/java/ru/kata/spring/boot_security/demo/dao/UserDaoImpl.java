package ru.kata.spring.boot_security.demo.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.User;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserDaoImpl implements UserDao {
    private static final Logger log = LoggerFactory.getLogger(UserDaoImpl.class.getName());

    @PersistenceContext
    private EntityManager em;

    @Override
    public User save(User user) {
        log.debug("save: <- " + user);
        em.persist(user);
        log.debug("save: -> " + user);
        return user;
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> list(int count) {
        log.debug("list: <- " + count);
        TypedQuery<User> query = em.createQuery("select u from User u", User.class);
        return query.getResultList().stream().limit(count).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> listAll() {
        log.debug("listAll: <- ");
        return em.createQuery("select u from User u", User.class).getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    //@EntityGraph(value = "User.roles")
    public User find(Long id) {
        User usr;
        log.debug("find: <- " + id);
        List<User> usrs = em.createQuery("select u from User u where u.id = :id", User.class)
                .setParameter("id", id)
                .getResultList();
        usr = usrs.isEmpty() ? null : usrs.get(0);
        log.debug("find: -> " + usr);
        return usr;
    }

    @Override
    public void delete(User user) {
        log.debug("delete: <- " + user);
        if (user == null) {
            log.warn("delete: user must not be null.");
            return;
        }
        delete(user.getId());
    }

    @Override
    public void delete(Long id) {
        log.debug("delete: <- " + id);
        int cnt = em.createQuery("delete from User where id = :id")
                .setParameter("id", id)
                .executeUpdate();
        String status = (cnt == 1) ? "deleted successfully" : "not found";
        log.debug("delete: -> User with id=" + id + " " + status);
    }

    @Override
    public User update(long id, User user) {
        log.debug("update: <- " + user);
        User usr = em.merge(user);
        log.debug("update: -> " + usr);
        return usr;
    }

    @Override
    public User findUserByUsername(String username) {
        log.debug("findUserByUsername: <- " + username);
        EntityGraph entityGraph = em.getEntityGraph("User.roles");
        List<User> usrs = em.createQuery("select u from User u where u.username = :username", User.class)
                .setParameter("username", username)
                .setHint("javax.persistence.fetchgraph", entityGraph)
                .getResultList();
        User usr = usrs.isEmpty() ? null : usrs.get(0);
        log.debug("findUserByUsername: -> " + usr);
        return usr;
    }
}

