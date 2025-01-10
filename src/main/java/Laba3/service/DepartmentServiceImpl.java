package Laba3.service;

import Laba3.entities.Department;
import Laba3.entities.Worker;
import Laba3.repository.DepartmentRepository;
import Laba3.repository.WorkerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("departmentService")
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final WorkerRepository workerRepository;

    public DepartmentServiceImpl(WorkerRepository workerRepository, DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
        this.workerRepository = workerRepository;
    }

    @Override
    public List<Department> getAllDepartments(){return departmentRepository.findAll();}

    @Override
    public Department getDepartmentByKeySet(String departmentName) {
        return departmentRepository.getDepartmentByNameDep(departmentName);
    }

    @Override
    public Department getDepartmentById(int id) {
        return departmentRepository.findById(id).orElse(null);
    }

    @Override
    public Department saveDepartment(Department department) {
        return departmentRepository.save(department);
    }

    @Override
    public Department updateDepartment(Integer id, Department department) {
        Department oldDepartmnet = departmentRepository.findById(id).orElse(null);
        if(oldDepartmnet != null){
            department.setId(id);
            return departmentRepository.save(department);
        }
        return null;
    }

    @Override
    public boolean existsByNameDep(String nameDep) {
        return departmentRepository.existsByNameDep(nameDep);
    }

    @Override
    public boolean existsByCodeDep(String codeDep) {
        return departmentRepository.existsByCodeDep(codeDep);
    }

    @Override
    public void deleteDepartment(int id) {
        departmentRepository.deleteById(id);
    }

    @Override
    public void deactivateDepartment(Integer id) {
        Department department = departmentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Department not found"));
        department.setActive(false);
        departmentRepository.save(department);


        List<Worker> workers = workerRepository.findByDepartmentId(id);
        for (Worker worker : workers) {
            worker.setActive(false);
            workerRepository.save(worker);
        }
    }
}
