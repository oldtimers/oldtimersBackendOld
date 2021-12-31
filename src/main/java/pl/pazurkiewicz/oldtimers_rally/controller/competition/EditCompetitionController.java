package pl.pazurkiewicz.oldtimers_rally.controller.competition;


import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.SmartValidator;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.pazurkiewicz.oldtimers_rally.MyConfigurationProperties;
import pl.pazurkiewicz.oldtimers_rally.model.*;
import pl.pazurkiewicz.oldtimers_rally.model.web.CompetitionModel;
import pl.pazurkiewicz.oldtimers_rally.repositiory.CompetitionRepository;
import pl.pazurkiewicz.oldtimers_rally.repositiory.EventRepository;
import pl.pazurkiewicz.oldtimers_rally.service.CompetitionService;

import javax.validation.Valid;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/{url:" + MyConfigurationProperties.eventRegex + "}/edit")
@SessionAttributes({"newCompetition", "competitionModel"})
public class EditCompetitionController {
    private final EventRepository eventRepository;
    private final CompetitionRepository competitionRepository;
    private final SmartValidator smartValidator;
    private final CompetitionService competitionService;

    public EditCompetitionController(EventRepository eventRepository, CompetitionRepository competitionRepository, SmartValidator smartValidator, CompetitionService competitionService) {
        this.eventRepository = eventRepository;
        this.competitionRepository = competitionRepository;
        this.smartValidator = smartValidator;
        this.competitionService = competitionService;
    }

    @GetMapping("/competition")
    @PreAuthorize("hasPermission(#event,'" + UserGroupEnum.Constants.ORGANIZER_VALUE + "')")
    String getCompetitions(Event event, Model model) {
        model.addAttribute("newCompetition", new CompetitionModel(event));
        model.addAttribute("competitionList", competitionRepository.getByEvent(event));
        return "competition/competitions";
    }

    @PostMapping("/competition")
    @PreAuthorize("hasPermission(#event,'" + UserGroupEnum.Constants.ORGANIZER_VALUE + "')")
    String saveNewCompetition(@ModelAttribute("newCompetition") @Valid CompetitionModel newCompetition, BindingResult bindingResult, Model model, Event event, RedirectAttributes redirectAttributes) {
        newCompetition.validate(smartValidator, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("competitionList", competitionRepository.getByEvent(event));
            return "competition/competitions";
        }
        Competition saved = competitionService.saveCompetitionModel(newCompetition);
        redirectAttributes.addAttribute("url", event.getUrl());
        redirectAttributes.addAttribute("competitionId", saved.getId());
        return "redirect:/{url}/edit/competition/{competitionId}";
    }

    @ModelAttribute("event")
    Event getEvent(@PathVariable("url") String url) {
        return eventRepository.getByUrl(url);
    }

    @GetMapping("/competition/{competitionId}")
    @PreAuthorize("hasPermission(#event,'" + UserGroupEnum.Constants.ORGANIZER_VALUE + "')")
    String getSelectedCompetition(Event event, @PathVariable("competitionId") Integer competitionId, Model model) {
        Competition competition = competitionRepository.getByEventAndId(event, competitionId);
        if (competition == null) {
            throw new ResourceNotFoundException();
        }
        competition.getFields().sort(Comparator.comparingInt(CompetitionField::getOrder));
        model.addAttribute("competitionModel", new CompetitionModel(competition, event));
        return "competition/selected_competition";
    }

    @PostMapping(value = "/competition/{competitionId}", params = "add")
    @PreAuthorize("hasPermission(#event,'" + UserGroupEnum.Constants.ORGANIZER_VALUE + "')")
    String addVariable(Event event, @PathVariable("competitionId") Integer competitionId, @ModelAttribute("competitionModel") CompetitionModel competitionModel) {
        checkAccess(competitionId, competitionModel);
        competitionModel.addField();
        return "competition/selected_competition";
    }

    @PostMapping(value = "/competition/{competitionId}", params = "delete")
    @PreAuthorize("hasPermission(#event,'" + UserGroupEnum.Constants.ORGANIZER_VALUE + "')")
    String removeSelectedField(Event event, @PathVariable("competitionId") Integer competitionId, @ModelAttribute("competitionModel") CompetitionModel competitionModel, BindingResult bindingResult, @RequestParam(value = "delete") Integer deleteId) {
        checkAccess(competitionId, competitionModel);
        competitionModel.removeField(deleteId);
        return "competition/selected_competition";
    }

    @PostMapping(value = "/competition/{competitionId}", params = "deleteFull")
    @PreAuthorize("hasPermission(#event,'" + UserGroupEnum.Constants.ORGANIZER_VALUE + "')")
    String removeCompetition(Event event, @PathVariable("competitionId") Integer competitionId, @ModelAttribute("competitionModel") CompetitionModel competitionModel, RedirectAttributes redirectAttributes) {
        checkAccess(competitionId, competitionModel);
        competitionService.removeCompetition(competitionId);
        redirectAttributes.addAttribute("url", event.getUrl());
        return "redirect:/{url}/edit/competition";
    }

    @PostMapping("/competition/{competitionId}")
    @PreAuthorize("hasPermission(#event,'" + UserGroupEnum.Constants.ORGANIZER_VALUE + "')")
    String saveSelectedCompetition(Event event, @PathVariable("competitionId") Integer competitionId, @ModelAttribute("competitionModel") @Valid CompetitionModel competitionModel, BindingResult bindingResult, Model model) {
        checkAccess(competitionId, competitionModel);
        competitionModel.validate(smartValidator, bindingResult);
        if (bindingResult.hasErrors()) {
            return "competition/selected_competition";
        }
        competitionService.saveCompetitionModel(competitionModel);
        return getSelectedCompetition(event, competitionId, model);
    }

    private void checkAccess(Integer competitionId, CompetitionModel competitionModel) {
        if (!Objects.equals(competitionId, competitionModel.getCompetition().getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid competitionId");
        }
    }


    @ModelAttribute("languages")
    List<Language> languages(Event event) {
        if (event != null) {
            return event.getEventLanguages().stream().map(EventLanguage::getLanguage).collect(Collectors.toList());
        }
        return new LinkedList<>();
    }

}
