package ru.kata.spring.boot_security.demo.dao;

import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserDao {
    User save(User user);
    List<User> list(int count);
    List<User> listAll();
    User find(Long id);
    User findUserByUsername(String username);
    void delete(User user);
    void delete(Long id);
    User update(long id, User user);
}
