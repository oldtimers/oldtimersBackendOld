package pl.pazurkiewicz.oldtimers_rally.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.pazurkiewicz.oldtimers_rally.model.Event;
import pl.pazurkiewicz.oldtimers_rally.model.User;
import pl.pazurkiewicz.oldtimers_rally.model.UserGroup;
import pl.pazurkiewicz.oldtimers_rally.model.UserGroupEnum;
import pl.pazurkiewicz.oldtimers_rally.model.web.EventPrivilegesModel;
import pl.pazurkiewicz.oldtimers_rally.repositiory.UserGroupRepository;
import pl.pazurkiewicz.oldtimers_rally.repositiory.UserRepository;
import pl.pazurkiewicz.oldtimers_rally.security.MyUserDetails;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class UserGroupService {
    @Autowired
    private UserGroupRepository userGroupRepository;
    @Autowired
    private UserRepository userRepository;

    public void addOwnerPrivileges(Event event, MyUserDetails userDetails) {
        User user = userDetails.getUser();
        userGroupRepository.saveAndFlush(new UserGroup(event, user, UserGroupEnum.ROLE_OWNER));
        user.setUserGroups(userGroupRepository.getByUser_Id(user.getId()));
    }

    public boolean addPrivilegeToList(List<UserGroup> privileges, UserGroupEnum groupEnum, String possibleUser, Event event) {
        Optional<User> newUser = userRepository.getUserByLogin(possibleUser);
        if (newUser.isPresent() && !userGroupRepository.cannotBeModified(event, newUser.get())) {
            switch (groupEnum) {
                case ROLE_ADMIN:
                case ROLE_OWNER:
                    groupEnum = UserGroupEnum.ROLE_ORGANIZER;
            }
            if (privileges.stream().map(userGroup -> userGroup.getUser().getLogin()).noneMatch(o -> Objects.equals(o, newUser.get().getLogin()))) {
                privileges.add(new UserGroup(event, newUser.get(), groupEnum));
                return true;
            }
        }
        return false;
    }

    @Transactional
    public void savePrivileges(EventPrivilegesModel privileges) {
        userGroupRepository.deleteAllById(privileges.getDeletedPrivileges());
        userGroupRepository.flush();
        userGroupRepository.saveAllAndFlush(privileges.getAllCheckedPrivileges());
    }
}
