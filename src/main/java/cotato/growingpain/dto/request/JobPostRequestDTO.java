package cotato.growingpain.dto.request;

import cotato.growingpain.domain.entity.JobApplication;
import cotato.growingpain.domain.entity.JobPost;
import cotato.growingpain.domain.entity.Member;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public record JobPostRequestDTO(
        @NotBlank String companyName,
        @NotBlank String jobPart,
        List<JobApplicationRequestDTO> jobApplications
) {

    public JobPost toEntity(Member member) {
        JobPost jobPost = JobPost.builder()
                .member(member)
                .companyName(companyName)
                .jobPart(jobPart)
                .jobApplications(new ArrayList<>())
                .build();

        log.info("JobPost created {}", jobPost);
        log.info("JobPost companyName {}", jobPost.getCompanyName());

        List<JobApplication> applications = jobApplications.stream()
                .map(JobApplicationRequestDTO -> JobApplicationRequestDTO.toEntity(member, jobPost))
                .toList();

        applications.forEach(jobPost::addJobApplication);

        return jobPost;
    }


}
