package pl.pazurkiewicz.oldtimers_rally.model.api.response;

public class TokenRefreshResponse {
    private String access;
    private String refresh;
    private String tokenType = "Bearer";

    public TokenRefreshResponse(String accessToken, String refreshToken) {
        this.access = accessToken;
        this.refresh = refreshToken;
    }

    public String getAccess() {
        return access;
    }

    public void setAccess(String token) {
        this.access = token;
    }

    public String getRefresh() {
        return refresh;
    }

    public void setRefresh(String refresh) {
        this.refresh = refresh;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

}
