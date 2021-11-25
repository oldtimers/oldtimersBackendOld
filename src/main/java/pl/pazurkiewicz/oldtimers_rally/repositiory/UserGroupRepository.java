package pl.pazurkiewicz.oldtimers_rally.repositiory;

import org.jetbrains.annotations.NotNull;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.pazurkiewicz.oldtimers_rally.model.Event;
import pl.pazurkiewicz.oldtimers_rally.model.User;
import pl.pazurkiewicz.oldtimers_rally.model.UserGroup;
import pl.pazurkiewicz.oldtimers_rally.model.UserGroupEnum;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public interface UserGroupRepository extends JpaRepository<UserGroup, Integer> {
    @Cacheable("userPrivileges")
    Set<UserGroup> getByUser_Id(@Param("id") Integer id);

    @Override
    @CacheEvict(value = "userPrivileges", key = "#entity.user.id")
    <S extends UserGroup> @NotNull S saveAndFlush(@NotNull S entity);

    @Query("from UserGroup g join fetch g.user  where g.event = :event and g.selectedGroup <> '" + UserGroupEnum.Constants.OWNER_VALUE + "'")
    List<UserGroup> getAllByEventExceptAdmin(Event event);

    Boolean existsByEventAndUserAndSelectedGroupIn(Event event, User user, Set<UserGroupEnum> userGroupEnum);

    default Boolean cannotBeModified(Event event, User user) {
        return existsByEventAndUserAndSelectedGroupIn(event, user, new HashSet<>(Arrays.asList(UserGroupEnum.ROLE_OWNER, UserGroupEnum.ROLE_ADMIN)));
    }
}
