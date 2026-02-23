package cotato.growingpain.dto;

import cotato.growingpain.domain.enums.ActivityCategory;
import cotato.growingpain.domain.entity.ActivityLog;
import cotato.growingpain.domain.entity.Member;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Builder
public record ActivityLogDTO(
        Long id,
        ActivityCategory activityCategory,
        String activityName,
        String content,
        String performance,
        String role,
        String activityDuration,
        String activityType,
        String url,
        int contribution
) {
    public static ActivityLogDTO fromEntity(ActivityLog activityLog) {
        return ActivityLogDTO.builder()
                .id(activityLog.getId())
                .activityName(activityLog.getActivityName())
                .activityCategory(activityLog.getActivityCategory())
                .activityDuration(activityLog.getActivityDuration())
                .content(activityLog.getContent())
                .performance(activityLog.getPerformance())
                .role(activityLog.getRole())
                .activityDuration(activityLog.getActivityDuration())
                .activityType(activityLog.getActivityType())
                .contribution(activityLog.getContribution())
                .url(activityLog.getUrl())
                .build();
    }

    public ActivityLog toEntity(Member member) {
        ActivityLog activityLog = ActivityLog.builder()
                .activityName(this.activityName)
                .activityCategory(this.activityCategory)
                .activityDuration(this.activityDuration)
                .content(this.content)
                .performance(this.performance)
                .role(this.role)
                .contribution(this.contribution)
                .activityType(this.activityType)
                .url(this.url)
                .member(member)
                .build();
        return activityLog;
    }
}
