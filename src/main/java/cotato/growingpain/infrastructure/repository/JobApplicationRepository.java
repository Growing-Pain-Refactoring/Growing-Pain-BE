package cotato.growingpain.infrastructure.repository;

import cotato.growingpain.domain.entity.JobApplication;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
    List<JobApplication> findByJobPostId(Long jobPostId);
    List<JobApplication> findByMemberIdAndApplicationCloseDate(Long memberId, String applicationCloseDate);
}
