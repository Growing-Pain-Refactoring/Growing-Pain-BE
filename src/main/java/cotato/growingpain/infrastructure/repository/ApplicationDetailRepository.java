package cotato.growingpain.infrastructure.repository;

import cotato.growingpain.domain.entity.ApplicationDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationDetailRepository extends JpaRepository<ApplicationDetail, Long> {
}
