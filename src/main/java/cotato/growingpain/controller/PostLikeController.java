package cotato.growingpain.controller;

import cotato.growingpain.common.Response;
import cotato.growingpain.domain.entity.Post;
import cotato.growingpain.dto.response.PostListResponse;
import cotato.growingpain.service.PostLikeService;
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

@Tag(name = "게시글 좋아요", description = "게시글 좋아요 관련된 api")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post/likes")
@Slf4j
public class PostLikeController {

    private final PostLikeService postLikeService;

    @Operation(summary = "게시글 좋아요 등록", description = "게시글 좋아요 등록을 위한 메소드")
    @ApiResponse(content = @Content(schema = @Schema(implementation = Response.class)))
    @PostMapping("/{postId}")
    @ResponseStatus(HttpStatus.OK)
    public Response<?> registerLike(@PathVariable Long postId,
                                    @AuthenticationPrincipal Long memberId) {

        postLikeService.registerLike(postId, memberId);
        log.info("게시글 {} 좋아요 한 memberId: {}", postId, memberId);
        return Response.createSuccessWithNoData("포스트 좋아요 등록 완료");
    }

    @Operation(summary = "게시글 좋아요 취소", description = "게시글 좋아요 취소를 위한 메소드")
    @ApiResponse(content = @Content(schema = @Schema(implementation = Response.class)))
    @DeleteMapping("/{postId}")
    @ResponseStatus(HttpStatus.OK)
    public Response<?> deleteLike(@PathVariable Long postId,
                                  @AuthenticationPrincipal Long memberId) {

        postLikeService.deleteLike(postId, memberId);
        return Response.createSuccessWithNoData("포스트 좋아요 취소 완료");
    }

    @Operation(summary = "좋아요 누른 게시글 목록 조회", description = "사용자가 좋아요를 누른 게시글의 목록을 조회하기 위한 메소드")
    @ApiResponse(content = @Content(schema = @Schema(implementation = Response.class)))
    @GetMapping("/{memberId}/list")
    @ResponseStatus(HttpStatus.OK)
    public Response<PostListResponse> getLikedPosts(@AuthenticationPrincipal Long memberId) {
        log.info("사용자가 좋아요를 누른 게시글 목록 요청: memberId {}", memberId);
        List<Post> likedPosts= postLikeService.getLikedPosts(memberId);
        PostListResponse postListResponse = PostListResponse.from(likedPosts);
        return Response.createSuccess("좋아요를 누른 게시글 목록 조회 완료", postListResponse);
    }
}