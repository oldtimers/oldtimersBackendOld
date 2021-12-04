package pl.pazurkiewicz.oldtimers_rally.model.web;

import pl.pazurkiewicz.oldtimers_rally.model.Event;
import pl.pazurkiewicz.oldtimers_rally.model.EventLanguage;
import pl.pazurkiewicz.oldtimers_rally.model.UserGroup;
import pl.pazurkiewicz.oldtimers_rally.model.UserGroupEnum;
import pl.pazurkiewicz.oldtimers_rally.model.comparator.EventLanguageComparator;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

public class EventPrivilegesModel implements ListWebModel<UserGroup> {
    private static final Hashtable<String, UserGroupEnum> groupEnums = new Hashtable<>() {{
        put("Organizer", UserGroupEnum.ROLE_ORGANIZER);
        put("Judge", UserGroupEnum.ROLE_JUDGE);
    }};
    private final List<UserGroup> privileges;
    private final Set<Integer> deletedPrivileges = new HashSet<>();
    private final List<EventLanguage> languages;
    @NotBlank
    private String newUser;
    @NotNull
    private UserGroupEnum newGroupEnum;

    public EventPrivilegesModel(List<UserGroup> privileges, Event event) {
        languages = event.getEventLanguages();
        languages.sort(new EventLanguageComparator());
        this.privileges = privileges;
    }

    public List<EventLanguage> getLanguages() {
        return languages;
    }

    public List<UserGroup> getAllCheckedPrivileges() {
        for (UserGroup privilege : privileges) {
            switch (privilege.getSelectedGroup()) {
                case ROLE_ADMIN:
                case ROLE_OWNER:
                    privilege.setSelectedGroup(UserGroupEnum.ROLE_ORGANIZER);
            }
        }
        return privileges;
    }

    public void deletePrivilege(Integer removeId) {
        removeFromList(removeId, privileges, deletedPrivileges);
    }

    public Set<Integer> getDeletedPrivileges() {
        return deletedPrivileges;
    }

    public List<UserGroup> getPrivileges() {
        return privileges;
    }

    public Hashtable<String, UserGroupEnum> getGroupEnums() {
        return groupEnums;
    }

    public String getNewUser() {
        return newUser;
    }

    public void setNewUser(String newUser) {
        this.newUser = newUser;
    }

    public UserGroupEnum getNewGroupEnum() {
        return newGroupEnum;
    }

    public void setNewGroupEnum(UserGroupEnum newGroupEnum) {
        this.newGroupEnum = newGroupEnum;
    }
}
