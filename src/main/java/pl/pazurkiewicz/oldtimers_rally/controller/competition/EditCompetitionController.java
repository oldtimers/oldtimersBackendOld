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
import pl.pazurkiewicz.oldtimers_rally.repository.CompetitionRepository;
import pl.pazurkiewicz.oldtimers_rally.repository.EventRepository;
import pl.pazurkiewicz.oldtimers_rally.repository.LanguageRepository;
import pl.pazurkiewicz.oldtimers_rally.service.CompetitionService;

import javax.validation.Valid;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/{url:" + MyConfigurationProperties.eventRegex + "}/edit")
@SessionAttributes({"newCompetition", "competitionModel"})
public class EditCompetitionController {
    private final EventRepository eventRepository;
    private final CompetitionRepository competitionRepository;
    private final SmartValidator smartValidator;
    private final CompetitionService competitionService;
    private final LanguageRepository languageRepository;

    public EditCompetitionController(EventRepository eventRepository, CompetitionRepository competitionRepository, SmartValidator smartValidator, CompetitionService competitionService, LanguageRepository languageRepository) {
        this.eventRepository = eventRepository;
        this.competitionRepository = competitionRepository;
        this.smartValidator = smartValidator;
        this.competitionService = competitionService;
        this.languageRepository = languageRepository;
    }

    @GetMapping("/competition")
    @PreAuthorize("hasPermission(#url,'" + UserGroupEnum.Constants.ORGANIZER_VALUE + "')")
    String getCompetitions(@PathVariable("url") String url, Event event, Model model) {
        model.addAttribute("newCompetition", new CompetitionModel(event));
        model.addAttribute("competitionList", competitionRepository.getByEvent(event));
        return "competition/competitions";
    }

    @PostMapping("/competition")
    @PreAuthorize("hasPermission(#url,'" + UserGroupEnum.Constants.ORGANIZER_VALUE + "')")
    String saveNewCompetition(@ModelAttribute("newCompetition") @Valid CompetitionModel newCompetition, BindingResult bindingResult, Model model, @PathVariable("url") String url, Event event, RedirectAttributes redirectAttributes) {
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
    @PreAuthorize("hasPermission(#url,'" + UserGroupEnum.Constants.ORGANIZER_VALUE + "')")
    String getSelectedCompetition(@PathVariable("url") String url, Event event, @PathVariable("competitionId") Integer competitionId, Model model) {
        Competition competition = competitionRepository.getByEventAndId(event, competitionId);
        if (competition == null) {
            throw new ResourceNotFoundException();
        }
        competition.getFields().sort(Comparator.comparingInt(CompetitionField::getOrder));
        model.addAttribute("competitionModel", new CompetitionModel(competition, event));
        return "competition/selected_competition";
    }

    @PostMapping(value = "/competition/{competitionId}", params = "add")
    @PreAuthorize("hasPermission(#url,'" + UserGroupEnum.Constants.ORGANIZER_VALUE + "')")
    String addVariable(@PathVariable("url") String url, Event event, @PathVariable("competitionId") Integer competitionId, @ModelAttribute("competitionModel") CompetitionModel competitionModel) {
        checkAccess(competitionId, competitionModel);
        competitionModel.addField();
        return "competition/selected_competition";
    }

    @PostMapping(value = "/competition/{competitionId}", params = "delete")
    @PreAuthorize("hasPermission(#url,'" + UserGroupEnum.Constants.ORGANIZER_VALUE + "')")
    String removeSelectedField(@PathVariable("url") String url, Event event, @PathVariable("competitionId") Integer competitionId, @ModelAttribute("competitionModel") CompetitionModel competitionModel, BindingResult bindingResult, @RequestParam(value = "delete") Integer deleteId) {
        checkAccess(competitionId, competitionModel);
        competitionModel.removeField(deleteId);
        return "competition/selected_competition";
    }

    @PostMapping(value = "/competition/{competitionId}", params = "deleteFull")
    @PreAuthorize("hasPermission(#url,'" + UserGroupEnum.Constants.ORGANIZER_VALUE + "')")
    String removeCompetition(@PathVariable("url") String url, Event event, @PathVariable("competitionId") Integer competitionId, @ModelAttribute("competitionModel") CompetitionModel competitionModel, RedirectAttributes redirectAttributes) {
        checkAccess(competitionId, competitionModel);
        competitionService.removeCompetition(competitionId);
        redirectAttributes.addAttribute("url", event.getUrl());
        return "redirect:/{url}/edit/competition";
    }

    @PostMapping("/competition/{competitionId}")
    @PreAuthorize("hasPermission(#url,'" + UserGroupEnum.Constants.ORGANIZER_VALUE + "')")
    String saveSelectedCompetition(@PathVariable("url") String url, Event event, @PathVariable("competitionId") Integer competitionId, @ModelAttribute("competitionModel") @Valid CompetitionModel competitionModel, BindingResult bindingResult, Model model) {
        checkAccess(competitionId, competitionModel);
        competitionModel.validate(smartValidator, bindingResult);
        if (bindingResult.hasErrors()) {
            return "competition/selected_competition";
        }
        competitionService.saveCompetitionModel(competitionModel);
        return getSelectedCompetition(url, event, competitionId, model);
    }

    private void checkAccess(Integer competitionId, CompetitionModel competitionModel) {
        if (!Objects.equals(competitionId, competitionModel.getCompetition().getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid competitionId");
        }
    }


    @ModelAttribute("languages")
    List<Language> languages(@PathVariable("url") String url) {
        return languageRepository.getLanguagesByUrl(url);
    }

}
