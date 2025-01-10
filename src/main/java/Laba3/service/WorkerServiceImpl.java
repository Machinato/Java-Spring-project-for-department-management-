package Laba3.service;

import Laba3.entities.Department;
import Laba3.entities.Worker;
import Laba3.repository.WorkerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkerServiceImpl implements WorkerService {
    public final WorkerRepository workerRepository;

    public WorkerServiceImpl(WorkerRepository workerRepository) {
        this.workerRepository = workerRepository;
    }

    @Override
    public List<Worker> getAllWorkers() {
        return workerRepository.findAll();
    }

    @Override
    public Worker getWorkerByKeySet(String WorkerName) {
        return workerRepository.findWorkerByNameFirst(WorkerName);
    }

    @Override
    public Worker getWorkerById(int id) {
        return workerRepository.findById(id).orElse(null);
    }

    @Override
    public Worker saveWorker(Worker Worker) {
        return workerRepository.save(Worker);
    }

    @Override
    public Worker updateWorker(Integer id, Worker worker) {
        Worker oldWorker = workerRepository.findById(id).orElse(null);
        if(oldWorker != null){
            worker.setId(id);
            return workerRepository.save(worker);
        }
        return null;
    }

    @Override
    public void deleteWorker(int id) {
        workerRepository.deleteById(id);
    }

    @Override
    public Worker getWorkerByCode(String code) {
        return workerRepository.getWorkerByCode(code);
    }

    @Override
    public List<Worker> getWorkersByDepartment(Integer departmentId) {
        return workerRepository.findByDepartmentId(departmentId);
    }
}
