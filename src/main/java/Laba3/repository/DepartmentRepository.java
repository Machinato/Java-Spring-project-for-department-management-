package Laba3.repository;

import Laba3.entities.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Integer> {
    Department getDepartmentByNameDep(String departmentName);
    boolean existsByNameDep(String nameDep);
    boolean existsByCodeDep(String codeDep);
}
