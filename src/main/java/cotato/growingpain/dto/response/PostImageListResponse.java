package cotato.growingpain.dto.response;

import cotato.growingpain.domain.entity.PostImage;
import java.util.List;

public record PostImageListResponse (
    List <PostImageResponse> postImages
) {
    public static PostImageListResponse from(List<PostImage> postImages) {
        List <PostImageResponse> postImageResponses = postImages.stream()
                .map(PostImageResponse::from)
                .toList();
        return new PostImageListResponse(postImageResponses);
    }
}
