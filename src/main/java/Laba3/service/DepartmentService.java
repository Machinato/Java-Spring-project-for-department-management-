package Laba3.service;

import Laba3.entities.Department;
import org.springframework.stereotype.Service;

import java.util.List;

public interface DepartmentService {
    List<Department> getAllDepartments();

    Department getDepartmentByKeySet(String departmentName);

    Department getDepartmentById(int id);

    Department saveDepartment(Department department);

    Department updateDepartment(Integer id, Department department);

    boolean existsByNameDep(String nameDep);

    boolean existsByCodeDep(String codeDep);

    void deleteDepartment(int id);

    public void deactivateDepartment(Integer id);
}
