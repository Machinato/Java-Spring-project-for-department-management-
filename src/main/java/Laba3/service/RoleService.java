package Laba3.service;

import Laba3.entities.Role;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RoleService {
    Role getRoleByName(String roleName);
    Role getRoleById(int roleId);
    List<Role> getAllRoles();
    void saveRole(Role role);
}
