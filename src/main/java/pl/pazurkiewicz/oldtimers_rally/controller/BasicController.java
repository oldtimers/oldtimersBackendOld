package pl.pazurkiewicz.oldtimers_rally.controller;

import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.pazurkiewicz.oldtimers_rally.model.Crew;
import pl.pazurkiewicz.oldtimers_rally.model.Event;
import pl.pazurkiewicz.oldtimers_rally.model.Language;
import pl.pazurkiewicz.oldtimers_rally.repository.CrewRepository;
import pl.pazurkiewicz.oldtimers_rally.repository.EventRepository;
import pl.pazurkiewicz.oldtimers_rally.repository.LanguageRepository;
import pl.pazurkiewicz.oldtimers_rally.security.service.UserDetailsImpl;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping
public class BasicController {
    private final EventRepository eventRepository;
    private final LanguageRepository languageRepository;
    private final CrewRepository crewRepository;


    public BasicController(EventRepository eventRepository, LanguageRepository languageRepository, CrewRepository crewRepository) {
        this.eventRepository = eventRepository;
        this.languageRepository = languageRepository;
        this.crewRepository = crewRepository;
    }

    @GetMapping(produces = MediaType.TEXT_HTML_VALUE)
    String getIndex(@RequestParam(name = "qr", required = false) String qr, RedirectAttributes redirectAttributes, Model model, @AuthenticationPrincipal UserDetailsImpl principal, @RequestParam(name = "404", defaultValue = "false", required = false) boolean occurred404) {
        if (qr != null) {
            qr = '%' + qr;
            Optional<Crew> crewQr = crewRepository.findByQrLike(qr);
            if (crewQr.isPresent() && crewQr.get().getPresent()) {
                Crew crew = crewQr.get();
                redirectAttributes.addAttribute("url", crew.getEvent().getUrl());
                redirectAttributes.addAttribute("crewId", crew.getId());
                return "redirect:/{url}/crew/{crewId}";
            }
        }
        if (principal != null) {
            List<?> events = eventRepository.getEventsWithGlobalJudgePrivilegesForId(principal.getUser().getId(), Event.class);
            if (events.isEmpty()) {
                events = eventRepository.getEventsWithJudgePrivilegesForId(principal.getUser().getId(), Event.class);
            }
            model.addAttribute("events", events);
        } else {
            model.addAttribute("events", eventRepository.findAllSorted());
        }
        model.addAttribute("occurred404", occurred404);
        return "index";
    }

    @ModelAttribute("languages")
    List<Language> languages() {
        return languageRepository.findAll();
    }
}
