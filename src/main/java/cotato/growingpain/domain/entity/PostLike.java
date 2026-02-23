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
public class PostLike extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_like_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @JsonIgnore
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    @JsonIgnore
    private Post post;

    private PostLike(Member member, Post post) {
        this.member = member;
        this.post = post;
    }

    public static PostLike of(Member member, Post post) {
        return new PostLike(member, post);
    }

    public void increasePostLikeCount() {
        post.increaseLikeCount();
    }

    public void decreasePostLikeCount(Member member, Post post) {
        if (!member.getId().equals(this.member.getId())) {
            throw new AppException(ErrorCode.ACCESS_DENIED_USER);
        }

        if (!post.getId().equals(this.post.getId())) {
            throw new AppException(ErrorCode.POST_NOT_FOUND);
        }

        post.decreaseLikeCount();
    }
}