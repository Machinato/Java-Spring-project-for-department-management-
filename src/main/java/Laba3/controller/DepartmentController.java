package Laba3.controller;

import Laba3.entities.Department;
import Laba3.service.DepartmentService;
import Laba3.service.UserService;
import Laba3.service.WorkerService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Date;

@Controller
public class DepartmentController {
    private final DepartmentService departmentService;
    private final WorkerService workerService;
    private final UserService userService;

    String DEPARTMENT_TEXT_EDIT = "Department edit";
    String DEPARTMENT_TEXT_INS  = "Department ins";

    public DepartmentController(DepartmentService departmentService, WorkerService workerService, UserService userService) {
        this.departmentService = departmentService;
        this.workerService = workerService;
        this.userService = userService;
    }

//    @GetMapping("/")
//    public String showStartPage(Model model) {
//        System.out.println("showStartPage");
//        model.addAttribute("now", new Date());
//        model.addAttribute("errorMessage", "Invalid username or password");
//        return "WJ24MHHstart";
//    }

    @GetMapping("/departments")
    public String getAllEntities(Model model) {
        System.out.println("getAllEntities");
        model.addAttribute("listDepartments", departmentService.getAllDepartments());
        model.addAttribute("err", departmentService.getAllDepartments());
//        model.
        return "departments/departments";  // відображає сторінку entities.html
    }

    @GetMapping("/department/new")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String createDepartmentForm(Model model) {
        System.out.println("Go to insert new department ");
        // create Employee object to hold employee form data
        Department newDepartment = new Department("");
        model.addAttribute("department", newDepartment);
        model.addAttribute("title", DEPARTMENT_TEXT_INS);
        model.addAttribute("errMessage", "");
        //== forward() in ServletAPI
        return "departments/department";
    }

    @GetMapping("/department/edit/{id_dep}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String editDepartmentForm(@PathVariable int id_dep, Model model) {
        System.out.println("Go to edit department with id=" + id_dep);
        Department department = departmentService.getDepartmentById(id_dep);
        System.out.println("department:" + department);
        model.addAttribute("department", department);
        model.addAttribute("title", DEPARTMENT_TEXT_EDIT);
        model.addAttribute("errMessage", "");
        return "departments/department";
    }

    @PostMapping("/department/save")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String updateDepartmentForm(@ModelAttribute("department") Department departmentToSave, Model model) {
        System.out.println("/employees/save");
        System.out.println("departmentToSave:" + departmentToSave);

//        Department department = departmentService.getDepartmentByKeySet(departmentToSave.getNameDep());

//        if(department == null) {
            System.out.println("Department not found");
            try {
                departmentService.saveDepartment(departmentToSave);
            }
            catch (Exception e) {
                System.out.println("DataIntegrityViolationException" + e);
                String errorMessage = determineErrorMessage(e);
                model.addAttribute("errMessage", errorMessage);
                return "/departments/department"; // Повертаємо на форму з помилкою
            }
//        }
//        else {
//            model.addAttribute("employee", departmentToSave);
//            model.addAttribute("titleEmployee", DEPARTMENT_TEXT_INS);
//            model.addAttribute("errorString", "Employee with such key (name) was found in DB!");
//            return "redirect:/department/save";
//        }

        return "redirect:/departments";
    }

    @PostMapping("/department/save/{id_dep}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String saveDepartmentForm(@PathVariable int id_dep, @ModelAttribute("department") Department departmentToSave, Model model) {
        System.out.println("/employees/save/id_dep: " + id_dep);

        System.out.println("departmentToSave:" + departmentToSave);

        try {
            departmentService.updateDepartment(id_dep, departmentToSave);
        } catch (DataIntegrityViolationException e) {
            System.out.println("DataIntegrityViolationException");
            String errorMessage = determineErrorMessage(e);
            model.addAttribute("errMessage", errorMessage);
//            model.addAttribute("department", departmentToSave);
            return "departments/department"; // Повертаємо на форму з помилкою
        }


        return "redirect:/departments";
    }

    @GetMapping("/delete/{id_dep}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deleteDepartment(@PathVariable int id_dep, Model model) {
        System.out.println("deleteDepartment");
        String messageDeleteError = "";

        Department departmentInDB = departmentService.getDepartmentById(id_dep);

        if (departmentInDB != null) {
            StringBuilder sbMessageAboutPresent = new StringBuilder();
            System.out.println("getWorkersByDepartment" + workerService.getWorkersByDepartment(id_dep));
            if(workerService.getWorkersByDepartment(id_dep).isEmpty()){
                departmentService.deleteDepartment(id_dep);
            }
            else{
                departmentService.deactivateDepartment(id_dep);
            }
//            departmentService.deactivateDepartment(id_dep);
        }
        else {
            messageDeleteError = "Object: DEPARTMENT, id=" + id_dep + "<br><br>Such department absent in DB!";
        }

        // Якщо виникла помилка, перенаправляємо на сторінку з повідомленням
        if (!messageDeleteError.isEmpty()) {
            model.addAttribute("error_crud_message", messageDeleteError);
            model.addAttribute("ret_page", "/departments");
            return "crud_error";
        } else {
            return "redirect:/departments";
        }
    }

    private String determineErrorMessage(Exception e) {
        System.out.println("" + e);
        if(e instanceof DataIntegrityViolationException) {
            String message = e.getCause().getMessage(); // Отримуємо повідомлення бази даних
            if (message.contains("nameDep")) {
                return "Department name must be unique.";
            } else if (message.contains("nameSDep")) {
                return "Department short name must be unique.";
            } else if (message.contains("codeDep")) {
                return "Department code must be unique.";
            } else if (message.contains("emailHead")) {
                return "Email address must be unique.";
            }
        }
        return e.getMessage();
    }
}
