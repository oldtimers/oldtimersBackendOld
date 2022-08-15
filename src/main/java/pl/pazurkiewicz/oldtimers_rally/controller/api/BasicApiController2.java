package pl.pazurkiewicz.oldtimers_rally.controller.api;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.SmartValidator;
import org.springframework.web.bind.annotation.*;
import pl.pazurkiewicz.oldtimers_rally.exception.InvalidScore;
import pl.pazurkiewicz.oldtimers_rally.model.Competition;
import pl.pazurkiewicz.oldtimers_rally.model.Score;
import pl.pazurkiewicz.oldtimers_rally.model.UserGroupEnum;
import pl.pazurkiewicz.oldtimers_rally.model.api.request.AdvancedRegScoreRequest;
import pl.pazurkiewicz.oldtimers_rally.model.api.request.AdvancedScoreRequest;
import pl.pazurkiewicz.oldtimers_rally.model.api.request2.RegScoreRequest;
import pl.pazurkiewicz.oldtimers_rally.model.api.request2.ScoreRequest;
import pl.pazurkiewicz.oldtimers_rally.model.api.response2.CompetitionInfo;
import pl.pazurkiewicz.oldtimers_rally.model.api.response2.CrewInfo;
import pl.pazurkiewicz.oldtimers_rally.model.api.response2.EventInfo;
import pl.pazurkiewicz.oldtimers_rally.model.api.response2.ShowScores;
import pl.pazurkiewicz.oldtimers_rally.repository.CompetitionRepository;
import pl.pazurkiewicz.oldtimers_rally.repository.CrewRepository;
import pl.pazurkiewicz.oldtimers_rally.repository.EventRepository;
import pl.pazurkiewicz.oldtimers_rally.repository.ScoreRepository;
import pl.pazurkiewicz.oldtimers_rally.security.service.UserDetailsImpl;
import pl.pazurkiewicz.oldtimers_rally.service.ScoreService;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static pl.pazurkiewicz.oldtimers_rally.model.CompetitionTypeEnum.REGULAR_DRIVE;

@RestController
@RequestMapping("/api2")
public class BasicApiController2 {
    private final EventRepository eventRepository;
    private final CompetitionRepository competitionRepository;
    private final CrewRepository crewRepository;
    private final ScoreService scoreService;
    private final ObjectMapper objectMapper;
    private final SmartValidator smartValidator;
    private final ScoreRepository scoreRepository;


