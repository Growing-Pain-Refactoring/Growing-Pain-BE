package cotato.growingpain.dto.response;

import cotato.growingpain.domain.entity.JobApplication;

public record JobApplicationResponse(

        Long jobPostId,
        Long jobApplicationId,
        String companyName,
        String applicationType,
        String applicationCloseDate
) {
    public JobApplicationResponse(JobApplication jobApplication) {
        this(
                jobApplication.getJobPost().getId(),
                jobApplication.getId(),
                jobApplication.getJobPost().getCompanyName(),
                jobApplication.getApplicationType().name(),
                jobApplication.getApplicationCloseDate()
        );
    }
}
