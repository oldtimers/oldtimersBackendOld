package pl.pazurkiewicz.oldtimers_rally.model.api.response;

public class JwtResponse {
    private String access;
    private String type = "Bearer";
    private String refresh;
    private String username;

    public JwtResponse(String accessToken, String refreshToken, String username) {
        this.access = accessToken;
        this.refresh = refreshToken;
        this.username = username;
    }

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRefresh() {
        return refresh;
    }

    public void setRefresh(String refresh) {
        this.refresh = refresh;
    }
}
