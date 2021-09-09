package pl.pazurkiewicz.oldtimers_rally.user;


import javax.persistence.*;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Access(AccessType.FIELD)
@Entity
@Table(name = "users",
        uniqueConstraints = @UniqueConstraint(name = "users_login_uindex", columnNames = {"login"})
)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "{validation.user.login}")
    private String login;

    @NotBlank(message = "{validation.user.firstName}")
    private String firstName;

    @NotBlank(message = "{validation.user.lastName}")
    private String lastName;

    @NotBlank(message = "{validation.user.password}")
    private String password;

    @NotBlank(message = "{validation.user.phone}")
    private String phone;

    @Email
    @NotBlank(message = "{validation.user.email}")
    private String email;

    @NotBlank(message = "{validation.user.acceptedReg}")
    @AssertTrue(message = "{validation.user.acceptedReg}")
    private Boolean acceptedReg;

    @NotBlank(message = "{validation.user.acceptedRodo}")
    @AssertTrue(message = "{validation.user.acceptedRodo}")
    private Boolean acceptedRodo;

    private LocalDateTime createTime;

    private LocalDateTime lastLogin;
}
