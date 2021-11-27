package pl.pazurkiewicz.oldtimers_rally.controller.crew;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.SmartValidator;
import org.springframework.web.bind.annotation.*;
import pl.pazurkiewicz.oldtimers_rally.model.Crew;
import pl.pazurkiewicz.oldtimers_rally.model.Event;
import pl.pazurkiewicz.oldtimers_rally.model.UserGroupEnum;
import pl.pazurkiewicz.oldtimers_rally.model.comparator.EventLanguageComparator;
import pl.pazurkiewicz.oldtimers_rally.model.web.CrewsModel;
import pl.pazurkiewicz.oldtimers_rally.repositiory.CrewRepository;
import pl.pazurkiewicz.oldtimers_rally.repositiory.EventRepository;
import pl.pazurkiewicz.oldtimers_rally.service.CrewService;

import javax.validation.Valid;

@Controller
@RequestMapping("/{url}/edit/crews")
@SessionAttributes({"crews"})
public class EditCrewsController {
    private final CrewRepository crewRepository;
    private final SmartValidator validator;
    private final CrewService crewService;
    private final EventRepository eventRepository;

    public EditCrewsController(EventRepository eventRepository, CrewRepository crewRepository, SmartValidator validator, CrewService crewService) {
        this.crewRepository = crewRepository;
        this.validator = validator;
        this.crewService = crewService;
        this.eventRepository = eventRepository;
    }

    @GetMapping
    @PreAuthorize("hasPermission(#event,'" + UserGroupEnum.Constants.ORGANIZER_VALUE + "')")
    String getCrews(Model model, Event event) {
        event.getEventLanguages().sort(new EventLanguageComparator());
        model.addAttribute("crews", new CrewsModel(crewRepository.getSortedByEventId(event.getId()), event));
        return "event/crews";
    }

    @PostMapping(params = "reload")
    @PreAuthorize("hasPermission(#event,'" + UserGroupEnum.Constants.ORGANIZER_VALUE + "')")
    String resetCrews(Model model, Event event) {
        return getCrews(model, event);
    }

    @PostMapping(params = "delete")
    @PreAuthorize("hasPermission(#event,'" + UserGroupEnum.Constants.ORGANIZER_VALUE + "')")
    String deleteCrew(@ModelAttribute("crews") CrewsModel crews, Event event, @RequestParam(value = "delete") Integer deleteId) {
        crews.removeCrew(deleteId);
        return "event/crews";
    }

    @PostMapping(params = "add")
    @PreAuthorize("hasPermission(#event,'" + UserGroupEnum.Constants.ORGANIZER_VALUE + "')")
    String addCrew(@ModelAttribute("crews") @Valid CrewsModel crews, BindingResult bindingResult, Event event) {
//                  ) throws IOException {
//        if (!multipartFile.isEmpty()) {
//            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
//            crewsWrapper.getNewCrew().setPhoto(fileName);
//            String uploadDir = resourceLocation + "/" + event.getId() + "/1";
//            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
//        }

        if (bindingResult.hasErrors()) {
            return "event/crews";
        }
        crews.acceptNewCrew(event);
        return "event/crews";
    }

    @PostMapping
    @PreAuthorize("hasPermission(#event,'" + UserGroupEnum.Constants.ORGANIZER_VALUE + "')")
    @Transactional
    String saveCrews(@ModelAttribute("crews") CrewsModel crews, BindingResult bindingResult, Event event) {
        Crew newCrew = crews.getNewCrew();
        crews.setNewCrew(null);
        validator.validate(crews, bindingResult);
        if (bindingResult.hasErrors()) {
            crews.setNewCrew(newCrew);
            return "event/crews";
        }
        crewService.saveCrewsModel(crews);
        return "event/crews";
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
