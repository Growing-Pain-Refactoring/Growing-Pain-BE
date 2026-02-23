package cotato.growingpain.dto.request;

import cotato.growingpain.domain.enums.ApplicationType;
import cotato.growingpain.domain.enums.Result;
import cotato.growingpain.domain.entity.ApplicationDetail;
import cotato.growingpain.domain.entity.JobApplication;
import cotato.growingpain.domain.entity.JobPost;
import cotato.growingpain.domain.entity.Member;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public record JobApplicationRequestDTO(
        Long id,
        String applicationType,
        String place,
        String result,
        String submissionStatus,
        String resultStatus,
        String applicationStartDate,
        String applicationCloseDate,
        Long memberId,
        Long jobPostId,
        List<ApplicationDetailRequestDTO> applicationDetails
) {
    public JobApplication toEntity(Member member, JobPost jobPost) {
        JobApplication jobApplication = JobApplication.builder()
                .applicationType(ApplicationType.valueOf(this.applicationType))
                .place(this.place)
                .result(Result.valueOf(this.result))
                .applicationStartDate(this.applicationStartDate)
                .applicationCloseDate(this.applicationCloseDate)
                .member(member)
                .jobPost(jobPost)
                .applicationDetails(new ArrayList<>())
                .build();

        log.info("jobApplication {}", jobApplication);

        List<ApplicationDetail> details = this.applicationDetails.stream()
                .map(ApplicationDetailRequestDTO -> ApplicationDetailRequestDTO.toEntity(jobApplication))
                .toList();
        details.forEach(jobApplication::addApplicationDetail);

        return jobApplication;
    }
}
