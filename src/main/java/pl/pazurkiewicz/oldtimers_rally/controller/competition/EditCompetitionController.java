package pl.pazurkiewicz.oldtimers_rally.controller.competition;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.pazurkiewicz.oldtimers_rally.model.Competition;
import pl.pazurkiewicz.oldtimers_rally.model.Event;
import pl.pazurkiewicz.oldtimers_rally.model.UserGroupEnum;
import pl.pazurkiewicz.oldtimers_rally.repositiory.CompetitionRepository;
import pl.pazurkiewicz.oldtimers_rally.repositiory.EventRepository;

import javax.validation.Valid;

@Controller
@RequestMapping("/{url}/edit")
@SessionAttributes({"newCompetition"})
public class EditCompetitionController {
    private final EventRepository eventRepository;
    private final CompetitionRepository competitionRepository;

    public EditCompetitionController(EventRepository eventRepository, CompetitionRepository competitionRepository) {
        this.eventRepository = eventRepository;
        this.competitionRepository = competitionRepository;
    }

    @GetMapping("/competitions")
    @PreAuthorize("hasPermission(#event,'" + UserGroupEnum.Constants.ORGANIZER_VALUE + "')")
    String getCompetitions(Event event, Model model) {
        model.addAttribute("competitionList", competitionRepository.getByEvent_Id(event.getId()));
        model.addAttribute("newCompetition", new Competition(event));
        return "competition/competitions";
    }

    @PostMapping("/competitions")
    @PreAuthorize("hasPermission(#event,'" + UserGroupEnum.Constants.ORGANIZER_VALUE + "')")
    String saveNewCompetition(@ModelAttribute("newCompetition") @Valid Competition newCompetition, BindingResult bindingResult, Model model, Event event) {
        if (bindingResult.hasErrors()) {
            return "competition/competitions";
        }


        return getCompetitions(event, model);
    }

    @ModelAttribute("event")
    Event getEvent(@PathVariable("url") String url) {
        return eventRepository.getByUrl(url);
    }

}
