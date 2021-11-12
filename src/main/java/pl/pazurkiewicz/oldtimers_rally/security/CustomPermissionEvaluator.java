package pl.pazurkiewicz.oldtimers_rally.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import pl.pazurkiewicz.oldtimers_rally.model.Event;
import pl.pazurkiewicz.oldtimers_rally.model.UserGroupEnum;
import pl.pazurkiewicz.oldtimers_rally.repositiories.EventRepository;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class CustomPermissionEvaluator implements PermissionEvaluator {
    private static final Logger logger = LoggerFactory.getLogger(CustomPermissionEvaluator.class);

    private final EventRepository eventRepository;

    public CustomPermissionEvaluator(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
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

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        if ((authentication == null) || (targetDomainObject == null) || !(permission instanceof String)) {
            return false;
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
            logger.error("Unknown or unreachable target: " + target);
        }
        return hasPrivilege(authentication, eventId, permission.toString().toUpperCase());
    }

    private boolean hasPrivilege(Authentication auth, Integer eventId, String permission) {
        Set<String> possible_permissions = new HashSet<>();

        switch (permission) {
            case UserGroupEnum.Constants.JUDGE_VALUE:
                addPermission(possible_permissions, eventId, UserGroupEnum.Constants.JUDGE_VALUE);
            case UserGroupEnum.Constants.ORGANIZER_VALUE:
                addPermission(possible_permissions, eventId, UserGroupEnum.Constants.ORGANIZER_VALUE);
            case UserGroupEnum.Constants.OWNER_VALUE:
                addPermission(possible_permissions, eventId, UserGroupEnum.Constants.OWNER_VALUE);
            default:
                addPermission(possible_permissions, eventId, UserGroupEnum.Constants.ADMIN_VALUE);
        }
        return auth.getAuthorities().stream().anyMatch(authority -> possible_permissions.contains(authority.getAuthority()));
    }
}
