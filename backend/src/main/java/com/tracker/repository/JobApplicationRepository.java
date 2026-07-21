package com.tracker.repository;

import com.tracker.model.ApplicationStatus;
import com.tracker.model.JobApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
    List<JobApplication> findByUserId(Long userId);

    @org.springframework.data.jpa.repository.Query("""
            SELECT a FROM JobApplication a
            WHERE a.userId = :userId
            AND (:status IS NULL OR a.status = :status)
            AND (:company IS NULL OR LOWER(a.company) LIKE LOWER(CONCAT('%', :company, '%')))
            ORDER BY a.id DESC
            """)
    Page<JobApplication> search(Long userId, ApplicationStatus status, String company, Pageable pageable);

    List<JobApplication> findByStatusAndAppliedDateBeforeAndReminderSentFalse(
            ApplicationStatus status, java.time.LocalDate cutoff);
}