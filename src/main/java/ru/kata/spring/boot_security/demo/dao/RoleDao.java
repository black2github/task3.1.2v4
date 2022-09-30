package ru.kata.spring.boot_security.demo.dao;

import ru.kata.spring.boot_security.demo.model.Role;

public interface RoleDao {
    Role save(Role user);
    Role find(Long id);
    void delete(Role id);
    Role update(long id, Role user);
}
