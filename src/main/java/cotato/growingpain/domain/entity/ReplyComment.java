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
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Getter
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReplyComment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reply_comment_id")
    private Long id;

    private String content;

    private int likeCount = 0;

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

    @OneToMany(mappedBy = "replyComment")
    @JsonIgnore
    private List<ReplyCommentLike> replyCommentLikes = new ArrayList<>();

    private ReplyComment(Member member, Post post, Comment comment, String content) {
        this.member = member;
        this.post = post;
        this.comment = comment;
        this.content = content;
    }

    public static ReplyComment of (Member member, Post post, Comment comment, String content) {
        return new ReplyComment(member, post, comment, content);
    }

    public void increaseLikeCount(){
        this.likeCount++;
    }

    public void decreaseLikeCount() {
        if (this.likeCount > 0) {
            this.likeCount--;
        }
    }

    public void validateReplyCommentLike(Member member) {
        if (member.getId().equals(this.member.getId())) {
            throw new AppException(ErrorCode.CANNOT_LIKE_OWN_REPLY_COMMENT);
        }
    }
}