package cotato.growingpain.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import cotato.growingpain.common.domain.BaseTimeEntity;
import cotato.growingpain.common.exception.AppException;
import cotato.growingpain.common.exception.ErrorCode;
import cotato.growingpain.domain.enums.PostCategory;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
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
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    private String title;

    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "parent_post_category")
    private PostCategory parentCategory;

    @Enumerated(EnumType.STRING)
    @Column(name = "sub_post_category")
    private PostCategory subCategory;

    private int likeCount = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @JsonIgnore
    private Member member;

    @OneToMany(mappedBy = "post")
    @JsonIgnore
    private List<PostLike> postLikes = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    @JsonIgnore
    private List<PostSave> postSaves = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    @JsonIgnore
    private List<Comment> comments = new ArrayList<>();

    private Post(Member member, String title, String content, PostCategory parentCategory, PostCategory subCategory) {
        this.member = member;
        this.title = title;
        this.content = content;
        this.parentCategory = parentCategory;
        this.subCategory = subCategory;
    }

    public static Post of(Member member, String title, String content, PostCategory parentCategory, PostCategory subCategory) {
        return new Post(member, title, content, parentCategory, subCategory);
    }

    public void increaseLikeCount(){
        this.likeCount++;
    }

    public void decreaseLikeCount() {
        if (this.likeCount > 0) {
            this.likeCount--;
        }
    }

    public void validatePostLike(Member member) {
        if (member.getId().equals(this.member.getId())) {
            throw new AppException(ErrorCode.CANNOT_LIKE_OWN_POST);
        }
    }

    public void updatePost(String title, String content, PostCategory category) {
        this.title = title;
        this.content = content;
        this.subCategory = category;
        this.parentCategory = category.getParent();
        this.modifiedAt = LocalDateTime.now();
    }
}