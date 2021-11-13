package pl.pazurkiewicz.oldtimers_rally.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.pazurkiewicz.oldtimers_rally.model.Event;
import pl.pazurkiewicz.oldtimers_rally.model.User;
import pl.pazurkiewicz.oldtimers_rally.model.UserGroup;
import pl.pazurkiewicz.oldtimers_rally.model.UserGroupEnum;
import pl.pazurkiewicz.oldtimers_rally.repositiories.UserGroupRepository;
import pl.pazurkiewicz.oldtimers_rally.security.MyUserDetails;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class SecurityService {
    @Autowired
    private UserGroupRepository userGroupRepository;

    public void addOwnerPrivileges(Event event, MyUserDetails userDetails) {
        User user = userDetails.getUser();
        UserGroup saved = userGroupRepository.saveAndFlush(new UserGroup(event, user, UserGroupEnum.ROLE_OWNER));
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        List<GrantedAuthority> updatedAuthorities = new ArrayList<>(auth.getAuthorities());
//        updatedAuthorities.add(new SimpleGrantedAuthority(CustomPermissionEvaluator.generateEventPermission(event.getId(),saved.getSelectedGroup().toString())));
//        Authentication newAuth = new UsernamePasswordAuthenticationToken(auth.getPrincipal(), auth.getCredentials(), updatedAuthorities);
//        SecurityContextHolder.getContext().setAuthentication(newAuth);
//        userDetails.getUser().setUserGroups(userRepository.getById(userDetails.getUser().getId()).getUserGroups());
        user.setUserGroups(userGroupRepository.getByUser_Id(user.getId()));
    }
}
