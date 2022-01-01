package pl.pazurkiewicz.oldtimers_rally.model.api.response;

public class UserName {
    private final String username;

    public UserName(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
