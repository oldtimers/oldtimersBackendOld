package pl.pazurkiewicz.oldtimers_rally.errors;

import org.springframework.boot.autoconfigure.web.ErrorProperties;

import java.lang.reflect.UndeclaredThrowableException;

public class InvalidConfigurationProperties extends RuntimeException {
    public InvalidConfigurationProperties(String message){
        super(message);
    }
}
