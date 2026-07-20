package com.tracker.controller;

import com.tracker.dto.JobApplicationDto;
import com.tracker.service.JobApplicationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
public class JobApplicationController {

    private final JobApplicationService applicationService;

    public JobApplicationController(JobApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    // Authentication object is auto-injected by Spring Security once the
    // JwtAuthFilter has validated the token — .getName() gives us the logged-in user's email.

    @GetMapping
    public ResponseEntity<List<JobApplicationDto>> getAll(Authentication auth) {
        return ResponseEntity.ok(applicationService.getAllForUser(auth.getName()));
    }

    @PostMapping
    public ResponseEntity<JobApplicationDto> create(Authentication auth, @Valid @RequestBody JobApplicationDto dto) {
        return ResponseEntity.ok(applicationService.create(auth.getName(), dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<JobApplicationDto> update(Authentication auth, @PathVariable Long id,
                                                      @Valid @RequestBody JobApplicationDto dto) {
        return ResponseEntity.ok(applicationService.update(auth.getName(), id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(Authentication auth, @PathVariable Long id) {
        applicationService.delete(auth.getName(), id);
        return ResponseEntity.noContent().build();
    }
}
