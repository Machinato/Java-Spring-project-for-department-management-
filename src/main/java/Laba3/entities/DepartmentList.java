package Laba3.entities;

import java.time.LocalDate;
import java.util.ArrayList;

public class DepartmentList extends ArrayList<Department> {
    private static DepartmentList instance;

    private DepartmentList() {
    }

    public static DepartmentList getInstance() {
        if (instance == null) {
            instance = new DepartmentList();
//            instance.add(new Department(0, "001", "FirstName", "FN", "emailadress123@gmail.com", "1234567890"));
            instance.add(new Department(null, "991", "Human Resources", "HR", "hr@company.com", "+38 (012) 345-67-89", true));
            instance.add(new Department(null, "881", "Information Technology", "IT", "it@company.com", "+38 (098) 765-43-21", true));
            instance.add(new Department(null, "771", "Marketing", "MK", "marketing@company.com", "+38 (056) 789-01-23", true));
            instance.add(new Department(null, "661", "Finance", "FN", "finance@company.com", "+38 (012) 312-31-23", true));
            instance.add(new Department(null, "551", "Sales", "SA", "sales@company.com", "+38 (023) 423-42-34", true));
            instance.add(new Department(null, "441", "Public Relations", "PR", "pr@company.com", "+38 (034) 534-53-45", true));
            instance.add(new Department(null, "331", "Product Development", "PD", "dev@company.com", "+38 (045) 645-64-56", true));
            instance.add(new Department(null, "221", "Administration", "AD", "admin@company.com", "+38 (056) 756-75-67", true));
            instance.add(new Department(null, "191", "Logistics", "LG", "logistics@company.com", "+38 (067) 867-86-78", true));
            instance.add(new Department(null, "101", "Research and Development", "RD", "research@company.com", "+38 (078) 978-97-89", true));
        }
        return instance;
    }
}
