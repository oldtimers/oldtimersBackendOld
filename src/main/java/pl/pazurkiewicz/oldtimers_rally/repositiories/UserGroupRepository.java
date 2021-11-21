package pl.pazurkiewicz.oldtimers_rally.repositiories;

import org.jetbrains.annotations.NotNull;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.pazurkiewicz.oldtimers_rally.model.UserGroup;

import java.util.Set;

@Repository
public interface UserGroupRepository extends JpaRepository<UserGroup, Integer> {
    @Cacheable("userPrivileges")
    Set<UserGroup> getByUser_Id(@Param("id") Integer id);

    @Override
    @CacheEvict(value = "userPrivileges", key = "#entity.user.id")
    <S extends UserGroup> @NotNull S saveAndFlush(@NotNull S entity);
}
