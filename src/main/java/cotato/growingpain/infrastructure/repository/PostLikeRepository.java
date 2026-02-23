package cotato.growingpain.infrastructure.repository;

import cotato.growingpain.domain.entity.Member;
import cotato.growingpain.domain.entity.Post;
import cotato.growingpain.domain.entity.PostLike;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    boolean existsByMemberAndPost(Member member, Post post);

    @Modifying
    @Query(value = "delete from PostLike p where p.post.id=:postId")
    void deleteAllByPostId(Long postId);

    Optional<PostLike> findAllByMemberAndPost(Member member, Post post);

    List<PostLike> findAllByMemberId(Long memberId);
}