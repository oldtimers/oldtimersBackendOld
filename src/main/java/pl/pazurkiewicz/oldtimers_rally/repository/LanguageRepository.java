package pl.pazurkiewicz.oldtimers_rally.repository;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.pazurkiewicz.oldtimers_rally.model.Language;

import java.util.List;

@Repository
public interface LanguageRepository extends JpaRepository<Language, Integer> {

    @Override
    @NotNull
//    @Cacheable("languages")
    Language getById(@NotNull Integer integer);


    @Query("select l from Event e inner join e.eventLanguages as el inner join el.language as l where e.url=:url order by l.id")
    List<Language> getLanguagesByUrl(String url);

    Language getLanguageByCode(String code);
}
