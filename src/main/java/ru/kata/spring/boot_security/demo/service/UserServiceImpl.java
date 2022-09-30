package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kata.spring.boot_security.demo.dao.UserDao;
import ru.kata.spring.boot_security.demo.model.User;

@Service("userService")
@Repository
@Transactional
public class UserServiceImpl implements UserService, UserDetailsService {
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private UserDao userDao;

    @Autowired
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public User create(User user) {
        log.debug("create: <- " + user);
        return userDao.save(user);
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> list(int count) {
        log.debug("list: <- count = " + count);
        return userDao.listAll().stream().limit(count).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> listAll() {
        log.debug("listAll: <-");
        List<User> list = userDao.listAll();
        log.debug("listAll: -> " + Arrays.toString(list.toArray()));
        return list;
    }

    @Transactional(readOnly = true)
    @Override
    public User find(Long id) {
        log.debug("find: <- id=" + id);
        User user = userDao.find(id);
        if (user == null) {
            log.warn("find: User with id=" + id + " not found");
        }
        return user;
    }

    @Override
    public void delete(User user) {
        log.debug("delete: <- " + user);
        userDao.delete(user);
    }

    @Override
    public void delete(Long id) {
        log.debug("delete: <- id=" + id);
        User usr = find(id);
        if (usr != null) {
            delete(usr);
        } else {
            log.warn("delete: User with id=" + id + " not found");
        }
    }

    @Override
    public User update(long id, User user) {
        log.debug(String.format("update: <- id=%d, user=%s", id, user));

        User u = userDao.find(id);
        if (u == null) {
            log.warn("update: User with id=" + id + " not found");
            return null;
        }
        if (user != null) {
            u.setUsername(user.getUsername());
            u.setPassword(user.getPassword());
            u.setAge(user.getAge());
            u.setRoles(user.getRoles());
            u = userDao.save(u);
        }

        log.debug("update: -> " + u);
        return u;
    }

    @Override
    public User findUserByUsername(String username) {
        log.debug("findUserByUsername: <- " + username);
        User user = userDao.findUserByUsername(username);
        log.debug("findUserByUsername: -> " + user);
        return user;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug(String.format("loadUserByUsername: <- username='%s'", username));
        User user = userDao.findUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User with username='" + username + "' not found.");
        }
        log.debug("loadUserByUsername: -> " + user);
        return user;
    }
}
