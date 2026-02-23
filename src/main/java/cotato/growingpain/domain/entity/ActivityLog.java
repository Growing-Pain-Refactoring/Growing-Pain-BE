package cotato.growingpain.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import cotato.growingpain.common.domain.BaseTimeEntity;
import cotato.growingpain.domain.enums.ActivityCategory;
import cotato.growingpain.dto.ActivityLogDTO;
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
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Entity
@Getter
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ActivityLog extends BaseTimeEntity {
    /* -------------------------------------------- */
    /* -------------- Default Column -------------- */
    /* -------------------------------------------- */
    @Id
    @Column(name = "activity_log_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* -------------------------------------------- */
    /* ------------ Information Column ------------ */
    /* -------------------------------------------- */
    @Enumerated(EnumType.STRING)
    @Column(name = "activity_category")
    private ActivityCategory activityCategory;

    @Column(name = "activity_name")
    private String activityName;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "performance")
    private String performance;

    @Column(name = "role")
    private String role;

    @Column(name = "contribution")
    private int contribution;

    @Column(name = "activity_duration")
    private String activityDuration;

    @Column(name = "activity_type")
    private String activityType;

    @Column(name = "url")
    private String url;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime modifiedAt;

    /* -------------------------------------------- */
    /* -------------- Relation Column ------------- */
    /* -------------------------------------------- */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @JsonIgnore
    private Member member;

    @Builder
    public ActivityLog(
            ActivityCategory activityCategory,
            String activityName,
            String content,
            String performance,
            String role,
            String activityDuration,
            String activityType,
            String url,
            int contribution,
            Member member
    ) {
        // Relation Column
        this.member = member;

        // Information Column
        this.activityCategory = activityCategory;
        this.activityName = activityName;
        this.content = content;
        this.performance = performance;
        this.role = role;
        this.activityDuration = activityDuration;
        this.contribution = contribution;
        this.activityType = activityType;
        this.url = url;
    }

    public void updateFromDTO(ActivityLogDTO updatedActivityLogDTO) {
        this.activityCategory = updatedActivityLogDTO.activityCategory();
        this.activityName = updatedActivityLogDTO.activityName();
        this.content = updatedActivityLogDTO.content();
        this.performance = updatedActivityLogDTO.performance();
        this.role = updatedActivityLogDTO.role();
        this.activityDuration = updatedActivityLogDTO.activityDuration();
        this.activityType = updatedActivityLogDTO.activityType();
        this.url = updatedActivityLogDTO.url();
        this.contribution = updatedActivityLogDTO.contribution();
    }
}
