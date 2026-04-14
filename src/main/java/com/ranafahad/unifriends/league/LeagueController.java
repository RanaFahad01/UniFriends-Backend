package com.ranafahad.unifriends.league;

import com.ranafahad.unifriends.league.dto.CreateLeagueRequest;
import com.ranafahad.unifriends.league.dto.LeagueResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/leagues")
@RequiredArgsConstructor
public class LeagueController {

    private final LeagueService leagueService;

    @GetMapping
    public ResponseEntity<List<LeagueResponse>> getLeagues(@RequestParam LeagueType type) {
        List<League> leagues = leagueService.findByType(type);
        return ResponseEntity.ok(leagues.stream().map(LeagueResponse::from).toList());
    }

    @GetMapping("/me")
    public ResponseEntity<List<LeagueResponse>> getMyLeagues(Principal principal) {
        List<League> leagues = leagueService.findLeaguesByUserEmail(principal.getName());
        return ResponseEntity.ok(leagues.stream().map(LeagueResponse::from).toList());
    }

    @PostMapping
    public ResponseEntity<LeagueResponse> createLeague(
            @RequestBody CreateLeagueRequest request,
            Principal principal) {
        League league = leagueService.createLeague(principal.getName(), request.name(), request.type(), request.description(), request.mascot());
        return ResponseEntity.ok(LeagueResponse.from(league));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LeagueResponse> getLeague(@PathVariable Long id) {
        return ResponseEntity.ok(LeagueResponse.from(leagueService.findById(id)));
    }

    @PostMapping("/{id}/join")
    public ResponseEntity<Void> joinLeague(@PathVariable Long id, Principal principal) {
        leagueService.joinLeague(id, principal.getName());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/members/{userId}")
    public ResponseEntity<Void> removeMember(
            @PathVariable Long id,
            @PathVariable Long userId,
            Principal principal) {
        leagueService.removeMember(id, userId, principal.getName());
        return ResponseEntity.ok().build();
    }
}
