package cotato.growingpain.infrastructure.repository;

import cotato.growingpain.domain.enums.PostCategory;
import cotato.growingpain.domain.entity.Post;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByMemberId(Long memberId);

    @Query("SELECT p FROM Post p WHERE p.parentCategory = :category OR p.subCategory = :category")
    List<Post> findAllByCategory(@Param("category") PostCategory category);

    @Query("SELECT p FROM Post p")
    List<Post> findAll();

    Optional<Post> findAllByIdAndMemberId(Long postId, Long memberId);
}