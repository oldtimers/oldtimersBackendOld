package pl.pazurkiewicz.oldtimers_rally.controller.privileges;

import org.springframework.cache.CacheManager;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.pazurkiewicz.oldtimers_rally.MyConfigurationProperties;
import pl.pazurkiewicz.oldtimers_rally.model.Event;
import pl.pazurkiewicz.oldtimers_rally.model.UserGroupEnum;
import pl.pazurkiewicz.oldtimers_rally.model.web.EventPrivilegesModel;
import pl.pazurkiewicz.oldtimers_rally.repositiory.EventRepository;
import pl.pazurkiewicz.oldtimers_rally.repositiory.UserGroupRepository;
import pl.pazurkiewicz.oldtimers_rally.service.UserGroupService;

import javax.validation.Valid;

@Controller
@RequestMapping("/{url:" + MyConfigurationProperties.eventRegex + "}/edit/privileges")
@SessionAttributes({"privileges"})
public class EditPrivilegesController {
    private final UserGroupRepository userGroupRepository;
    private final UserGroupService userGroupService;
    private final EventRepository eventRepository;

    public EditPrivilegesController(UserGroupRepository userGroupRepository, UserGroupService userGroupService, EventRepository eventRepository, CacheManager cacheManager) {
        this.userGroupRepository = userGroupRepository;
        this.userGroupService = userGroupService;
        this.eventRepository = eventRepository;
    }

    @GetMapping
    @PreAuthorize("hasPermission(#event,'" + UserGroupEnum.Constants.OWNER_VALUE + "')")
    String getEventPrivileges(Model model, Event event) {
        model.addAttribute("privileges", new EventPrivilegesModel(userGroupRepository.getAllByEventExceptAdmin(event), event));
        return "privileges/privileges";
    }

    @PostMapping
    @PreAuthorize("hasPermission(#event,'" + UserGroupEnum.Constants.ORGANIZER_VALUE + "')")
    String saveEventPrivileges(@ModelAttribute("privileges") EventPrivilegesModel privileges, Event event, Model model) {
        userGroupService.savePrivileges(privileges);
        return getEventPrivileges(model, event);
    }

    @PostMapping(params = "add")
    @PreAuthorize("hasPermission(#event,'" + UserGroupEnum.Constants.ORGANIZER_VALUE + "')")
    String addUserPermission(@ModelAttribute("privileges") @Valid EventPrivilegesModel privileges, BindingResult bindingResult, Event event) {
        if (!bindingResult.hasErrors() && userGroupService.addPrivilegeToList(privileges.getPrivileges(), privileges.getNewGroupEnum(), privileges.getNewUser(), event)) {
            privileges.setNewUser("");
            privileges.setNewGroupEnum(UserGroupEnum.ROLE_JUDGE);
        }
        return "privileges/privileges";
    }

    @PostMapping(params = "delete")
    @PreAuthorize("hasPermission(#event,'" + UserGroupEnum.Constants.ORGANIZER_VALUE + "')")
    String deleteUserPermission(@ModelAttribute("privileges") EventPrivilegesModel privileges, Event event, @RequestParam(value = "delete") Integer deleteId) {
        privileges.deletePrivilege(deleteId);
        return "privileges/privileges";
    }

    @PostMapping(params = "reload")
    @PreAuthorize("hasPermission(#event,'" + UserGroupEnum.Constants.ORGANIZER_VALUE + "')")
    String reloadUserPermission(Model model, Event event) {
        model.addAttribute("privileges", new EventPrivilegesModel(userGroupRepository.getAllByEventExceptAdmin(event), event));
        return "privileges/privileges";
    }

    @ModelAttribute("event")
    Event getEvent(@PathVariable("url") String url) {
        return eventRepository.getByUrl(url);
    }

    @ModelAttribute("action")
    String getAction(@PathVariable("url") String url) {
        return "/" + url + "/edit";
    }
}
