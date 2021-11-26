package pl.pazurkiewicz.oldtimers_rally.controller.event;

import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import pl.pazurkiewicz.oldtimers_rally.model.Event;
import pl.pazurkiewicz.oldtimers_rally.model.web.CrewsModel;
import pl.pazurkiewicz.oldtimers_rally.repositiory.CrewRepository;
import pl.pazurkiewicz.oldtimers_rally.repositiory.EventRepository;

@Controller
@RequestMapping("/{url}/edit/crews")
@SessionAttributes({"crews"})
public class EditCrewsController extends AbstractEventController{
    private final CrewRepository crewRepository;


    public EditCrewsController(EventRepository eventRepository, CacheManager cacheManager, CrewRepository crewRepository) {
        super(eventRepository, cacheManager);
        this.crewRepository = crewRepository;
    }

    @GetMapping
    String getCrews(Model model, Event event){
        model.addAttribute("crews", new CrewsModel(crewRepository.getByEvent_Id(event.getId()), event));
        return "event/crews";
    }

}
