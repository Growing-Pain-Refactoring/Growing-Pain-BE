package cotato.growingpain.controller;

import cotato.growingpain.common.Response;
import cotato.growingpain.dto.request.ReplyCommentRegisterRequest;
import cotato.growingpain.dto.response.ReplyCommentListResponse;
import cotato.growingpain.service.ReplyCommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "답글", description = "답글 또는 대댓글 관련된 api")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reply-comment")
@Slf4j
public class ReplyCommentController {

    private final ReplyCommentService replyCommentService;

    @Operation(summary = "답글 등록", description = "답글 등록을 위한 메소드")
    @ApiResponse(content = @Content(schema = @Schema(implementation = Response.class)))
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public Response<?> registerComment(@Valid @RequestBody ReplyCommentRegisterRequest request,
                                       @RequestParam Long postId,
                                       @RequestParam Long commentId,
                                       @AuthenticationPrincipal Long memberId) {
        log.info("답글 등록한 memberId: {}", memberId);
        log.info("답글이 등록된 postId: {}", postId);
        log.info("답글이 등록된 commentId: {}", commentId);
        replyCommentService.registerReplyComment(request, postId, commentId,memberId);
        return Response.createSuccessWithNoData("딥글 생성 완료");
    }

    @Operation(summary = "댓글별 답글 목록 조회", description = "한 댓글내의 답글 목록 조회를 위한 메소드")
    @ApiResponse(content = @Content(schema = @Schema(implementation = ReplyCommentListResponse.class)))
    @GetMapping("/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public Response<ReplyCommentListResponse> getCommentsByPostId(@PathVariable Long commentId) {
        ReplyCommentListResponse replyCommentListResponse = replyCommentService.getReplyCommentsByCommentId(commentId);
        return Response.createSuccess("댓글별 답글 목록 조회 완료", replyCommentListResponse);
    }

    @Operation(summary = "답글 삭제", description = "답글 삭제를 위한 메소드")
    @ApiResponse(content = @Content(schema = @Schema(implementation = Response.class)))
    @DeleteMapping("/{replyCommentId}")
    @ResponseStatus(HttpStatus.OK)
    public Response<?> deleteReplyComment(@PathVariable Long replyCommentId,
                                     @AuthenticationPrincipal Long memberId) {
        log.info("답글 삭제한 memberId: {}", memberId);
        replyCommentService.deleteReplyComment(replyCommentId, memberId);
        return Response.createSuccessWithNoData("답글 삭제 완료");
    }
}