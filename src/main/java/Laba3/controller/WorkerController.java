package Laba3.controller;

import Laba3.entities.Department;
import Laba3.entities.Worker;
import Laba3.service.DepartmentService;
import Laba3.service.DepartmentServiceImpl;
import Laba3.service.WorkerService;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Controller
public class WorkerController {
    private final WorkerService workerService;
    private final DepartmentServiceImpl departmentService;

    public WorkerController(WorkerService workerService, DepartmentServiceImpl departmentService) {
        this.workerService = workerService;
        this.departmentService = departmentService;
    }

    @GetMapping("/workers")
    public String getAllWorkers(Model model){
        List<Worker> allWorkers = workerService.getAllWorkers();
        List<Department> allDepartments = departmentService.getAllDepartments();
//        Department department = departmentService.getDepartmentById(id_dep);

        System.out.println();
//        model.addAttribute("id_dep", id_dep);
        model.addAttribute("departments", allDepartments);
        model.addAttribute("workers", allWorkers);
//        model.addAttribute("department", department);

        return "workers/workers";
    }

    @GetMapping("/workers/{id_dep}")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String getAllWorkersInTheDep(@PathVariable int id_dep, Model model){
        List<Worker> workersByDepartment = workerService.getWorkersByDepartment(id_dep);
        Department department = departmentService.getDepartmentById(id_dep);

        System.out.println();
//        model.addAttribute("id_dep", id_dep);
        model.addAttribute("workers", workersByDepartment);
        model.addAttribute("department", department);

        return "workers/workers";
    }

    @GetMapping("/worker/new")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String showNewWorkerForm(Model model) {
        List<Department> departments = departmentService.getAllDepartments();
        model.addAttribute("departments", departments);
//        model.addAttribute("worker", new Worker());
        model.addAttribute("errMessage", "");
        return "workers/worker";
    }

    // Додати нового працівника
    @PostMapping("/worker/new")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String addWorker(@ModelAttribute Worker worker, Model model) {
        worker.setStartWork(LocalDate.now());
        System.out.println("Worker to add" + worker);
        try{
            System.out.println("worker start date" + worker.getStartWork() + "\nWorker end date" + worker.getEndWork());

            if (worker.getDepartment().isActive() != true) {
                throw new Exception("This department is not active");
            }
            else if (worker.getEndWork() != null) {
                LocalDate startWork = worker.getStartWork();
                LocalDate endWork = worker.getEndWork();

                if (startWork.plusMonths(4).isAfter(endWork)) {
                    throw new Exception("The end date of the job cannot be less than the start date, 4 months in advance");
                }
            }
            workerService.saveWorker(worker);
        }
        catch (Exception e){
            if(e.getMessage().equals("This department is not active" ) || e.getMessage().equals("The end date of the job cannot be less than the start date, 4 months in advance")){
                model.addAttribute("departments", departmentService.getAllDepartments());
                model.addAttribute("errMessage", e.getMessage());
                return "workers/worker";
            }
            System.out.println(e.getMessage() + e.getClass());
            String errMessage = determineErrorMessage(e);
            System.out.println(errMessage);
            model.addAttribute("departments", departmentService.getAllDepartments());
            model.addAttribute("errMessage", errMessage);
            return "workers/worker";
        }

        return "redirect:/workers";
    }

    // Отримати форму для редагування існуючого працівника
    @GetMapping("/worker/edit/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String showEditWorkerForm(@PathVariable("id") int id, Model model) {
        Worker worker = workerService.getWorkerById(id);
        List<Department> departments = departmentService.getAllDepartments();
        System.out.println("Worker for edit" + worker);
        model.addAttribute("departments", departments);
        model.addAttribute("worker", worker);
        model.addAttribute("errMessage", "");
        return "workers/worker";
    }

    // Оновити існуючого працівника
    @PostMapping("/worker/update/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String updateWorker(@PathVariable("id") int id, @ModelAttribute Worker worker, Model model) {
        System.out.println("Worker:" + worker);
        System.out.println("id:" + id);
        worker.setId(id);
        try {
            System.out.println("worker start date" + worker.getStartWork() + "\nWorker end date" + worker.getEndWork());

            if (worker.getDepartment().isActive() != true) {
                System.out.println("worker.getDepartment().isActive() != true");
                throw new Exception("This department is not active");
            }
            else if (worker.getEndWork() != null) {
                System.out.println("We here!");
                LocalDate startWork = worker.getStartWork();
                LocalDate endWork = worker.getEndWork();

                System.out.println("Start work + 4 month: " + startWork.plusMonths(4));
                System.out.println("endWork " + endWork);
                System.out.println("End work > start work: " + startWork.plusMonths(4).isBefore(endWork));
                if (startWork.plusMonths(4).isAfter(endWork)) {
                    throw new Exception("The end date of the job cannot be less than the start date, 4 months in advance");
                }
            }
            workerService.updateWorker(id, worker);
        }
        catch (Exception e){
            if(e.getMessage().equals("This department is not active" ) || e.getMessage().equals("The end date of the job cannot be less than the start date, 4 months in advance")){
                model.addAttribute("departments", departmentService.getAllDepartments());
                model.addAttribute("errMessage", e.getMessage());
                return "workers/worker";
            }
            String errMessage = determineErrorMessage(e);
            System.out.println(errMessage);
            model.addAttribute("departments", departmentService.getAllDepartments());
            model.addAttribute("errMessage", errMessage);
            return "workers/worker";
        }
        return "redirect:/workers";
    }

    @GetMapping("/workers/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deleteWorker(@PathVariable int id) {
        Worker worker = workerService.getWorkerById(id);
        int departmentId = worker.getDepartment().getId();
        workerService.deleteWorker(id);
        return "redirect:/workers/" + departmentId;  // Після видалення повертаємось до списку працівників
    }

    private String determineErrorMessage(Exception e) {
        if (e instanceof ConstraintViolationException) {
            String message = e.getCause() != null ? e.getCause().getMessage() : e.getMessage(); // Отримуємо повідомлення бази даних

            if (message.contains("code")) {
                return "Worker code must be unique and exactly 3 characters long.";
            } else if (message.contains("nameFirst")) {
                return "First name must be between 2 and 50 characters and cannot be empty.";
            } else if (message.contains("nameSecond")) {
                return "Last name must be between 2 and 50 characters.";
            } else if (message.contains("startWork")) {
                return "Start work date must be a valid past or present date.";
            } else if (message.contains("endWork")) {
                return "End work date must be at least 4 months after the start work date.";
            } else if (message.contains("department")) {
                return "Worker must belong to a valid department.";
            }
        }
        return "An unexpected error occurred.";
    }

}
