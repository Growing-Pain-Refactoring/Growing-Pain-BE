package cotato.growingpain.controller;

import cotato.growingpain.domain.entity.Comment;
import cotato.growingpain.dto.response.CommentListResponse;
import cotato.growingpain.service.CommentLikeService;
import cotato.growingpain.common.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "댓글 좋아요", description = "댓글 좋아요 관련된 api")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment/likes")
@Slf4j
public class CommentLikeController {

    private final CommentLikeService commentLikeService;

    @Operation(summary = "댓글 좋아요 등록", description = "댓글 좋아요 등록을 위한 메소드")
    @ApiResponse(content = @Content(schema = @Schema(implementation = Response.class)))
    @PostMapping("/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public Response<?> registerLike(@PathVariable Long commentId,
                                    @AuthenticationPrincipal Long memberId) {

        commentLikeService.registerLike(commentId, memberId);
        log.info("댓글 {} 좋아요 한 memberId: {}", commentId, memberId);
        return Response.createSuccessWithNoData("댓글 좋아요 등록 완료");
    }

    @Operation(summary = "댓글 좋아요 취소", description = "댓글 좋아요 취소를 위한 메소드")
    @ApiResponse(content = @Content(schema = @Schema(implementation = Response.class)))
    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public Response<?> deleteLike(@PathVariable Long commentId,
                                  @AuthenticationPrincipal Long memberId) {

        commentLikeService.deleteLike(commentId, memberId);
        return Response.createSuccessWithNoData("댓글 좋아요 취소 완료");
    }

    @Operation(summary = "좋아요 누른 댓글 목록 조회", description = "사용자가 좋아요를 누른 댓글의 목록을 조회하기 위한 메소드")
    @ApiResponse(content = @Content(schema = @Schema(implementation = Response.class)))
    @GetMapping("/{memberId}/list")
    @ResponseStatus(HttpStatus.OK)
    public Response<CommentListResponse> getLikeComments(@AuthenticationPrincipal Long memberId) {
        log.info("사용자가 좋아요를 누른 댓글 목록 요청: memberId {}", memberId);
        List<Comment> likedComments = commentLikeService.getLikedComments(memberId);
        CommentListResponse commentListResponse = CommentListResponse.from(likedComments);
        return Response.createSuccess("좋아요를 누른 댓글 목록 조회 완료", commentListResponse);
    }
}