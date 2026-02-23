package cotato.growingpain.dto.request;

import cotato.growingpain.domain.entity.ApplicationDetail;
import cotato.growingpain.domain.entity.JobApplication;

public record ApplicationDetailRequestDTO(
        Long id,
        String title,
        String content) {

    public ApplicationDetail toEntity(JobApplication jobApplication) {
        return ApplicationDetail.builder()
                .title(this.title)
                .content(this.content)
                .jobApplication(jobApplication)
                .build();
    }

}
