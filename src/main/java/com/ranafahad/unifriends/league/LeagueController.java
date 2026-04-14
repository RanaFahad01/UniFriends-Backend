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
        return ResponseEntity.ok(leagueService.findByType(type));
    }

    @GetMapping("/me")
    public ResponseEntity<List<LeagueResponse>> getMyLeagues(Principal principal) {
        return ResponseEntity.ok(leagueService.findLeaguesByUserEmail(principal.getName()));
    }

    @PostMapping
    public ResponseEntity<LeagueResponse> createLeague(
            @RequestBody CreateLeagueRequest request,
            Principal principal) {
        return ResponseEntity.ok(leagueService.createLeague(principal.getName(), request.name(), request.type(), request.description(), request.mascot()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LeagueResponse> getLeague(@PathVariable Long id) {
        return ResponseEntity.ok(leagueService.findById(id));
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
