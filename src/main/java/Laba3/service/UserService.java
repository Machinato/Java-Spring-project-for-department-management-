package Laba3.service;

import Laba3.entities.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    List<User> getAllUsers();
    User getUserById(int id);
    User findUserByName(String Name);
    void saveUser(User user);
    void deleteUser(int id);
}
