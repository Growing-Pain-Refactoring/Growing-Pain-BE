package cotato.growingpain.controller;

import static cotato.growingpain.common.Response.createSuccess;

import cotato.growingpain.common.Response;
import cotato.growingpain.dto.ActivityLogDTO;
import cotato.growingpain.dto.request.ActivityLogRequestDTO;
import cotato.growingpain.service.ActivityLogService;
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
@RequestMapping("/api/activity-logs")
@RequiredArgsConstructor
public class ActivityLogController {
    private final ActivityLogService activityLogService;

    @Operation(summary = "활동 기록 저장", description = "활동 기록을 등록하기 위한 메소드")
    @ApiResponse(content = @Content(schema = @Schema(implementation = Response.class)))
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public Response<ActivityLogRequestDTO> createJobPost(
            @RequestBody @Valid ActivityLogRequestDTO activityLogRequestDTO,
            @AuthenticationPrincipal Long memberId) {
        activityLogService.createActivityLog(activityLogRequestDTO, memberId);
        return createSuccess("활동 기록 등록 완료", null);
    }

    @Operation(summary = "활동 기록 리스트 조회", description = "활동 기록 리스트를 조회하기 위한 메소드")
    @ApiResponse(content = @Content(schema = @Schema(implementation = Response.class)))
    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public Response<List<ActivityLogRequestDTO>> retrieveActivityLogList(
            @AuthenticationPrincipal Long memberId) {
        List<ActivityLogRequestDTO> activityLogList = activityLogService.retrieveActivityLogsByMemberId(memberId);
        return createSuccess("활동 기록 조회 완료", activityLogList);
    }

    @Operation(summary = "활동 기록 상세 조회", description = "활동 기록을 상세 조회하기 위한 메소드")
    @ApiResponse(content = @Content(schema = @Schema(implementation = Response.class)))
    @GetMapping("/{activityLogId}")
    @ResponseStatus(HttpStatus.OK)
    public Response<ActivityLogDTO> retrieveActivityLogById(
            @PathVariable Long activityLogId) {
        ActivityLogDTO activityLogDTO = activityLogService.retrieveActivityLogById(activityLogId);
        return Response.createSuccess("활동 기록 상세 조회 완료", activityLogDTO);
    }

    @Operation(summary = "활동 기록 수정", description = "활동 기록을 수정하기 위한 메소드")
    @ApiResponse(content = @Content(schema = @Schema(implementation = Response.class)))
    @PatchMapping("/{activityLogId}")
    @ResponseStatus(HttpStatus.OK)
    public Response<ActivityLogDTO> updateActivityLog(@PathVariable Long activityLogId,
                                                      @RequestBody ActivityLogDTO updatedActivityLogDTO) {
        ActivityLogDTO updatedActivityLog = activityLogService.updateActivityLog(activityLogId, updatedActivityLogDTO);
        return Response.createSuccess("활동 기록 수정 완료", updatedActivityLog);
    }

    @Operation(summary = "활동 기록 삭제", description = "활동 기록을 삭제하기 위한 메소드")
    @ApiResponse(content = @Content(schema = @Schema(implementation = Response.class)))
    @DeleteMapping("/{activityLogId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Response<?> deleteActivityLog(@PathVariable Long activityLogId,
                                         @AuthenticationPrincipal Long memberId) {
        activityLogService.deleteActivityLog(activityLogId, memberId);

        return Response.createSuccess("활동 기록 삭제 완료", null);
    }
}
