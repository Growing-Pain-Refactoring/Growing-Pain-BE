package cotato.growingpain.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import cotato.growingpain.common.domain.BaseTimeEntity;
import cotato.growingpain.common.exception.AppException;
import cotato.growingpain.common.exception.ErrorCode;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Getter
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReplyCommentLike extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reply_comment_like_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @JsonIgnore
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    @JsonIgnore
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    @JsonIgnore
    private Comment comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reply_comment_id")
    @JsonIgnore
    private ReplyComment replyComment;


    public ReplyCommentLike(Member member, ReplyComment replyComment) {
        this.member = member;
        this.replyComment = replyComment;
    }

    public static ReplyCommentLike of(Member member,ReplyComment replyComment) {
        return new ReplyCommentLike(member, replyComment);
    }

    public void increaseReplyCommentLikeCount() {
        replyComment.increaseLikeCount();
    }

    public void decreaseReplyCommentLikeCount(Member member, ReplyComment replyComment) {
        if (!member.getId().equals(this.member.getId())) {
            throw new AppException(ErrorCode.ACCESS_DENIED_USER);
        }

        if (!replyComment.getId().equals(this.replyComment.getId())) {
            throw new AppException(ErrorCode.REPLY_COMMENT_NOT_FOUND);
        }

        replyComment.decreaseLikeCount();
    }
}