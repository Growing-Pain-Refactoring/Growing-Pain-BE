package cotato.growingpain.infrastructure.repository;

import cotato.growingpain.domain.entity.CoverLetter;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoverLetterRepository extends JpaRepository<CoverLetter, Long> {

    List<CoverLetter> findByMemberId(Long memberId);
}
