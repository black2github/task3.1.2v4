package ru.kata.spring.boot_security.demo.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Component
@Transactional
public class RoleDaoImpl implements RoleDao {
    private static final Logger log = LoggerFactory.getLogger(RoleDaoImpl.class.getName());

    @PersistenceContext
    private EntityManager em;

    @Override
    public Role save(Role role) {
        log.debug("save: <- " + role);
        em.persist(role);
        log.debug("save: -> " + role);
        return role;
    }

    @Override
    public Role find(Long id) {
        Role role;
        log.debug("find: <- " + id);
        List<Role> usrs = em.createQuery("select r from Role r where r.id = :id", Role.class)
                .setParameter("id", id)
                .getResultList();
        role = usrs.isEmpty() ? null : usrs.get(0);
        log.debug("find: -> " + role);
        return role;
    }

    @Override
    public void delete(Role id) {
        log.debug("delete: <- " + id);
        int cnt = em.createQuery("delete from Role where id = :id")
                .setParameter("id", id)
                .executeUpdate();
        String status = (cnt == 1) ? "deleted successfully" : "not found";
        log.debug("delete: -> Role with id=" + id + " " + status);
    }

    @Override
    public Role update(long id, Role role) {
        log.debug("update: <- " + role);
        Role r = em.merge(role);
        log.debug("update: -> " + r);
        return r;
    }
}

