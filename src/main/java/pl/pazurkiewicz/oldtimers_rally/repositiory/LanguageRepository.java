package pl.pazurkiewicz.oldtimers_rally.repositiory;

import org.jetbrains.annotations.NotNull;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.pazurkiewicz.oldtimers_rally.model.Language;

@Repository
public interface LanguageRepository extends JpaRepository<Language, Integer> {

    @Override
    @NotNull
    @Cacheable("languages")
    Language getById(@NotNull Integer integer);

    Language getLanguageByCode(String code);
}
