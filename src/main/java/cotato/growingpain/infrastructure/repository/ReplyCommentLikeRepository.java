package cotato.growingpain.infrastructure.repository;

import cotato.growingpain.domain.entity.Member;
import cotato.growingpain.domain.entity.ReplyComment;
import cotato.growingpain.domain.entity.ReplyCommentLike;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ReplyCommentLikeRepository extends JpaRepository<ReplyCommentLike, Long> {
    boolean existsByMemberAndReplyComment(Member member, ReplyComment replyComment);

    @Modifying
    @Query(value = "delete from ReplyCommentLike r where r.replyComment.id=:replyCommentId")
    void deleteByReplyCommentId(Long replyCommentId);


    Optional<ReplyCommentLike> findAllByMemberAndReplyComment(Member member, ReplyComment replyComment);
    List<ReplyCommentLike> findAllByMemberId(Long memberId);
}