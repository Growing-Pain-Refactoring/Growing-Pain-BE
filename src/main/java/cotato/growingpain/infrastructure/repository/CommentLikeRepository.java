package cotato.growingpain.infrastructure.repository;

import cotato.growingpain.domain.entity.Comment;
import cotato.growingpain.domain.entity.CommentLike;
import cotato.growingpain.domain.entity.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    boolean existsByMemberAndComment(Member member, Comment comment);

    @Modifying
    @Query(value = "delete from CommentLike c where c.comment.id=:commentId")
    void deleteByCommentId(Long commentId);

    Optional<CommentLike> findAllByMemberAndComment(Member member, Comment comment);

    List<CommentLike> findAllByMemberId(Long memberId);
}