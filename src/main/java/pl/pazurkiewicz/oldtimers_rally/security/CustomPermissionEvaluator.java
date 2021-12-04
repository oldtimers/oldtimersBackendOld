package pl.pazurkiewicz.oldtimers_rally.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import pl.pazurkiewicz.oldtimers_rally.model.Event;
import pl.pazurkiewicz.oldtimers_rally.model.User;
import pl.pazurkiewicz.oldtimers_rally.model.UserGroup;
import pl.pazurkiewicz.oldtimers_rally.model.UserGroupEnum;
import pl.pazurkiewicz.oldtimers_rally.repositiory.EventRepository;
import pl.pazurkiewicz.oldtimers_rally.repositiory.UserGroupRepository;
import pl.pazurkiewicz.oldtimers_rally.security.service.UserDetailsImpl;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Component
public class CustomPermissionEvaluator implements PermissionEvaluator {
    private static final Logger logger = LoggerFactory.getLogger(CustomPermissionEvaluator.class);

    private final EventRepository eventRepository;
    private final UserGroupRepository userGroupRepository;

    public CustomPermissionEvaluator(EventRepository eventRepository, UserGroupRepository userGroupRepository) {
        this.eventRepository = eventRepository;
        this.userGroupRepository = userGroupRepository;
    }

    private static void addPermission(Set<String> permissions, Integer eventId, String permission) {
        permissions.add(permission);
        if (eventId != null) {
            permissions.add(generateEventPermission(eventId, permission));
        }
    }

    public static String generateEventPermission(Integer eventId, String permission) {
        return permission + "__" + eventId;
    }

    public static String generateEventPermission(UserGroup userGroup) {
        if (userGroup.getEvent() == null) {
            return userGroup.getSelectedGroup().toString();
        } else {
            return generateEventPermission(userGroup.getEvent().getId(), userGroup.getSelectedGroup().toString());
        }
    }


    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        if ((authentication == null) || !(permission instanceof String)) {
            return false;
        } else if (targetDomainObject == null) {
            throw new ResourceNotFoundException();
        }
        Integer eventId;
        if (targetDomainObject instanceof Event) {
            eventId = ((Event) targetDomainObject).getId();
        } else {
            logger.error("Unknown object instance, impossible to check permissions");
            return false;
        }
        return hasPrivilege(authentication, eventId, permission.toString().toUpperCase());
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable target, String targetType, Object permission) {
        if ((authentication == null) || (targetType == null) || !(permission instanceof String)) {
            return false;
        }
        if (!targetType.equals(Event.class.getSimpleName())) {
            logger.error("Unknown class, impossible to check permissions");
            return false;
        }
        Integer eventId = null;
        if (target instanceof String) {
            eventId = eventRepository.getIdByUrl((String) target);

        } else if (target instanceof Integer) {
            eventId = (Integer) target;
        }
        if (target != null && eventId == null) {
            logger.info("Unknown or unreachable target: " + target);
            throw new ResourceNotFoundException();
        }
        return hasPrivilege(authentication, eventId, permission.toString().toUpperCase());
    }

    private boolean hasPrivilege(Authentication auth, Integer eventId, String permission) {
        Set<String> possiblePermissions = new HashSet<>();
        switch (permission) {
            case UserGroupEnum.Constants.JUDGE_VALUE:
                addPermission(possiblePermissions, eventId, UserGroupEnum.Constants.JUDGE_VALUE);
            case UserGroupEnum.Constants.ORGANIZER_VALUE:
                addPermission(possiblePermissions, eventId, UserGroupEnum.Constants.ORGANIZER_VALUE);
            case UserGroupEnum.Constants.OWNER_VALUE:
                addPermission(possiblePermissions, eventId, UserGroupEnum.Constants.OWNER_VALUE);
            default:
                addPermission(possiblePermissions, eventId, UserGroupEnum.Constants.ADMIN_VALUE);
        }
        Object myUserDetails = auth.getPrincipal();
        if (!(myUserDetails instanceof UserDetailsImpl)) {
            return false;
        }
        User user = ((UserDetailsImpl) auth.getPrincipal()).getUser();
        return userGroupRepository.getByUser_Id(user.getId()).stream().map(CustomPermissionEvaluator::generateEventPermission).anyMatch(possiblePermissions::contains);
    }
}
