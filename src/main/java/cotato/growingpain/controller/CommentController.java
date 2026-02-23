package cotato.growingpain.controller;

import cotato.growingpain.dto.request.CommentRegisterRequest;
import cotato.growingpain.dto.response.CommentListResponse;
import cotato.growingpain.service.CommentService;
import cotato.growingpain.common.Response;
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

@Tag(name = "댓글", description = "댓글 또는 코멘트 관련된 api")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
@Slf4j
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "댓글 등록", description = "댓글 등록을 위한 메소드")
    @ApiResponse(content = @Content(schema = @Schema(implementation = Response.class)))
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public Response<?> registerComment(@Valid @RequestBody CommentRegisterRequest request,
                                       @RequestParam Long postId,
                                       @AuthenticationPrincipal Long memberId) {
        log.info("댓글 등록한 memberId: {}", memberId);
        log.info("댓글이 등록된 postId: {}", postId);
        commentService.registerComment(request, memberId, postId);
        return Response.createSuccessWithNoData("댓글 생성 완료");
    }

    @Operation(summary = "사용자별 댓글 목록 조회", description = "사용자가 작성한 모든 댓글 목록 조회를 위한 메소드")
    @ApiResponse(content = @Content(schema = @Schema(implementation = CommentListResponse.class)))
    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public Response<CommentListResponse> getCommentsByMemberId(@AuthenticationPrincipal Long memberId) {
        CommentListResponse commentListResponse = commentService.getCommentsByMemberId(memberId);
        log.info("{}가 작성한 댓글 목록", memberId);
        return Response.createSuccess("사용자 댓글 목록 조회 완료", commentListResponse);
    }

    @Operation(summary = "포스트별 댓글 목록 조회", description = "한 포스트내의 댓글 목록 조회를 위한 메소드")
    @ApiResponse(content = @Content(schema = @Schema(implementation = CommentListResponse.class)))
    @GetMapping("/{postId}")
    @ResponseStatus(HttpStatus.OK)
    public Response<CommentListResponse> getCommentsByPostId(@PathVariable Long postId) {
        CommentListResponse commentListResponse = commentService.getCommentsByPostId(postId);
        return Response.createSuccess("포스트별 댓글 목록 조회 완료", commentListResponse);
    }

    @Operation(summary = "사용자가 작성한 포스트내의 모든 댓글 목록 조회", description = "사용자가 작성한 포스트내의 모든 댓글 목록 조회를 위한 메소드")
    @ApiResponse(content = @Content(schema = @Schema(implementation = CommentListResponse.class)))
    @GetMapping("/member")
    @ResponseStatus(HttpStatus.OK)
    public Response<CommentListResponse> getAllPostsAndCommentsByMemberId(@AuthenticationPrincipal Long memberId) {
        CommentListResponse commentListResponse = commentService.getAllPostsAndCommentsByMemberId(memberId);
        return Response.createSuccess("포스트별 댓글 목록 조회 완료", commentListResponse);
    }

    @Operation(summary = "댓글 삭제", description = "댓글 삭제를 위한 메소드")
    @ApiResponse(content = @Content(schema = @Schema(implementation = Response.class)))
    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public Response<?> deleteComment(@PathVariable Long commentId,
                                  @AuthenticationPrincipal Long memberId) {
        log.info("댓글 삭제한 memberId: {}", memberId);
        commentService.deleteComment(commentId, memberId);
        return Response.createSuccessWithNoData("댓글 삭제 완료");
    }
}