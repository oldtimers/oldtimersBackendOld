package pl.pazurkiewicz.oldtimers_rally.login;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class UserWriteModel {
    @NotBlank(message = "{login.notBlank}")
    @Length(min = 3, max = 32)
    private String login;
    @NotBlank(message = "s")
    @Length(min = 3, max = 64)
    private String firstName;
    @NotBlank(message = "s")
    @Length(min = 3, max = 64)
    private String lastName;
    @NotBlank(message = "s")
    @Length(min = 6, max = 40)
    private String newPassword;
    @NotBlank(message = "s")
    @Length(min = 6, max = 12)
    private String phone;
    @NotBlank(message = "s")
    @Length(max = 64)
    private String email;
    @NotNull
    @AssertTrue(message = "t")
    private Boolean acceptedReg = false;
    @NotNull
    @AssertTrue(message = "b")
    private Boolean acceptedRodo = false;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getAcceptedReg() {
        return acceptedReg;
    }

    public void setAcceptedReg(Boolean acceptedReg) {
        this.acceptedReg = acceptedReg;
    }

    public Boolean getAcceptedRodo() {
        return acceptedRodo;
    }

    public void setAcceptedRodo(Boolean acceptedRodo) {
        this.acceptedRodo = acceptedRodo;
    }
}
