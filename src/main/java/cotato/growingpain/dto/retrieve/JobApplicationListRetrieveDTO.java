package cotato.growingpain.dto.retrieve;

import cotato.growingpain.domain.entity.JobApplication;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Builder
public record JobApplicationListRetrieveDTO(
        String applicationType,
        String status,
        String endDate
) {
    public static JobApplicationListRetrieveDTO fromEntity(JobApplication jobApplication) {
        String status = jobApplication.getResult().toString();

        if ("DOCUMENT".equals(jobApplication.getApplicationType().toString())) {
            status = jobApplication.getResult() != null ? jobApplication.getResult().toString() : null;
        } else if ("INTERVIEW".equals(jobApplication.getApplicationType().toString())) {
            status = jobApplication.getResult() != null ? jobApplication.getResult().toString() : null;
        }

        return JobApplicationListRetrieveDTO.builder()
                .applicationType(String.valueOf(jobApplication.getApplicationType()))
                .status(status)
                .endDate(jobApplication.getApplicationCloseDate())
                .build();
    }
}
