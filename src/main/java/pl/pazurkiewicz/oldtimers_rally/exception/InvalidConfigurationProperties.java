package pl.pazurkiewicz.oldtimers_rally.exception;

public class InvalidConfigurationProperties extends RuntimeException {
    public InvalidConfigurationProperties(String message) {
        super(message);
    }
}
