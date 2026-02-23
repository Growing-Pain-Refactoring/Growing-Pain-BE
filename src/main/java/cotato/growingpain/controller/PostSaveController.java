package cotato.growingpain.controller;

import cotato.growingpain.common.Response;
import cotato.growingpain.domain.entity.Post;
import cotato.growingpain.dto.response.PostListResponse;
import cotato.growingpain.service.PostSaveService;
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

@Tag(name = "게시글 저장", description = "게시글 저장 관련된 api")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post/saves")
@Slf4j
public class PostSaveController {

    private final PostSaveService postSaveService;

    @Operation(summary = "게시글 저장", description = "게시글을 저장하기 위한 메소드")
    @ApiResponse(content = @Content(schema = @Schema(implementation = Response.class)))
    @PostMapping("/{postId}")
    @ResponseStatus(HttpStatus.OK)
    public Response<?> savePost(@PathVariable Long postId,
                                @AuthenticationPrincipal Long memberId) {
        log.info("게시글 {} 저장한 memberId: {}", postId, memberId);
        postSaveService.savePost(postId, memberId);
        return Response.createSuccessWithNoData("게시글 저장 완료");
    }

    @Operation(summary = "게시글 저장 취소", description = "게시글 저장을 취소하기 위한 메소드")
    @ApiResponse(content = @Content(schema = @Schema(implementation = Response.class)))
    @DeleteMapping("/{postId}")
    @ResponseStatus(HttpStatus.OK)
    public Response<?> deleteSavePost(@PathVariable Long postId,
                                      @AuthenticationPrincipal Long memberId) {
        log.info("게시글 {} 저장 취소한 memberId: {}", postId, memberId);
        postSaveService.deleteSavePost(postId, memberId);
        return Response.createSuccessWithNoData("게시글 저장 취소 완료");
    }

    @Operation(summary = "저장한 게시글 목록 조회", description = "사용자가 저장한 게시글의 목록을 조회하기 위한 메소드")
    @ApiResponse(content = @Content(schema = @Schema(implementation = Response.class)))
    @GetMapping("/{memberId}/list")
    @ResponseStatus(HttpStatus.OK)
    public Response<PostListResponse> getSavedPosts(@AuthenticationPrincipal Long memberId) {
        log.info("사용자가 저장한 게시글 목록 요청: memberId {}", memberId);
        List<Post> savedPosts= postSaveService.getSavedPosts(memberId);
        PostListResponse postListResponse = PostListResponse.from(savedPosts);
        return Response.createSuccess("저장한 게시글 목록 조회 완료", postListResponse);
    }
}