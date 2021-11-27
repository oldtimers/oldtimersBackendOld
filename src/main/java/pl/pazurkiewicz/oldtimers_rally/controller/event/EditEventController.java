package pl.pazurkiewicz.oldtimers_rally.controller.event;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.SmartValidator;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.pazurkiewicz.oldtimers_rally.model.Competition;
import pl.pazurkiewicz.oldtimers_rally.model.Crew;
import pl.pazurkiewicz.oldtimers_rally.model.Event;
import pl.pazurkiewicz.oldtimers_rally.model.UserGroupEnum;
import pl.pazurkiewicz.oldtimers_rally.model.comparator.EventLanguageComparator;
import pl.pazurkiewicz.oldtimers_rally.model.web.CrewsWrapper;
import pl.pazurkiewicz.oldtimers_rally.repositiory.CompetitionRepository;
import pl.pazurkiewicz.oldtimers_rally.repositiory.CrewRepository;
import pl.pazurkiewicz.oldtimers_rally.repositiory.EventRepository;
import pl.pazurkiewicz.oldtimers_rally.utils.FileUploadUtil;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/{url}/edit")
@SessionAttributes({"crewsWrapper"})
public class EditEventController {
    private final EventRepository eventRepository;
    private final CrewRepository crewRepository;
    private final CompetitionRepository competitionRepository;
    private final SmartValidator smartValidator;
    @Value("${custom.resourceLocation}")
    String resourceLocation;

    public EditEventController(EventRepository eventRepository, CrewRepository crewRepository, CompetitionRepository competitionRepository, SmartValidator smartValidator) {
        this.eventRepository = eventRepository;
        this.crewRepository = crewRepository;
        this.competitionRepository = competitionRepository;
        this.smartValidator = smartValidator;
    }

    @GetMapping
    @PreAuthorize("hasPermission(#event,'" + UserGroupEnum.Constants.ORGANIZER_VALUE + "')")
    String showEditPage(@ModelAttribute("event") Event event) {
        return "event/edit_event";
    }


    @ModelAttribute("crewsWrapper")
    CrewsWrapper getCrews(@PathVariable("url") String url, @ModelAttribute("event") Event event) {
        return new CrewsWrapper(crewRepository.getAllByEvent_UrlOrderByNumberAscYearOfProductionAsc(url, Crew.class).stream().peek(crew -> {
            crew.getDescription().prepareForLoad(event.getEventLanguages());
        }).collect(Collectors.toList()));
    }

    @ModelAttribute("competitions")
    List<Competition> getCompetitions(@PathVariable("url") String url, @ModelAttribute("event") Event event) {
        return competitionRepository.getAllByEvent_UrlOrderById(url, Competition.class);
    }


    @ModelAttribute("event")
    Event getEvent(@PathVariable("url") String url) {
        Event event = eventRepository.getByUrl(url);
        event.getEventLanguages().sort(new EventLanguageComparator());
        return event;
    }

    @PostMapping(params = "crew")
    @PreAuthorize("hasPermission(#event,'" + UserGroupEnum.Constants.ORGANIZER_VALUE + "')")
    String saveSpecificCrew(@ModelAttribute("event") Event event,
                            @ModelAttribute("crewsWrapper") CrewsWrapper crewsWrapper,
                            @RequestParam("crew") Integer index,
                            @RequestParam(value = "photo") MultipartFile photo,
                            @RequestParam(value = "removePhoto", required = false) boolean removePhoto,
                            BindingResult bindingResult) throws IOException {
        Crew crew = crewsWrapper.getCrews().get(index);
        try {
            bindingResult.setNestedPath(String.format("crews[%d]", index));
            smartValidator.validate(crew, bindingResult);
        } finally {
            bindingResult.setNestedPath("");
        }
        if (bindingResult.hasErrors()) {
            return "event/edit_event";
        }
        if (!photo.isEmpty()) {
            FileUploadUtil.savePhotoForCrew(crew, photo, resourceLocation);
        }

        return showEditPage(event);
    }


}
