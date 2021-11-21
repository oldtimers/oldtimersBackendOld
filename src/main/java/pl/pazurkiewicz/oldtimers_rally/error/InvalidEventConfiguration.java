package pl.pazurkiewicz.oldtimers_rally.error;

public class InvalidEventConfiguration extends RuntimeException {
    public InvalidEventConfiguration(String message) {
        super(message);
    }
}
