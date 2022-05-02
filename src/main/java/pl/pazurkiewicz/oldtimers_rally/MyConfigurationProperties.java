package pl.pazurkiewicz.oldtimers_rally;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("custom")
public class MyConfigurationProperties {
    public static final String eventRegex = "^(?!rally$)(?!api$)(?!login$)(?!register$)(?!logout$).*$";
    private String defaultLanguage = "en";
    private String resourceLocation = "photos";
    private String jwtSecret = "secret";
    private Long jwtExpiration = 60000L;
    private Long jwtRefreshExpiration = 86400000L;
    private String realUrl = "http://localhost:8080/";
    private Boolean duplicateScores = false;

    public Boolean getDuplicateScores() {
        return duplicateScores;
    }

    public void setDuplicateScores(Boolean duplicateScores) {
        this.duplicateScores = duplicateScores;
    }

    public String getDefaultLanguage() {
        return defaultLanguage;
    }

    public void setDefaultLanguage(String defaultLanguage) {
        this.defaultLanguage = defaultLanguage;
    }

    public String getResourceLocation() {
        return resourceLocation;
    }

    public void setResourceLocation(String resourceLocation) {
        this.resourceLocation = resourceLocation;
    }

    public String getJwtSecret() {
        return jwtSecret;
    }

    public void setJwtSecret(String jwtSecret) {
        this.jwtSecret = jwtSecret;
    }

    public Long getJwtExpiration() {
        return jwtExpiration;
    }

    public void setJwtExpiration(Long jwtExpiration) {
        this.jwtExpiration = jwtExpiration;
    }

    public Long getJwtRefreshExpiration() {
        return jwtRefreshExpiration;
    }

    public void setJwtRefreshExpiration(Long jwtRefreshExpiration) {
        this.jwtRefreshExpiration = jwtRefreshExpiration;
    }

    public String getRealUrl() {
        return realUrl;
    }

    public void setRealUrl(String realUrl) {
        this.realUrl = realUrl;
    }
}
