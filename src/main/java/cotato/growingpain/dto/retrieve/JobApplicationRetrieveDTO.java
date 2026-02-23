package cotato.growingpain.dto.retrieve;

import cotato.growingpain.domain.entity.JobApplication;
import cotato.growingpain.dto.ApplicationDetailDTO;
import java.util.List;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Builder
public record JobApplicationRetrieveDTO(
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
        List<ApplicationDetailDTO> applicationDetails
) {
    public static JobApplicationRetrieveDTO fromEntity(JobApplication jobApplication) {
        List<ApplicationDetailDTO> applicationDetailList = jobApplication.getApplicationDetails().stream()
                .map(ApplicationDetailDTO::fromEntity)
                .toList();

        return JobApplicationRetrieveDTO.builder()
                .id(jobApplication.getId())
                .applicationType(String.valueOf(jobApplication.getApplicationType()))
                .place(jobApplication.getPlace())
                .result(String.valueOf(jobApplication.getResult()))
                .submissionStatus(String.valueOf(jobApplication.isSubmissionStatus()))
                .resultStatus(String.valueOf(jobApplication.isResultStatus()))
                .applicationStartDate(jobApplication.getApplicationStartDate())
                .applicationCloseDate(jobApplication.getApplicationCloseDate())
                .applicationDetails(applicationDetailList)
                .build();
    }
}
