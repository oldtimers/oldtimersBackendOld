package pl.pazurkiewicz.oldtimers_rally.controller.api;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pl.pazurkiewicz.oldtimers_rally.exception.InvalidScore;
import pl.pazurkiewicz.oldtimers_rally.model.Competition;
import pl.pazurkiewicz.oldtimers_rally.model.CompetitionTypeEnum;
import pl.pazurkiewicz.oldtimers_rally.model.UserGroupEnum;
import pl.pazurkiewicz.oldtimers_rally.model.api.request.QrRequest;
import pl.pazurkiewicz.oldtimers_rally.model.api.request.RegScoreRequest;
import pl.pazurkiewicz.oldtimers_rally.model.api.request.ScoreRequest;
import pl.pazurkiewicz.oldtimers_rally.model.api.response.CompetitionInfo;
import pl.pazurkiewicz.oldtimers_rally.model.api.response.CrewInfo;
import pl.pazurkiewicz.oldtimers_rally.model.api.response.EventInfo;
import pl.pazurkiewicz.oldtimers_rally.repositiory.CompetitionRepository;
import pl.pazurkiewicz.oldtimers_rally.repositiory.CrewRepository;
import pl.pazurkiewicz.oldtimers_rally.repositiory.EventRepository;
import pl.pazurkiewicz.oldtimers_rally.security.service.UserDetailsImpl;
import pl.pazurkiewicz.oldtimers_rally.service.ScoreService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class BasicApiController {
    private final EventRepository eventRepository;
    private final CompetitionRepository competitionRepository;
    private final CrewRepository crewRepository;
    private final ScoreService scoreService;

    public BasicApiController(EventRepository eventRepository, CompetitionRepository competitionRepository, CrewRepository crewRepository, ScoreService scoreService) {
        this.eventRepository = eventRepository;
        this.competitionRepository = competitionRepository;
        this.crewRepository = crewRepository;
        this.scoreService = scoreService;
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

    @PostMapping("/event/{id}/crew")
    @PreAuthorize("hasPermission(#id,'Event','" + UserGroupEnum.Constants.JUDGE_VALUE + "')")
    ResponseEntity<?> getCrewByQr(@RequestBody @Valid QrRequest qr, @PathVariable Integer id) {
        Optional<CrewInfo> crew = crewRepository.findByQrAndEventId(qr.getQr(), id, CrewInfo.class);
        if (crew.isPresent()) {
            return ResponseEntity.ok(crew.get());
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/event/{id}/score/reg")
    @PreAuthorize("hasPermission(#id,'Event','" + UserGroupEnum.Constants.JUDGE_VALUE + "')")
    ResponseEntity<?> addRegScore(@PathVariable Integer id, @RequestBody @Valid RegScoreRequest scoreRequest) {
        try {
            scoreService.addRegScore(scoreRequest, id);
        } catch (InvalidScore e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/event/{id}/competition/{competitionId}/crews")
    @PreAuthorize("hasPermission(#id,'Event','" + UserGroupEnum.Constants.JUDGE_VALUE + "')")
    ResponseEntity<?> getCrewsInRegCompetition(@PathVariable("id") Integer id, @PathVariable("competitionId") Integer competitionId) {
        Competition competition = competitionRepository.getByEvent_idAndId(id, competitionId);
        if (competition != null) {
            if (competition.getType() == CompetitionTypeEnum.REGULAR_DRIVE) {
                List<CrewInfo> crews = crewRepository.getAllStartedForCompetition(competition, CrewInfo.class);
                return ResponseEntity.ok(crews);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/event/{id}/score")
    @PreAuthorize("hasPermission(#id,'Event','" + UserGroupEnum.Constants.JUDGE_VALUE + "')")
    ResponseEntity<?> addScore(@PathVariable Integer id, @RequestBody @Valid ScoreRequest scoreRequest) {
        try {
            scoreService.addScore(scoreRequest, id);
        } catch (InvalidScore e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }
}
