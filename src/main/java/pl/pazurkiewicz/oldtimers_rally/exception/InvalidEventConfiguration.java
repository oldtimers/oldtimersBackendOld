package pl.pazurkiewicz.oldtimers_rally.exception;

public class InvalidEventConfiguration extends RuntimeException {
    public InvalidEventConfiguration(String message) {
        super(message);
    }
}
