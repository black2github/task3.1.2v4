package ru.kata.spring.boot_security.demo.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@NamedEntityGraph(name = "User.roles",
        attributeNodes = @NamedAttributeNode("roles")
)
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(name = "uc_user_username", columnNames = {"username"})
})
public class User implements UserDetails {
    private static final Logger log = LoggerFactory.getLogger(User.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Version
    private int version;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    private int age;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    private Set<Role> roles = new LinkedHashSet<>();

    public User() {
    }

    public User(String username, String password, int age) {
        this.username = username;
        this.age = age;
        this.password = password;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setPassword(String secondName) {
        this.password = secondName;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public int getAge() {
        return age;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        log.debug("getAuthorities: ->" + mapRolesToAuthorities(roles));
        return mapRolesToAuthorities(roles);
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream().map(r -> new SimpleGrantedAuthority(r.getName())).collect(Collectors.toList());
    }

    public String getPassword() {
        return password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    @Override
    public String toString() {
        return String.format("User{id=%d, username='%s', password='%s', age=%d, roles=%s}",
                id, username, password, age, Arrays.toString(roles.toArray()));
    }

    @Override
    public int hashCode() {
        return
                Objects.hash(id, username, password, age, roles);
    }

    @Override
    public boolean equals(Object o) {
        if ((o == null) || (o.getClass() != User.class)) {
            return false;
        }
        if (this == o) {
            return true;
        }
        return (username.equals(((User) o).getUsername())
                && password.equals(((User) o).getPassword())
                && age == ((User) o).getAge()
                // compare two set
                && roles.containsAll(((User) o).getRoles())
                && ((User) o).getRoles().containsAll(roles)
        );
    }
}

