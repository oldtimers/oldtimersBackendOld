package pl.pazurkiewicz.oldtimers_rally.controller.event;


import org.hibernate.Hibernate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.SmartValidator;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.pazurkiewicz.oldtimers_rally.MyConfigurationProperties;
import pl.pazurkiewicz.oldtimers_rally.exception.InvalidNumberOfCrews;
import pl.pazurkiewicz.oldtimers_rally.exception.InvalidScore;
import pl.pazurkiewicz.oldtimers_rally.model.*;
import pl.pazurkiewicz.oldtimers_rally.model.comparator.EventLanguageComparator;
import pl.pazurkiewicz.oldtimers_rally.model.web.CrewsModel;
import pl.pazurkiewicz.oldtimers_rally.repository.*;
import pl.pazurkiewicz.oldtimers_rally.service.CalculatorService;
import pl.pazurkiewicz.oldtimers_rally.service.CategoriesService;
import pl.pazurkiewicz.oldtimers_rally.service.CrewService;
import pl.pazurkiewicz.oldtimers_rally.service.QrCodeService;
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
    private final CalculatorService calculatorService;
    private final QrCodeService qrCodeService;

    private final LanguageRepository languageRepository;

    private final CategoriesService categoriesService;

    public EditEventController(EventRepository eventRepository, CrewRepository crewRepository, CompetitionRepository competitionRepository, SmartValidator smartValidator, FileUploadService fileUploadService, CategoryRepository categoryRepository, CrewService crewService, CalculatorService calculatorService, QrCodeService qrCodeService, LanguageRepository languageRepository, CategoriesService categoriesService) {
        this.eventRepository = eventRepository;
        this.crewRepository = crewRepository;
        this.competitionRepository = competitionRepository;
        this.smartValidator = smartValidator;
        this.fileUploadService = fileUploadService;
        this.categoryRepository = categoryRepository;
        this.crewService = crewService;
        this.calculatorService = calculatorService;
        this.qrCodeService = qrCodeService;
        this.languageRepository = languageRepository;
        this.categoriesService = categoriesService;
    }

    @GetMapping
    @PreAuthorize("hasPermission(#url,'" + UserGroupEnum.Constants.ORGANIZER_VALUE + "')")
    String showEditPage(@PathVariable("url") String url, @ModelAttribute("event") Event event, Model model) {
        model.addAttribute("crewsModel", getCrews(event));
        event.getEventLanguages().sort(new EventLanguageComparator());
        event.getEventLanguages().forEach(eventLanguage -> Hibernate.initialize(eventLanguage.getLanguage()));
        return "event/edit_event";
    }


    CrewsModel getCrews(Event event) {
        return new CrewsModel(crewRepository.getAllByEvent_UrlOrderByQrCode_NumberAscYearOfProductionAsc(event.getUrl()).stream().peek(crew -> crew.getDescription().prepareForLoad(event.getEventLanguages())).collect(Collectors.toList()), categoryRepository.findByEvent_IdOrderById(event.getId()), event);
    }

    @ModelAttribute("event")
    Event getEvent(@PathVariable("url") String url) {
        return eventRepository.getByUrl(url);
    }

    @ModelAttribute("competitions")
    List<Competition> getCompetitions(@PathVariable("url") String url) {
        return competitionRepository.getByUrl(url);
    }


    @PostMapping(params = "crew")
    @PreAuthorize("hasPermission(#event,'" + UserGroupEnum.Constants.ORGANIZER_VALUE + "')")
    String saveSpecificCrew(@PathVariable("url") String url, @ModelAttribute("event") Event event,
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
        return showEditPage(url, event, model);
    }

    @PostMapping(params = "present")
    @PreAuthorize("hasPermission(#url,'" + UserGroupEnum.Constants.ORGANIZER_VALUE + "')")
    String savePresent(@PathVariable("url") String url, @ModelAttribute("event") Event event, @ModelAttribute("crewsModel") CrewsModel crewsModel, Model model) {
        crewService.saveCrewsModelOnlyPresent(crewsModel);
        categoriesService.calculateYearMultipliers(event.getId());
        return showEditPage(url, event, model);
    }


    @PostMapping("/count")
    @PreAuthorize("hasPermission(#url,'" + UserGroupEnum.Constants.ORGANIZER_VALUE + "')")
    @Transactional
    String countPoints(@PathVariable("url") String url, Event event, RedirectAttributes redirectAttributes) throws InvalidScore {
        if (event.getStage() != StageEnum.NEW && event.getStage() != StageEnum.PRESENTS) {
            calculatorService.countGlobalPoints(event);
            redirectAttributes.addAttribute("url", event.getUrl());
            //invalidateEventByUrl(event.getUrl());
            eventRepository.save(event);
        }
        return "redirect:/{url}/edit";
    }

    @PostMapping("/generate_numbers")
    @PreAuthorize("hasPermission(#url,'" + UserGroupEnum.Constants.ORGANIZER_VALUE + "')")
    @Transactional
    String generateNumbers(@PathVariable("url") String url, Event event, RedirectAttributes redirectAttributes) throws InvalidNumberOfCrews {
        if (event.getStage() != StageEnum.NEW) {
            qrCodeService.assignUsersToQrCode(event);
            redirectAttributes.addAttribute("url", event.getUrl());
            eventRepository.save(event);
        }
        return "redirect:/{url}/edit";
    }

    @PostMapping(value = "/qr_codes", produces = MediaType.APPLICATION_PDF_VALUE)
    @PreAuthorize("hasPermission(#url,'" + UserGroupEnum.Constants.ORGANIZER_VALUE + "')")
    @Transactional
    ResponseEntity<byte[]> generateQrCodes(@PathVariable("url") String url, Event event) throws com.lowagie.text.DocumentException, IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        String filename = String.format("qr_%s.pdf", event.getUrl());
        headers.setContentDispositionFormData(filename, filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        return new ResponseEntity<>(qrCodeService.generateQrList(event), headers, HttpStatus.OK);
    }

//    public void invalidateEventByUrl(String url) {
//        cacheManager.getCache("eventsByUrl").evictIfPresent(url);
//        cacheManager.getCache("eventsId").evictIfPresent(url);
//    }

    @ModelAttribute("languages")
    List<Language> languages(@PathVariable("url") String url) {
        return languageRepository.getLanguagesByUrl(url);
    }
}
