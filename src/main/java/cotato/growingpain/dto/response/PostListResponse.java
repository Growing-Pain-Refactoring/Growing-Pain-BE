package cotato.growingpain.dto.response;

import cotato.growingpain.domain.entity.Post;
import java.util.List;

public record PostListResponse(
        List<PostResponse> posts
) {
    public static PostListResponse from(List<Post> posts) {
        List<PostResponse> postResponses = posts.stream()
                .map(PostResponse::from)
                .toList();
        return new PostListResponse(postResponses);
    }
}
