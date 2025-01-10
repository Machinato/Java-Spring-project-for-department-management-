package Laba3.service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import Laba3.entities.Department;
import Laba3.entities.Worker;
import Laba3.repository.DepartmentRepository;
import Laba3.repository.WorkerRepository;
import org.springframework.stereotype.Service;

@Service
public class ReportService {

    private final DepartmentRepository departmentRepository;
    private final WorkerRepository workerRepository;

    public ReportService(DepartmentRepository departmentRepository, WorkerRepository workerRepository) {
        this.departmentRepository = departmentRepository;
        this.workerRepository = workerRepository;
    }

    public String generateReport() throws IOException {
        List<Department> departments = departmentRepository.findAll(); // Отримання даних із БД
        List<Worker> workers = workerRepository.findAll(); // Отримання даних із БД
        String filePath = "C:/Users/mason/Desktop/Univer/5 simestr/WEB java/report.html"; // Абсолютний шлях до файлу

        try (FileWriter writer = new FileWriter(filePath)) {
            // Запис HTML-шаблону
            writer.write("<html>\n");
            writer.write("<head><title>Report</title></head>\n");
            writer.write("<body>\n");

            // Тема "Department List"
            writer.write("<h2>Department List</h2>\n");
            writer.write("<table border='1'>\n");
            writer.write("<tr><th>Name</th><th>Short Name</th><th>Code</th><th>Email</th><th>Phone Number</th></tr>\n");

            for (Department department : departments) {
                writer.write("<tr>");
                writer.write("<td>" + department.getNameDep() + "</td>");
                writer.write("<td>" + department.getNameSDep() + "</td>");
                writer.write("<td>" + department.getCodeDep() + "</td>");
                writer.write("<td>" + department.getEmailHead() + "</td>");
                writer.write("<td>" + department.getPhoneHead() + "</td>");
                writer.write("</tr>\n");
            }
            writer.write("</table>\n");

            // Тема "Worker List"
            writer.write("<h2>Worker List</h2>\n");
            writer.write("<table border='1'>\n");
            writer.write("<tr><th>Code</th><th>Name</th><th>Surname</th><th>Start Work</th><th>End Work</th><th>Department Name</th></tr>\n");

            for (Worker worker : workers) {
                writer.write("<tr>");
                writer.write("<td>" + worker.getCode() + "</td>");
                writer.write("<td>" + worker.getNameFirst() + "</td>");
                writer.write("<td>" + worker.getNameSecond() + "</td>");
                writer.write("<td>" + worker.getStartWork() + "</td>");
                writer.write("<td>" + worker.getEndWork() + "</td>");
                writer.write("<td>" + worker.getDepartment().getNameDep() + "</td>");
                writer.write("</tr>\n");
            }
            writer.write("</table>\n");

            // Закриття тегів HTML
            writer.write("</body>\n");
            writer.write("</html>\n");
        }

        return filePath; // Повертаємо шлях до файлу
    }
}
