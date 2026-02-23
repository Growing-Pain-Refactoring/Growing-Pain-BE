package cotato.growingpain.controller;

import cotato.growingpain.common.Response;
import cotato.growingpain.domain.entity.JobPost;
import cotato.growingpain.dto.request.JobPostRequestDTO;
import cotato.growingpain.dto.request.JobPostRetrieveDTO;
import cotato.growingpain.dto.response.JobApplicationListResponse;
import cotato.growingpain.dto.retrieve.JobPostListRetrieveDTO;
import cotato.growingpain.service.JobService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/job-posts")
@RequiredArgsConstructor
public class JobPostController {

    private final JobService jobService;

    @Operation(summary = "지원 현황 등록", description = "지원 현황을 등록하기 위한 메소드")
    @ApiResponse(content = @Content(schema = @Schema(implementation = Response.class)))
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public Response<JobPost> createJobPost(@RequestBody @Valid JobPostRequestDTO request,
                                           @AuthenticationPrincipal Long memberId) {
        jobService.createJobPost(request, memberId);
        return Response.createSuccess("지원 현황 등록 완료", null);
    }

    @Operation(summary = "지원 현황 리스트 조회", description = "지원 현황 리스트를 조회하기 위한 메소드")
    @ApiResponse(content = @Content(schema = @Schema(implementation = Response.class)))
    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public Response<List<JobPostListRetrieveDTO>> getJobPosts(
            @AuthenticationPrincipal Long memberId) {
        List<JobPostListRetrieveDTO> retrievedJobPostList = jobService.jobPostRetrieveList(memberId);
        return Response.createSuccess("지원 현황 리스트 조회 완료", retrievedJobPostList);
    }

    @Operation(summary = "지원 현황 상세 조회", description = "지원 현황 상세 정보를 조회하기 위한 메소드")
    @ApiResponse(content = @Content(schema = @Schema(implementation = Response.class)))
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{jobPostId}")
    public Response<JobPostRetrieveDTO> getJobPostById(@PathVariable Long jobPostId) {

        JobPostRetrieveDTO retrievedJobPost = jobService.getJobPostById(jobPostId);
        return Response.createSuccess("지원 현황 상세 조회 완료", retrievedJobPost);
    }

    @Operation(summary = "지원 현황 내용 수정", description = "지원 현황을 수정하기 위한 메소드")
    @ApiResponse(content = @Content(schema = @Schema(implementation = Response.class)))
    @PatchMapping("/{jobPostId}")
    @ResponseStatus(HttpStatus.OK)
    public Response<JobPost> updateJobPost(
            @PathVariable Long jobPostId,
            @AuthenticationPrincipal Long memberId,
            @RequestBody JobPostRequestDTO jobPostRequestDTO) {
        jobService.updateJobApplication(jobPostId, jobPostRequestDTO, memberId);

        return Response.createSuccess("지원 현황 수정 완료", null);
    }

    @Operation(summary = "지원 현황 내용 삭제", description = "지원 현황을 삭제하기 위한 메소드")
    @ApiResponse(content = @Content(schema = @Schema(implementation = Response.class)))
    @DeleteMapping("/{jobPostId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Response<JobPost> updateJobPost(
            @PathVariable Long jobPostId,
            @AuthenticationPrincipal Long memberId) {
        jobService.deleteJobPost(jobPostId, memberId);

        return Response.createSuccess("지원 현황 삭제 완료", null);
    }

    @Operation(summary = "일정 기간이 남은 지원 현황 조회", description = "일정 기간이 남은 지원 현황을 조회하여 반환하는 메소드")
    @ApiResponse(content = @Content(schema = @Schema(implementation = JobApplicationListResponse.class)))
    @GetMapping("/days-left")
    @ResponseStatus(HttpStatus.OK)
    public Response<JobApplicationListResponse> getJobApplicationsByDaysLeft( @AuthenticationPrincipal Long memberId) {
        JobApplicationListResponse jobApplications = jobService.getJobApplicationsByDaysLeft(memberId);
        return Response.createSuccess("1일, 3일, 7일 남은 지원 현황 조회 완료", jobApplications);
    }
}
