package pl.pazurkiewicz.oldtimers_rally.model.projection;

import org.hibernate.validator.constraints.Length;
import pl.pazurkiewicz.oldtimers_rally.model.web.DefaultLanguageSelector;
import pl.pazurkiewicz.oldtimers_rally.validator.FieldsValueMatch;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@FieldsValueMatch(
        field = "newPassword",
        fieldMatch = "newPasswordConfirmation",
        message = "Passwords do not match!"
)
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
    @Length(min = 6, max = 40)
    private String newPasswordConfirmation;
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
    @NotNull
    private DefaultLanguageSelector languageSelector;

    public UserWriteModel() {
    }

    public UserWriteModel(DefaultLanguageSelector languageSelector) {
        this.languageSelector = languageSelector;
    }

    public DefaultLanguageSelector getLanguageSelector() {
        return languageSelector;
    }

    public void setLanguageSelector(DefaultLanguageSelector languageSelector) {
        this.languageSelector = languageSelector;
    }

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

    public String getNewPasswordConfirmation() {
        return newPasswordConfirmation;
    }

    public void setNewPasswordConfirmation(String newPasswordConfirmation) {
        this.newPasswordConfirmation = newPasswordConfirmation;
    }
}
