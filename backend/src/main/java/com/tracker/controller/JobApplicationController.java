package com.tracker.controller;

import com.tracker.dto.JobApplicationDto;
import com.tracker.service.JobApplicationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/applications")
public class JobApplicationController {

    private final JobApplicationService applicationService;

    public JobApplicationController(JobApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportCsv(Authentication auth) {
        String csv = applicationService.exportAsCsv(auth.getName());
        byte[] bytes = csv.getBytes(java.nio.charset.StandardCharsets.UTF_8);

        org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.parseMediaType("text/csv"));
        headers.setContentDisposition(
                org.springframework.http.ContentDisposition.attachment()
                        .filename("job-applications.csv")
                        .build());

        return new ResponseEntity<>(bytes, headers, org.springframework.http.HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<org.springframework.data.domain.Page<JobApplicationDto>> getAll(
            Authentication auth,
            @RequestParam(required = false) com.tracker.model.ApplicationStatus status,
            @RequestParam(required = false) String company,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size);
        return ResponseEntity.ok(applicationService.search(auth.getName(), status, company, pageable));
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