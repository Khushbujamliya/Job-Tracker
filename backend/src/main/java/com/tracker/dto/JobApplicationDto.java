package com.tracker.dto;

import com.tracker.model.ApplicationStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

// This DTO is what the API accepts/returns for a job application —
// kept separate from the JobApplication entity so the API shape isn't
// tightly coupled to the database schema (a common interview talking point).
public class JobApplicationDto {

    private Long id;

    @NotBlank
    private String company;

    @NotBlank
    private String role;

    @NotNull
    private ApplicationStatus status;

    private LocalDate appliedDate;

    private String notes;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCompany() { return company; }
    public void setCompany(String company) { this.company = company; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public ApplicationStatus getStatus() { return status; }
    public void setStatus(ApplicationStatus status) { this.status = status; }

    public LocalDate getAppliedDate() { return appliedDate; }
    public void setAppliedDate(LocalDate appliedDate) { this.appliedDate = appliedDate; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
