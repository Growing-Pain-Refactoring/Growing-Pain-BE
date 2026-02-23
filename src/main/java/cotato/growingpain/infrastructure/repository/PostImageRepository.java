package cotato.growingpain.infrastructure.repository;

import cotato.growingpain.domain.entity.PostImage;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostImageRepository extends JpaRepository<PostImage, Long> {
    void deleteAllByPostId(Long postId);

    List<PostImage> findAllByPostId(Long postId);
}
