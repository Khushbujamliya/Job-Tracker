package com.tracker.service;

import com.tracker.dto.JobApplicationDto;
import com.tracker.model.JobApplication;
import com.tracker.model.User;
import com.tracker.repository.JobApplicationRepository;
import com.tracker.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobApplicationService {

    private final JobApplicationRepository applicationRepository;
    private final UserRepository userRepository;

    public JobApplicationService(JobApplicationRepository applicationRepository, UserRepository userRepository) {
        this.applicationRepository = applicationRepository;
        this.userRepository = userRepository;
    }

    private Long getUserIdByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return user.getId();
    }

    public List<JobApplicationDto> getAllForUser(String userEmail) {
        Long userId = getUserIdByEmail(userEmail);
        return applicationRepository.findByUserId(userId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public JobApplicationDto create(String userEmail, JobApplicationDto dto) {
        Long userId = getUserIdByEmail(userEmail);

        JobApplication app = new JobApplication();
        app.setCompany(dto.getCompany());
        app.setRole(dto.getRole());
        app.setStatus(dto.getStatus());
        app.setAppliedDate(dto.getAppliedDate());
        app.setNotes(dto.getNotes());
        app.setUserId(userId);

        return toDto(applicationRepository.save(app));
    }

    public JobApplicationDto update(String userEmail, Long id, JobApplicationDto dto) {
        Long userId = getUserIdByEmail(userEmail);
        JobApplication app = applicationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Application not found"));

        // Important check: make sure this application actually belongs to
        // the logged-in user, so one user can't edit another user's data.
        if (!app.getUserId().equals(userId)) {
            throw new SecurityException("You do not have access to this application");
        }

        app.setCompany(dto.getCompany());
        app.setRole(dto.getRole());
        app.setStatus(dto.getStatus());
        app.setAppliedDate(dto.getAppliedDate());
        app.setNotes(dto.getNotes());

        return toDto(applicationRepository.save(app));
    }

    public void delete(String userEmail, Long id) {
        Long userId = getUserIdByEmail(userEmail);
        JobApplication app = applicationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Application not found"));

        if (!app.getUserId().equals(userId)) {
            throw new SecurityException("You do not have access to this application");
        }

        applicationRepository.delete(app);
    }

    private JobApplicationDto toDto(JobApplication app) {
        JobApplicationDto dto = new JobApplicationDto();
        dto.setId(app.getId());
        dto.setCompany(app.getCompany());
        dto.setRole(app.getRole());
        dto.setStatus(app.getStatus());
        dto.setAppliedDate(app.getAppliedDate());
        dto.setNotes(app.getNotes());
        return dto;
    }
}
