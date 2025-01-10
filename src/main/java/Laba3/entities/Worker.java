package Laba3.entities;

import jakarta.persistence.*;
import lombok.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.Check;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "worker")
public class Worker {
    public static final String ERR_WORKER_CODE_NOTNULL = "'code' must be entered.";
    public static final String ERR_WORKER_CODE_LENGTH = "'code' must consist of exactly 3 characters.";
    public static final String ERR_WORKER_NAME_FIRST_NOTNULL = "'nameFirst' must be entered.";
    public static final String ERR_WORKER_START_DATE_NOTNULL = "'startWork' must be entered.";
    public static final String ERR_WORKER_START_DATE_PAST = "'startWork' must be a date in the past.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "code", nullable = false, unique = true, length = 3)
    @NotNull(message = ERR_WORKER_CODE_NOTNULL)
    @Size(min = 3, max = 3, message = ERR_WORKER_CODE_LENGTH)
    @Check(constraints = "REGEXP_LIKE(code, '^[0-9]{3}$', 'c') = 1")
    private String code;

    @Column(name = "nameFirst", nullable = false, length = 50)
    @NotNull(message = ERR_WORKER_NAME_FIRST_NOTNULL)
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters.")
    @Check(constraints = "REGEXP_LIKE(nameFirst, '^[A-Za-zÀ-ÿа-яА-ЯёЁ\\s''-]*$', 'c') = 1")
//    @Pattern(regexp = "^[A-Za-zÀ-ÿа-яА-ЯёЁ\\s'-]+$", message = "First name must contain only letters, spaces, apostrophes, or hyphens.")
    private String nameFirst;

    @Column(name = "nameSecond", nullable = true, length = 50)
//    @Pattern(regexp = "^[A-Za-zÀ-ÿа-яА-ЯёЁ\\s'-]*$", message = "Last name must contain only letters, spaces, apostrophes, or hyphens.")
    @Check(constraints = "REGEXP_LIKE(nameSecond, '^[A-Za-zÀ-ÿа-яА-ЯёЁ\\s''-]*$', 'c') = 1")
    private String nameSecond;


    @Column(name = "startWork", nullable = false)
    @NotNull(message = ERR_WORKER_START_DATE_NOTNULL)
    @PastOrPresent(message = ERR_WORKER_START_DATE_PAST)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startWork;

    @Column(name = "endWork", nullable = true)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate endWork;

    @ManyToOne
    @JoinColumn(name = "department", nullable = false)
    @NotNull(message = "Worker must belong to a department.")
    private Department department;

    @Column(name = "active", nullable = false)
    private boolean active = true;

    // Кастомний конструктор
    public Worker(String code, String nameFirst, String nameSecond, LocalDate startWork, Department department) {
        this.code = code;
        this.nameFirst = nameFirst;
        this.nameSecond = nameSecond;
        this.startWork = startWork;
        this.department = department;
    }

    @Override
    public String toString(){
        return this.code + "\t\t" + this.nameFirst + "\t\t" + this.nameSecond + "\t\t\t" + this.startWork + "\t\t" + this.endWork + "\t\t" + this.department.getNameDep();
    }
}
