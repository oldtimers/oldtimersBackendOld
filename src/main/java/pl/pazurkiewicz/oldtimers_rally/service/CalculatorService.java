package pl.pazurkiewicz.oldtimers_rally.service;

import java.util.HashMap;

public class CalculatorService {
    public static final HashMap<Integer, String> variableMapping = new HashMap<>() {{
        put(0, "a");
        put(1, "b");
        put(2, "c");
        put(3, "d");
        put(4, "e");
    }};
}
