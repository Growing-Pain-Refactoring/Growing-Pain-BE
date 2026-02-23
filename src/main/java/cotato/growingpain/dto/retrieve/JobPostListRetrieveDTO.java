package cotato.growingpain.dto.retrieve;

import cotato.growingpain.domain.entity.JobPost;
import java.util.List;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Builder
public record JobPostListRetrieveDTO(
        Long id,
        String companyName,
        String jobPart,
        List<JobApplicationListRetrieveDTO> jobApplications
) {
    public static JobPostListRetrieveDTO fromEntity(JobPost jobPost) {
        List<JobApplicationListRetrieveDTO> jobApplicationList = jobPost.getJobApplications().stream()
                .map(JobApplicationListRetrieveDTO::fromEntity)
                .toList();

        return JobPostListRetrieveDTO.builder()
                .id(jobPost.getId())
                .companyName(jobPost.getCompanyName())
                .jobPart(jobPost.getJobPart())
                .jobApplications(jobApplicationList)
                .build();
    }
}
