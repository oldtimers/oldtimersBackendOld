package pl.pazurkiewicz.oldtimers_rally.error;

public class InvalidConfigurationProperties extends RuntimeException {
    public InvalidConfigurationProperties(String message){
        super(message);
    }
}
