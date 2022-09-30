package ru.kata.spring.boot_security.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.LinkedHashSet;

@SpringBootApplication
public class SpringBootSecurityDemoApplication {
	private static final Logger log = LoggerFactory.getLogger(SpringBootSecurityDemoApplication.class);

	@Autowired
	private UserService userService;

	@Autowired
	PasswordEncoder bCryptPasswordEncoder;

	@PostConstruct
	public void init() {
		for (int i=0; i < 5; i++) {
			userService.create(new User("username"+i, "password"+i, i));
			log.debug("." + i);
		}

		Role role1 = new Role("ROLE_USER");
		Role role2 = new Role("ROLE_ADMIN");
		User user = new User("admin", bCryptPasswordEncoder.encode("admin"));
		user.setRoles(new LinkedHashSet<Role>(Arrays.asList(role1, role2)));
		user = userService.create(user);

		user = new User("user", bCryptPasswordEncoder.encode("user"));
		user.getRoles().add(new Role("ROLE_USER"));
		userService.create(user);
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringBootSecurityDemoApplication.class, args);
	}

}
