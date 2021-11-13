package pl.pazurkiewicz.oldtimers_rally.repositiories;

import org.springframework.data.repository.query.Param;
import pl.pazurkiewicz.oldtimers_rally.model.UserGroup;

import java.util.Set;

public interface UserGroupRepository extends CustomRepository<UserGroup, Integer> {
    Set<UserGroup> getByUser_Id(@Param("id") Integer id);
}
