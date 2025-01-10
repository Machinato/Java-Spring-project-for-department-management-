package Laba3.repository;

import Laba3.entities.Worker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkerRepository extends JpaRepository<Worker, Integer> {
    Worker findWorkerByNameFirst(String WorkerFirstName);
    Worker getWorkerByCode(String code);
    List<Worker> findByDepartmentId(Integer departmentId);
}
