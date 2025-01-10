package Laba3;

import Laba3.entities.Department;
import Laba3.entities.DepartmentList;
import Laba3.service.DepartmentService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;

import static com.jayway.jsonpath.internal.path.PathCompiler.fail;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
public class DepartmentServiceTests {
    @Autowired
    private DepartmentService departmentService;

    @Transactional
    @Test
    public void insertListDepartments(){
        List<Department> listDepartments = DepartmentList.getInstance();
        listDepartments.forEach(department -> {
            try{
                Department depInDb = departmentService.saveDepartment(department);
                if (depInDb != null) {
                    System.out.println(department.getNameDep() + " Successfully insert.\n\nEmployees list AFTER INSERT:");
                }
            }
            catch(DataIntegrityViolationException e){
                System.out.println("DataIntegrityViolationException");
                String errorMessage = determineErrorMessage(e);
                System.out.println("errorMessage: " + errorMessage);
            }
        });
    }

    @Test
    public void insertDepartmentWithWrongName(){
        Department department = new Department();
        department.setActive(true);
        department.setNameDep("111");
        department.setNameSDep("qwe");
        department.setPhoneHead("+38 (064) 323-47-40");
        department.setEmailHead("googledep12@gmail.com");
        department.setCodeDep("176");

        try{
            Department depInDb = departmentService.saveDepartment(department);
            if (depInDb != null) {
                System.out.println(department.getNameDep() + " Successfully insert.\n\nEmployees list AFTER INSERT: " + departmentService.getAllDepartments());
            }
        }
        catch(Exception e){
            System.out.println("DataIntegrityViolationException");
            if(e instanceof DataIntegrityViolationException){
                String errorMessage = determineErrorMessage((DataIntegrityViolationException) e);
                System.out.println("errorMessage: " + errorMessage);
            }
            else {
                System.out.println("Exception: " + e);
            }
        }
    }

    @Test
    public void testFindAll() {
        List<Department> departments = departmentService.getAllDepartments();
        Assertions.assertNotNull(departments);
        departments.stream().forEach(System.out::println);
    }

    @Test
    public void testSaveAndFindById() {
        Department department = new Department();
        department.setNameDep("HR");
        department.setNameSDep("HRD");
        department.setCodeDep("763");
        department.setEmailHead("hr@example.com");
        department.setPhoneHead("+38 (063) 020-10-30");

        Department saved = departmentService.saveDepartment(department);
        Department found = departmentService.getDepartmentById(saved.getId());

        Assertions.assertEquals(saved.getNameDep(), found.getNameDep());
        System.out.println("found:" + found);
    }

    @Test
    public void testDeleteById() {
        Department department = new Department();
        department.setNameDep("Finance");
        department.setNameSDep("FIN");
        department.setCodeDep("777");
        department.setEmailHead("finance@example.com");
        department.setPhoneHead("+38 (063) 020-20-30");

        Department saved = departmentService.saveDepartment(department);
        departmentService.deleteDepartment(saved.getId());
        Assertions.assertNull(departmentService.getDepartmentById(saved.getId()));
    }

//    private String determineErrorMessage(DataIntegrityViolationException e) {
//        String message = e.getRootCause().getMessage(); // Отримуємо повідомлення бази даних
//
//        if (message.contains("nameDep")) {
//            return "Department name must be unique.";
//        } else if (message.contains("nameSDep")) {
//            return "Department short name must be unique.";
//        } else if (message.contains("codeDep")) {
//            return "Department code must be unique.";
//        } else if (message.contains("emailHead")) {
//            return "Email address must be unique.";
//        }
//        else if (message.contains("phoneHead_UNIQUE")) {
//            return "phone number must be unique.";
//        }
//
//        return "An unexpected error occurred.";
//    }

    @Transactional
    @Test
    public void insertDepartmentWithInvalidCodeDep() {
        Department department = new Department();
        department.setActive(true);
        department.setNameDep("Test Department");
        department.setNameSDep("TDep");
        department.setPhoneHead("+38 (064) 323-47-40");
        department.setEmailHead("testdep@example.com");

        // Неправильне значення для codeDep, яке не відповідає шаблону
        department.setCodeDep("176");  // Це не відповідає регулярному виразу

        try {
            // Спробувати зберегти Department
            departmentService.saveDepartment(department);
            fail("Expected IllegalArgumentException due to invalid codeDep format");
        } catch (DataIntegrityViolationException e) {
            // Перевіряємо, чи це правильний виняток
            String errorMessage = determineErrorMessage(e);
            assertTrue(errorMessage.contains("Department code must match pattern"));
        }
    }

    private String determineErrorMessage(DataIntegrityViolationException e) {
        String message = e.getRootCause().getMessage(); // Отримуємо повідомлення бази даних

        if (message.contains("codeDep")) {
            return "Department code must match pattern";
        }

        return "An unexpected error occurred.";
    }

}
