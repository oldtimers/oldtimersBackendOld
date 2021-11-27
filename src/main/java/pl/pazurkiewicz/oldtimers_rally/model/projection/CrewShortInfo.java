package pl.pazurkiewicz.oldtimers_rally.model.projection;

import java.time.Year;

public interface CrewShortInfo {
    Integer getId();

    String getCar();

    String getDriverName();

    Integer getNumber();

    String getPhone();

    String getPhoto();

    Year getYearOfProduction();
}
