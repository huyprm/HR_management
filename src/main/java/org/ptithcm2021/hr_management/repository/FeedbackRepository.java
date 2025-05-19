package org.ptithcm2021.hr_management.repository;

import org.ptithcm2021.hr_management.dto.response.FeedbackResponse;
import org.ptithcm2021.hr_management.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    List<Feedback> findAllBySenderId(long senderId);
}
