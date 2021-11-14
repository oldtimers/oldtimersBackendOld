package pl.pazurkiewicz.oldtimers_rally.repositiories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.pazurkiewicz.oldtimers_rally.model.UserGroup;

import java.util.Set;

@Repository
public interface UserGroupRepository extends JpaRepository<UserGroup, Integer> {
    Set<UserGroup> getByUser_Id(@Param("id") Integer id);
}
