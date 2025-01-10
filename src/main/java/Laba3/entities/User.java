package Laba3.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.Check;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="username", nullable = false)
    @Check(constraints = "REGEXP_LIKE(username, " +
            "'^[a-zA-Z]([\\\\.\\\\-$]?[a-zA-Z\\\\d]+){1,3}$','c') = 1")
    private String username;

    @Column(name="password", nullable = false)
    @NotBlank(message = "Пароль не може бути порожнім")
    @Size(min = 8, message = "Пароль має бути не менше 8 символів")
    private String password;

    @Column(name="email", nullable = false)
    @NotBlank(message = "Адреса електронної пошти не може бути порожньою")
//    @Size(min = 8, message = "Пароль має бути не менше 8 символів")
    private String email;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
}
