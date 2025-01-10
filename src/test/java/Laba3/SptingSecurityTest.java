package Laba3;

import Laba3.entities.Role;
import Laba3.entities.User;
import Laba3.service.RoleService;
import Laba3.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
class TestESpringSecurity {

    @Autowired
    RoleService roleService;

    @Autowired
    UserService userService;

    @Autowired
    ApplicationContext context;

    @Test
    void contextLoads() {
    }

    @Test
    void insertUser(){
        PasswordEncoder passwordEncoder = context.getBean(PasswordEncoder.class);
        User user = userService.findUserByName("admin");
        if (user == null) {
            user = new User();
            user.setUsername("admin");
            user.setEmail("kakahakakahovna228@gmail.com");
            user.setPassword(passwordEncoder.encode("admin"));
            user.setRole(roleService.getRoleById(1));
            System.out.println("INSERT USER:" + user);
            userService.saveUser(user);
            user = userService.findUserByName("admin");
        }
        assertNotEquals(user.getRole(),null);


        User user2 = userService.findUserByName("user");
        if (user2 == null) {
            user2 = new User();
            user2.setUsername("user");
            user2.setEmail("useremail228@gmail.com");
            user2.setPassword(passwordEncoder.encode("user"));
            user2.setRole(roleService.getRoleById(2));
            System.out.println("INSERT USER:" + user2);
            userService.saveUser(user2);
            user2 = userService.findUserByName("user");
        }
        assertNotEquals(user2.getRole(),null);
    }

    @Test
    void insertUsersRolesTest(){
        //============= add roles if its not on db
        Role adminR = roleService.getRoleByName("ROLE_ADMIN");
        System.out.println("adminR: " + adminR);
        if (adminR == null) {
            adminR = new Role(1, "ROLE_ADMIN");
            roleService.saveRole(adminR);
            adminR = roleService.getRoleByName("ROLE_ADMIN");
        }

        Role userR = roleService.getRoleByName("ROLE_USER");
        System.out.println("userR: " + userR);
        if (userR == null) {
            userR = new Role(2, "ROLE_USER");
            roleService.saveRole(userR);
            userR = roleService.getRoleByName("ROLE_USER");
        }

        assertEquals(2, roleService.getAllRoles().size());

        PasswordEncoder passwordEncoder = context.getBean(PasswordEncoder.class);

        User userAdmin = userService.findUserByName("adminDB");
        System.out.println("userAdmin" + userAdmin);
        if (userAdmin == null) {
            userAdmin = new User();
            userAdmin.setUsername("adminDB");
            userAdmin.setPassword(passwordEncoder.encode("adminAAA"));
            userAdmin.setRole(adminR);
            System.out.println("USER_ADMIN:" + userAdmin.getUsername() + userAdmin.getRole() + userAdmin.getPassword());
            userService.saveUser(userAdmin);
            userAdmin = userService.findUserByName("adminDB");
        }

        assertNotEquals(userAdmin.getRole(),null);

        User user = userService.findUserByName("testDBuser");
        if (user == null) {
            user = new User();
            user.setUsername("testDBuser");
            user.setPassword(passwordEncoder.encode("userAAA"));
            user.setRole(userR);
            System.out.println("USER:" + user);
            userService.saveUser(user);
            user = userService.findUserByName("testDBuser");
        }
        assertNotEquals(user.getRole(),null);

    }
}
