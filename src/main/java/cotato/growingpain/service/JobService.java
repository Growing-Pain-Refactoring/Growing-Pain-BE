package cotato.growingpain.service;

import cotato.growingpain.domain.entity.JobApplication;
import cotato.growingpain.domain.entity.JobPost;
import cotato.growingpain.dto.request.JobPostRequestDTO;
import cotato.growingpain.dto.request.JobPostRetrieveDTO;
import cotato.growingpain.dto.response.JobApplicationListResponse;
import cotato.growingpain.dto.response.JobApplicationResponse;
import cotato.growingpain.dto.retrieve.JobPostListRetrieveDTO;
import cotato.growingpain.infrastructure.repository.ApplicationDetailRepository;
import cotato.growingpain.infrastructure.repository.JobApplicationRepository;
import cotato.growingpain.infrastructure.repository.JobPostRepository;
import cotato.growingpain.domain.entity.Member;
import cotato.growingpain.infrastructure.repository.MemberRepository;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobService {

    private final JobPostRepository jobPostRepository;

    private final MemberRepository memberRepository;

    private final JobApplicationRepository jobApplicationRepository;

    private final ApplicationDetailRepository applicationDetailRepository;

    public void createJobPost(final JobPostRequestDTO jobPostRequest, Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found with ID: " + memberId));

        JobPost jobPost = jobPostRequest.toEntity(member);

        JobPost savedJobPost = jobPostRepository.save(jobPost);

        jobPost.getJobApplications().forEach(jobApplication -> {
            jobApplication.setJobPost(savedJobPost);
            JobApplication savedJobApplication = jobApplicationRepository.save(jobApplication);

            jobApplication.getApplicationDetails().forEach(applicationDetail -> {
                applicationDetail.setJobApplication(savedJobApplication);
                applicationDetailRepository.save(applicationDetail);
            });
        });

    }

    public List<JobPostListRetrieveDTO> jobPostRetrieveList(final Long memberId) {
        List<JobPost> jobPosts = jobPostRepository.findByMemberId(memberId);

        return jobPosts.stream()
                .map(JobPostListRetrieveDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public JobPostRetrieveDTO getJobPostById(final Long jobPostId) {
        JobPost jobPost = jobPostRepository.findById(jobPostId)
                .orElseThrow(() -> new RuntimeException("JobPost not found with ID: " + jobPostId));

        return JobPostRetrieveDTO.fromEntity(jobPost);
    }


    public void updateJobApplication(Long jobPostId, JobPostRequestDTO request, Long memberId) {
        JobPost jobPost = jobPostRepository.findById(jobPostId)
                .orElseThrow(() -> new RuntimeException("JobPost not found with ID: " + jobPostId));

        if (!jobPost.getMember().getId().equals(memberId)) {
            throw new RuntimeException("Member not authorized to update this JobPost");
        }

        jobPost.update(request, jobApplicationRepository, applicationDetailRepository);
        jobPostRepository.save(jobPost);

        log.info("Updated JobPost with ID: {}", jobPostId);

    }

    public void deleteJobPost(Long jobPostId, Long memberId) {
        JobPost jobPost = jobPostRepository.findById(jobPostId)
                .orElseThrow(() -> new RuntimeException("JobPost not found with ID" + jobPostId));

        jobPostRepository.delete(jobPost);
    }

    public JobApplicationListResponse getJobApplicationsByDaysLeft(Long memberId) {

        List<JobApplicationResponse> applications = new ArrayList<>();

        // 현재 날짜에서 daysLeft를 더한 날짜 계산
        int[] daysArray = {1, 3, 7};
        for (int daysLeft : daysArray) {
            LocalDate targetDate = LocalDate.now().plusDays(daysLeft);
            String formattedDate = targetDate.format(DateTimeFormatter.ISO_DATE);

            // 해당 날짜에 해당하는 JobApplication을 조회
            List<JobApplicationResponse> dayApplications = jobApplicationRepository.findByMemberIdAndApplicationCloseDate(
                            memberId, formattedDate)
                    .stream()
                    .map(JobApplicationResponse::new)  // 매핑 로직 간소화
                    .toList();
            applications.addAll(dayApplications);
        }

        // JobApplicationListResponse로 반환
        return new JobApplicationListResponse(applications);
    }
}
