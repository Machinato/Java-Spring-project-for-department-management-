package Laba3;

import Laba3.entities.Department;
import Laba3.entities.Worker;
import Laba3.entities.WorkersList;
import Laba3.service.DepartmentService;
import Laba3.service.WorkerService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
//@Transactional
public class WorkerServiceTests {
    @Autowired
    private WorkerService workerService;
    @Qualifier("departmentService")
    @Autowired
    private DepartmentService departmentService;

    @Test
    public void addToDb(){
        List<Department> departments= departmentService.getAllDepartments();
        WorkersList.getInstance(departments).forEach(worker -> {workerService.saveWorker(worker);});
    }

    @Test
    public void insertWorkers() {
        System.out.println("\n === Insert Workers into DB ===");

        // Отримуємо всі відділи з бази даних
        List<Department> departments = departmentService.getAllDepartments();
        if (departments.isEmpty()) {
            // Додаємо відділи, якщо їх ще немає
            (new DepartmentServiceTests()).insertListDepartments();
            departments = departmentService.getAllDepartments();
        }

        // Отримуємо всіх працівників
        List<Worker> workers = workerService.getAllWorkers();

        // Додаємо працівників, якщо їх менше 10
        if (workers.size() < 10) {
            for (int i = workers.size(); i < 10; i++) {
                // Створюємо нового працівника
                Worker worker = new Worker(
                        "00" + i, // code
                        "Worker" + i, // Ім'я
                        "LastName" + i, // Прізвище
                        LocalDate.now().minusYears(i + 1), // startWork
                        departments.get(i % departments.size()) // Призначаємо випадковий відділ
                );

                // Перевіряємо, чи такого працівника ще немає в базі даних
                Worker workerInDB = workerService.getWorkerByCode(worker.getCode());
                if (workerInDB == null) {
                    // Додаємо працівника до бази даних
                    workerService.saveWorker(worker);
                    System.out.println("Worker successfully inserted: " + worker);
                } else {
                    System.out.println("Worker with such email already exists: " + workerInDB);
                }
            }
        }

        // Показуємо всіх працівників після вставки
        System.out.println("\n\nAFTER INSERT COLLECTION:");
        workerService.getAllWorkers().forEach(System.out::println);
    }


    @Test
    public void AllWorkers() {
        List<Worker> workers = workerService.getAllWorkers();
        Assertions.assertNotNull(workers);
        System.out.println("\n === All Workers into DB ===");
        workers.forEach(System.out::println);
    }

    @Test
    public void WorkerById() {
        Worker worker = workerService.getWorkerById(1);
        Assertions.assertNotNull(worker);
        System.out.println(worker);
    }

    @Test
    public void updateWorker() {
        Worker worker = new Worker("185", "First", "Second", LocalDate.now().minusYears(2), departmentService.getDepartmentById(1));

        System.out.println("worker" + worker);

        Assertions.assertNotNull(worker, "Worker with ID 1 should exist");

        String newLastName = "UpdatedLastName";
        worker.setNameSecond(newLastName);

        workerService.saveWorker(worker);
        System.out.println("Workers after save new worker:\n" + workerService.getAllWorkers());

        Worker updatedWorker = workerService.getWorkerByCode("185");
        Assertions.assertEquals(newLastName, updatedWorker.getNameSecond(), "Worker's last name should be updated");
    }

    @Test
    public void deleteWorker() {
        Worker worker = new Worker(
                "999", "TempWorker", "TempLastName", LocalDate.now().minusYears(1),
                departmentService.getAllDepartments().get(0)
        );
        workerService.saveWorker(worker);

        Worker savedWorker = workerService.getWorkerByCode("999");
        Assertions.assertNotNull(savedWorker, "Worker should be saved to the database");

        workerService.deleteWorker(savedWorker.getId());

        Worker deletedWorker = workerService.getWorkerById(savedWorker.getId());
        Assertions.assertNull(deletedWorker, "Worker should be deleted from the database");
    }

    @Test
    public void insertWorkerWithoutDepartment() {
        Worker worker = new Worker(
                "123", "NoDepartmentWorker", "LastName", LocalDate.now(),
                null // No department assigned
        );

        Assertions.assertThrows(Exception.class, () -> workerService.saveWorker(worker),
                "Worker without department should not be saved");
    }

    @Test
    public void getWorkersByDepartment() {
//        List<Department> departments = departmentService.getAllDepartments();
//        if (departments.isEmpty()) {
//            // Додаємо відділи, якщо їх ще немає
//            (new DepartmentServiceTests()).insertListDepartments();
//            departments = departmentService.getAllDepartments();
//        }
//        System.out.println("Departments:");
//        departments.forEach(System.out::println);
//
//        Department department = departments.get(0);

        insertWorkers();
        List<Department> departments = departmentService.getAllDepartments();
        Department department = departments.get(0);
        List<Worker> workers = workerService.getWorkersByDepartment(department.getId());

        Assertions.assertNotNull(workers, "Workers list should not be null");
        workers.forEach(worker -> Assertions.assertEquals(department.getId(), worker.getDepartment().getId(),
                "Worker's department ID should match the queried department ID"));
        System.out.println("All workers" + workers);
    }

}
