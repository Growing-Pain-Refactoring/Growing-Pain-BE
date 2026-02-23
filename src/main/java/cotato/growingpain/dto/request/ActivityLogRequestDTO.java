package cotato.growingpain.dto.request;

import cotato.growingpain.domain.enums.ActivityCategory;
import cotato.growingpain.domain.entity.ActivityLog;
import cotato.growingpain.domain.entity.Member;

public record ActivityLogRequestDTO(
        Long id,
        ActivityCategory activityCategory,
        String activityName,
        String role,
        String activityDuration,
        String activityType,
        String url,
        int contribution
) {
    public ActivityLog toEntity(Member member) {
        ActivityLog activityLog = ActivityLog.builder()
                .activityName(this.activityName)
                .activityCategory(this.activityCategory)
                .activityDuration(this.activityDuration)
                .role(this.role)
                .contribution(this.contribution)
                .activityType(this.activityType)
                .url(this.url)
                .member(member)
                .build();
        return activityLog;
    }
}
