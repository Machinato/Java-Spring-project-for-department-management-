package Laba3.entities;

import jakarta.persistence.*;
import lombok.*;
import jakarta.validation.constraints.*;
import java.util.Random;
import org.hibernate.annotations.Check;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "department")
public class Department {
    public static final String ERR_DEP_CODE_NOTNULL = "'code' of department must be entered.";
    public static final String ERR_DEP_CODE_BADLENGTH = "'code' of department must have exactly 3 digits.";
    public static final String ERR_DEP_NAME_NOTNULL = "'nameDep' must be entered.";
    public static final String ERR_DEP_NAME_SECOND_NOTNULL = "'nameSDep' must be entered.";
    public static final String ERR_DEP_EMAIL_NOTNULL = "'email' of department must be entered.";
    public static final String ERR_DEP_PHONE_NUMBER_NOTNULL = "'phoneHead' of department must be entered.";
    public static final String ERR_DEP_NOTEMAIL = "'email' of department must be a valid email address.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "codeDep", length = 3, nullable = false, unique = true)
    @NotNull(message = ERR_DEP_CODE_NOTNULL)
//    @Pattern(regexp = "\\d{3}", message = ERR_DEP_CODE_BADLENGTH)
        @Check(constraints = "REGEXP_LIKE(codeDep, '^[0-9]{3}$', 'c') = 1")  // Замінено на @Check для перевірки формату
    private String codeDep;

    @Column(name = "nameDep", nullable = false, length = 50)
    @NotNull(message = ERR_DEP_NAME_NOTNULL)
    @Size(min = 2, max = 50, message = "Department name must be between 2 and 50 characters.")
    @Check(constraints = "REGEXP_LIKE(nameDep, '^[A-Z][a-z]*(\\s[A-Z][a-z]*)*$', 'c') = 1")
    private String nameDep;

    @Column(name = "nameSDep", nullable = false, length = 8)
    @NotNull(message = ERR_DEP_NAME_SECOND_NOTNULL)
    @Size(min = 2, max = 8, message = "Short name must be between 2 and 8 characters.")
    @Check(constraints = "REGEXP_LIKE(nameSDep, '^[A-Z]{2,8}$', 'c') = 1")
    private String nameSDep;

    @Column(name = "emailHead", nullable = false)
    @NotNull(message = ERR_DEP_EMAIL_NOTNULL)
    @Email(message = ERR_DEP_NOTEMAIL)
    private String emailHead;

    @Column(name = "phoneHead", nullable = false)
    @NotNull(message = ERR_DEP_PHONE_NUMBER_NOTNULL)
//    @Pattern(regexp = "\\+\\d{1,3}\\s\\(\\d{1,4}\\)\\s\\d{3}-\\d{2}-\\d{2}", message = "Wrong phone number format.")
    @Check(constraints = "REGEXP_LIKE(phoneHead, '^+38\\\\s(\\\\d{3})\\\\s\\\\d{3}-\\\\d{2}-\\\\d{2}$', 'c') = 1")  // Замінено на @Check для перевірки формату
    private String phoneHead;

    @Column(name = "active", nullable = false)
    private boolean active = true;

    public Department(String name) {
        Random random = new Random();
        this.codeDep = String.format("%03d", random.nextInt(1000));
        this.id = 0;
        this.nameDep = "New Department";
        this.nameSDep = "NEW DEPARTMENT";
        this.emailHead = "emailaddress123@gmail.com";
        this.phoneHead = "+38 (012) 345-67-89";
    }

    @Override
    public String toString(){
        return this.nameDep + "\t\t\t" + this.nameSDep + "\t\t" + this.codeDep + "\t\t" + this.emailHead + "\t\t\t" + this.phoneHead;
    }
}
