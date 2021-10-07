package pl.pazurkiewicz.oldtimers_rally.user;

import javax.persistence.*;
import java.time.Instant;
import java.util.Set;

@Table(name = "users", indexes = {
        @Index(name = "users_login_uindex", columnList = "login", unique = true)
})
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "login", nullable = false, length = 32)
    private String login;

    @Column(name = "first_name", nullable = false, length = 64)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 64)
    private String lastName;

    @Column(name = "password", nullable = false, length = 60)
    private String password;

    @Column(name = "phone", nullable = false, length = 16)
    private String phone;

    @Column(name = "email", nullable = false, length = 64)
    private String email;

    @Column(name = "accepted_reg", nullable = false)
    private Boolean acceptedReg = false;

    @Column(name = "accepted_rodo", nullable = false)
    private Boolean acceptedRodo = false;

    @Column(name = "create_time", nullable = false)
    private Instant createTime;

    @Column(name = "last_login")
    private Instant lastLogin;

    @OneToMany(mappedBy = "user")
    private Set<UserGroup> userGroups;

    public User() {
    }

    public User(UserWriteModel model) {
        login = model.getLogin();
        firstName = model.getFirstName();
        lastName = model.getLastName();
        password = model.getPassword();
        phone = model.getPhone();
        email = model.getEmail();
        acceptedReg = model.getAcceptedReg();
        acceptedRodo = model.getAcceptedRodo();
        createTime = Instant.now();
        lastLogin = Instant.now();
    }

    public Set<UserGroup> getUserGroups() {
        return userGroups;
    }

    public Instant getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Instant lastLogin) {
        this.lastLogin = lastLogin;
    }

    public Instant getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Instant createTime) {
        this.createTime = createTime;
    }

    public Boolean getAcceptedRodo() {
        return acceptedRodo;
    }

    public void setAcceptedRodo(Boolean acceptedRodo) {
        this.acceptedRodo = acceptedRodo;
    }

    public Boolean getAcceptedReg() {
        return acceptedReg;
    }

    public void setAcceptedReg(Boolean acceptedReg) {
        this.acceptedReg = acceptedReg;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
