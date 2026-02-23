package cotato.growingpain.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import cotato.growingpain.common.domain.BaseTimeEntity;
import cotato.growingpain.domain.enums.MemberJob;
import cotato.growingpain.domain.enums.MemberProfileShowing;
import cotato.growingpain.domain.enums.MemberRole;
import cotato.growingpain.infrastructure.security.oauth.AuthProvider;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Entity
@AllArgsConstructor
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(name = "member_name")   //닉네임
    private String name;

    @Email
    @Column(name = "member_email")
    private String email;

    @Column(name = "member_profile_showing")
    @Enumerated(EnumType.STRING)
    @ColumnDefault(value = "'PUBLIC'")
    private MemberProfileShowing memberProfileShowing;

    @Column(name = "member_field")  //분야
    private String field;

    @Column(name = "member_belong") //소속
    private String belong;

    @Enumerated(EnumType.STRING)    //소속 중 직업
    @Column(name = "member_job")
    private MemberJob job;

    @Column(name = "member_role")
    @Enumerated(EnumType.STRING)
    private MemberRole memberRole;

    @Column(name = "profile_image_url") // 프로필 사진 URL
    private String profileImageUrl;

    @Column(name = "oauth_id")
    private String oauth2Id;

    @Column(name = "auth_provider")
    @Enumerated(EnumType.STRING)
    private AuthProvider authProvider;

    @Column(name = "education_background")
    private String educationBackground;

    @Column(name = "skill")
    private String skill;

    @Column(name = "activity_history")
    private String activityHistory;

    @Column(name = "award")
    private String award;

    @Column(name = "language_score")
    private String languageScore;

    @Column(name = "career")
    private String career;

    @Column(name = "about_me")
    private String aboutMe;

    @OneToMany(mappedBy = "member")
    @JsonIgnore
    private List<JobApplication> jobApplications = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    @JsonIgnore
    private List<ActivityLog> activityLogs = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    @JsonIgnore
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    @JsonIgnore
    private List<PostLike> postLikes = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    @JsonIgnore
    private List<PostSave> postSaves = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    @JsonIgnore
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    @JsonIgnore
    private List<CommentLike> commentLikes = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    @JsonIgnore
    private List<ReplyComment> replyComments = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    @JsonIgnore
    private List<ReplyCommentLike> replyCommentLikes = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    @JsonIgnore
    private List<JobPost> jobPosts = new ArrayList<>();

    @Column(nullable = false)
    private boolean deleted = false;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime modifiedAt;

    public void updateMemberInfo(String name, String field, String belong, MemberJob job) {
        this.name = name;
        this.field = field;
        this.belong = belong;
        this.job = job;
        this.modifiedAt = LocalDateTime.now();
    }

    public void updateRole(MemberRole memberRole) {
        this.memberRole = memberRole;
        this.modifiedAt = LocalDateTime.now();
    }

    public void updateDefaultInfo(String field, String belong, MemberJob job, String educationBackground, String skill,
                                  String activityHistory, String award,
                                  String languageScore) {
        this.field = field;
        this.belong = belong;
        this.job = job;
        this.educationBackground = educationBackground;
        this.skill = skill;
        this.activityHistory = activityHistory;
        this.award = award;
        this.languageScore = languageScore;
        this.modifiedAt = LocalDateTime.now();
    }

    public void updateAdditionalInfo(String career, String aboutMe) {
        this.career = career;
        this.aboutMe = aboutMe;
        this.modifiedAt = LocalDateTime.now();
    }

    public void updateProfilePublic(MemberProfileShowing memberProfileShowing) {
        this.memberProfileShowing = memberProfileShowing;
        this.modifiedAt = LocalDateTime.now();
    }

    public void updateProfileImage(String imageUrl) {
        this.profileImageUrl = imageUrl;
        this.modifiedAt = LocalDateTime.now();
    }
}
