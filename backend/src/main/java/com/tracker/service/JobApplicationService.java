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

    public org.springframework.data.domain.Page<JobApplicationDto> search(
            String userEmail,
            com.tracker.model.ApplicationStatus status,
            String company,
            org.springframework.data.domain.Pageable pageable) {
        Long userId = getUserIdByEmail(userEmail);
        return applicationRepository.search(userId, status, company, pageable)
                .map(this::toDto);
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

    public String exportAsCsv(String userEmail) {
        Long userId = getUserIdByEmail(userEmail);
        List<JobApplication> apps = applicationRepository.findByUserId(userId);

        StringBuilder sb = new StringBuilder();
        sb.append("Company,Role,Status,Applied Date,Notes\n");
        for (JobApplication app : apps) {
            sb.append(csvEscape(app.getCompany())).append(",");
            sb.append(csvEscape(app.getRole())).append(",");
            sb.append(app.getStatus()).append(",");
            sb.append(app.getAppliedDate() != null ? app.getAppliedDate() : "").append(",");
            sb.append(csvEscape(app.getNotes())).append("\n");
        }
        return sb.toString();
    }

    private String csvEscape(String value) {
        if (value == null)
            return "";
        String escaped = value.replace("\"", "\"\"");
        return "\"" + escaped + "\"";
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