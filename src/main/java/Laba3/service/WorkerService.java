package Laba3.service;

import Laba3.entities.Department;
import Laba3.entities.Worker;
import org.springframework.stereotype.Service;

import java.util.List;

public interface WorkerService {
    List<Worker> getAllWorkers();

    Worker getWorkerByKeySet(String WorkerName);

    Worker getWorkerById(int id);

    Worker saveWorker(Worker Worker);

    Worker updateWorker(Integer id, Worker Worker);

    void deleteWorker(int id);

    Worker getWorkerByCode(String code);

    public List<Worker> getWorkersByDepartment(Integer departmentId);
}
