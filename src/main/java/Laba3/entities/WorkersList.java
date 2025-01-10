package Laba3.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class WorkersList extends ArrayList<Worker> {
    private static WorkersList instance;

    private WorkersList(List<Department> departmentList) {
        // Створення прикладів працівників
        this.add(new Worker("001", "John", "Doe", LocalDate.of(2020, 1, 15), departmentList.get(0)));
        this.add(new Worker("002", "Jane", "Smith", LocalDate.of(2019, 5, 10), departmentList.get(1)));
        this.add(new Worker("003", "Robert", "Brown", LocalDate.of(2021, 3, 20), departmentList.get(2)));
        this.add(new Worker("004", "Alice", "Johnson", LocalDate.of(2022, 7, 25), departmentList.get(3)));
        this.add(new Worker("005", "Michael", "Davis", LocalDate.of(2020, 9, 1), departmentList.get(4)));
    }

    public static WorkersList getInstance(List<Department> departmentList) {
        if (instance == null) {
            instance = new WorkersList(departmentList);
        }
        return instance;
    }
}