    public BasicApiController2(EventRepository eventRepository, CompetitionRepository competitionRepository, CrewRepository crewRepository, ScoreService scoreService, SmartValidator smartValidator, ScoreRepository scoreRepository) {
        this.eventRepository = eventRepository;
        this.competitionRepository = competitionRepository;
        this.crewRepository = crewRepository;
        this.scoreService = scoreService;
        this.smartValidator = smartValidator;
        this.scoreRepository = scoreRepository;
        this.objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @GetMapping("/event")
    @PreAuthorize("isAuthenticated()")
    List<?> getEventsWithPrivileges(@AuthenticationPrincipal UserDetailsImpl principal) {
        Integer userId = principal.getUser().getId();
        List<?> result = eventRepository.getEventsWithGlobalJudgePrivilegesForId(userId, EventInfo.class);
        if (result.isEmpty()) {
            return eventRepository.getEventsWithJudgePrivilegesForId(principal.getUser().getId(), EventInfo.class);
        }
        return result;
    }

    @GetMapping("/event/{id}")
    @PreAuthorize("hasPermission(#id,'Event','" + UserGroupEnum.Constants.JUDGE_VALUE + "')")
    ResponseEntity<?> getEvent(@PathVariable Integer id) {
        Optional<EventInfo> eventInfo = eventRepository.findById(id, EventInfo.class);
        if (eventInfo.isPresent()) {
            return ResponseEntity.ok(eventInfo.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/event/{id}/competition")
    @PreAuthorize("hasPermission(#id,'Event','" + UserGroupEnum.Constants.JUDGE_VALUE + "')")
    ResponseEntity<?> getCompetitions(@PathVariable Integer id) {
        boolean eventExist = eventRepository.existsById(id);
        if (eventExist) {
            List<CompetitionInfo> competitions = competitionRepository.getByEvent_Id(id, CompetitionInfo.class);
            return ResponseEntity.ok(competitions);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/event/{id}/competition/{competitionId}")
    @PreAuthorize("hasPermission(#id,'Event','" + UserGroupEnum.Constants.JUDGE_VALUE + "')")
    ResponseEntity<?> getCompetition(@PathVariable Integer id, @PathVariable Integer competitionId) {
        Optional<CompetitionInfo> competition = competitionRepository.getByEvent_idAndId(id, competitionId, CompetitionInfo.class);
        if (competition.isPresent()) {
            return ResponseEntity.ok(competition);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/event/{eventId}/crew", params = {"id"})
    @PreAuthorize("hasPermission(#eventId,'Event','" + UserGroupEnum.Constants.JUDGE_VALUE + "')")
    ResponseEntity<?> getCrewById(@RequestParam(name = "id") String rawId, @PathVariable Integer eventId) {
        try {
            int id = Integer.parseInt(rawId);
            Optional<CrewInfo> crew = crewRepository.findByEvent_IdAndId(id, eventId, CrewInfo.class);
            if (crew.isPresent()) {
                return ResponseEntity.ok(crew.get());
            }
        } catch (NumberFormatException ignored) {
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping(value = "/event/{id}/crew", params = {"qr"})
    @PreAuthorize("hasPermission(#id,'Event','" + UserGroupEnum.Constants.JUDGE_VALUE + "')")
    ResponseEntity<?> getCrewByQr(@RequestParam(name = "qr") String qr, @PathVariable Integer id) {
        Optional<CrewInfo> crew = crewRepository.findByQrAndEventId(qr, id, CrewInfo.class);
        if (crew.isPresent()) {
            return ResponseEntity.ok(crew.get());
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/event/{id}/crew")
    @PreAuthorize("hasPermission(#id,'Event','" + UserGroupEnum.Constants.JUDGE_VALUE + "')")
    ResponseEntity<?> getCrews(@PathVariable Integer id) {
        Set<CrewInfo> crews = crewRepository.getAllByEventAndCrewIsPresent(id, CrewInfo.class);
        return ResponseEntity.ok(crews);
    }

    @PostMapping("/event/{eventId}/competition/{competitionId}/crew/{crewId}")
    @PreAuthorize("hasPermission(#eventId,'Event','" + UserGroupEnum.Constants.JUDGE_VALUE + "')")
    @Transactional
    ResponseEntity<?> addScore(@PathVariable Integer eventId, @PathVariable Integer competitionId, @PathVariable Integer crewId, @RequestBody Map<String, Object> request, @AuthenticationPrincipal UserDetailsImpl principal, BindingResult bindingResult) {
        Optional<Competition> competition = competitionRepository.getByEvent_idAndId(eventId, competitionId, Competition.class);
        if (competition.isPresent()) {
            try {
                if (competition.get().getType() == REGULAR_DRIVE) {
                    RegScoreRequest scoreRequest = objectMapper.convertValue(request, RegScoreRequest.class);
                    AdvancedRegScoreRequest advancedScoreRequest = new AdvancedRegScoreRequest(scoreRequest, competitionId, crewId);
                    smartValidator.validate(advancedScoreRequest, bindingResult);
                    if (bindingResult.hasErrors()) {
                        return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
                    }
                    scoreService.addRegScore(advancedScoreRequest, eventId, principal.getUser());
                } else {
                    ScoreRequest scoreRequest = objectMapper.convertValue(request, ScoreRequest.class);
                    AdvancedScoreRequest advancedScoreRequest = new AdvancedScoreRequest(scoreRequest, competitionId, crewId);
                    smartValidator.validate(advancedScoreRequest, bindingResult);
                    if (bindingResult.hasErrors()) {
                        return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
                    }
                    scoreService.addScore(advancedScoreRequest, eventId, principal.getUser());
                }
            } catch (InvalidScore e) {
                return ResponseEntity.badRequest().build();
            }
        } else {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/event/{id}/competition/{competitionId}/crew")
    @PreAuthorize("hasPermission(#id,'Event','" + UserGroupEnum.Constants.JUDGE_VALUE + "')")
    ResponseEntity<?> getResultsForCrewsInCompetition(@PathVariable("id") Integer id, @PathVariable("competitionId") Integer competitionId) {
        Optional<Competition> competition = competitionRepository.getByEvent_idAndId(id, competitionId, Competition.class);
        if (competition.isPresent()) {
            return ResponseEntity.ok(new ShowScores(scoreRepository.getScoresByCompetition(competition.get(), Score.class)));
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/event/{id}/scores")
    @PreAuthorize("hasPermission(#id,'Event','" + UserGroupEnum.Constants.JUDGE_VALUE + "')")
    @Transactional
    ResponseEntity<?> addScores(@PathVariable Integer id, @RequestBody List<Map<String, Object>> request, @AuthenticationPrincipal UserDetailsImpl principal, BindingResult bindingResult) throws InvalidScore {
        for (Map<String, Object> entry : request) {
            ResponseEntity<?> smallResponse = addScore(id,
                    (Integer) entry.get("competitionId"),
                    (Integer) entry.get("crewId"),
                    entry,
                    principal,
                    bindingResult);
            if (smallResponse.getStatusCode() != HttpStatus.OK) {
                throw new InvalidScore("Invalid entry");
            }
        }
        return ResponseEntity.ok().build();
    }
}
