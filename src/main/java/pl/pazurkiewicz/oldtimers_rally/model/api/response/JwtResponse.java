package pl.pazurkiewicz.oldtimers_rally.model.api.response;

import java.time.LocalDateTime;

public class JwtResponse {
    private String access;
    private String type = "Bearer";
    private String refresh;
    private String username;

    private Integer userId;

    private LocalDateTime expirationDate;

    public JwtResponse(String accessToken, String refreshToken, String username, Integer userId, LocalDateTime expirationDate) {
        this.access = accessToken;
        this.refresh = refreshToken;
        this.username = username;
        this.expirationDate = expirationDate;
        this.userId = userId;
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

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
