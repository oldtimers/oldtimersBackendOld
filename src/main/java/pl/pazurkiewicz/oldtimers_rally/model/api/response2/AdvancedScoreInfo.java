package pl.pazurkiewicz.oldtimers_rally.model.api.response2;

import pl.pazurkiewicz.oldtimers_rally.model.Competition;
import pl.pazurkiewicz.oldtimers_rally.model.Crew;

public interface AdvancedScoreInfo {
    Integer getId();

    Boolean isInvalidResult();

    Double getAdditional1();

    Double getAdditional2();

    Double getAdditional3();

    Double getAdditional4();

    Double getAdditional5();

    Double getResult();

    Competition getCompetition();

    Crew getCrew();
}
