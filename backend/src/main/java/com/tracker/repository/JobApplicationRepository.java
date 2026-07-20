package com.tracker.repository;

import com.tracker.model.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
    // Spring Data JPA generates the query from this method name automatically
    List<JobApplication> findByUserId(Long userId);
}
