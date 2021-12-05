package pl.pazurkiewicz.oldtimers_rally.controller.event;


import org.hibernate.Hibernate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.SmartValidator;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.pazurkiewicz.oldtimers_rally.MyConfigurationProperties;
import pl.pazurkiewicz.oldtimers_rally.model.Competition;
import pl.pazurkiewicz.oldtimers_rally.model.Crew;
import pl.pazurkiewicz.oldtimers_rally.model.Event;
import pl.pazurkiewicz.oldtimers_rally.model.UserGroupEnum;
import pl.pazurkiewicz.oldtimers_rally.model.comparator.EventLanguageComparator;
import pl.pazurkiewicz.oldtimers_rally.model.web.CrewsModel;
import pl.pazurkiewicz.oldtimers_rally.repositiory.CategoryRepository;
import pl.pazurkiewicz.oldtimers_rally.repositiory.CompetitionRepository;
import pl.pazurkiewicz.oldtimers_rally.repositiory.CrewRepository;
import pl.pazurkiewicz.oldtimers_rally.repositiory.EventRepository;
import pl.pazurkiewicz.oldtimers_rally.service.CrewService;
import pl.pazurkiewicz.oldtimers_rally.utils.FileUploadService;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/{url:" + MyConfigurationProperties.eventRegex + "}/edit")
@SessionAttributes({"crewsModel"})
public class EditEventController {
    private final EventRepository eventRepository;
    private final CrewRepository crewRepository;
    private final CompetitionRepository competitionRepository;
    private final SmartValidator smartValidator;
    private final FileUploadService fileUploadService;
    private final CategoryRepository categoryRepository;
    private final CrewService crewService;

    public EditEventController(EventRepository eventRepository, CrewRepository crewRepository, CompetitionRepository competitionRepository, SmartValidator smartValidator, FileUploadService fileUploadService, CategoryRepository categoryRepository, CrewService crewService) {
        this.eventRepository = eventRepository;
        this.crewRepository = crewRepository;
        this.competitionRepository = competitionRepository;
        this.smartValidator = smartValidator;
        this.fileUploadService = fileUploadService;
        this.categoryRepository = categoryRepository;
        this.crewService = crewService;
    }

    @GetMapping
    @PreAuthorize("hasPermission(#event,'" + UserGroupEnum.Constants.ORGANIZER_VALUE + "')")
    String showEditPage(@ModelAttribute("event") Event event, Model model) {
        model.addAttribute("crewsModel", getCrews(event));
        event.getEventLanguages().sort(new EventLanguageComparator());
        event.getEventLanguages().forEach(eventLanguage -> Hibernate.initialize(eventLanguage.getLanguage()));
        return "event/edit_event";
    }


    CrewsModel getCrews(Event event) {
        return new CrewsModel(crewRepository.getAllByEvent_UrlOrderByNumberAscYearOfProductionAsc(event.getUrl(), Crew.class).stream().peek(crew -> {
            crew.getDescription().prepareForLoad(event.getEventLanguages());
        }).collect(Collectors.toList()), categoryRepository.findByEvent_IdOrderById(event.getId()), event);
    }

    @ModelAttribute("competitions")
    List<Competition> getCompetitions(@ModelAttribute("event") Event event) {
        return competitionRepository.getByEvent(event);
    }


    @ModelAttribute("event")
    Event getEvent(@PathVariable("url") String url) {
        return eventRepository.getByUrl(url);
    }


    @PostMapping(params = "crew")
    @PreAuthorize("hasPermission(#event,'" + UserGroupEnum.Constants.ORGANIZER_VALUE + "')")
    String saveSpecificCrew(@ModelAttribute("event") Event event,
                            @ModelAttribute("crewsModel") CrewsModel crewsModel,
                            @RequestParam("crew") Integer index,
                            @RequestParam(value = "photo") MultipartFile photo,
                            @RequestParam(value = "removePhoto", required = false, defaultValue = "false") boolean removePhoto,
                            BindingResult bindingResult,
                            Model model) throws IOException {
        Crew crew = crewsModel.getCrews().get(index).getCrew();
        try {
            bindingResult.setNestedPath(String.format("crews[%d].crew", index));
            smartValidator.validate(crew, bindingResult);
        } finally {
            bindingResult.setNestedPath("");
        }
        if (bindingResult.hasErrors()) {
            return "event/edit_event";
        }
        if (!photo.isEmpty()) {
            crew.setPhoto(fileUploadService.savePhotoForCrew(crew, photo));
        } else if (removePhoto) {
            fileUploadService.deleteForCrew(crew);
            crew.setPhoto(null);
        }
        crewService.saveCrewModel(crewsModel.getCrews().get(index), event);
        return showEditPage(event, model);
    }


}
