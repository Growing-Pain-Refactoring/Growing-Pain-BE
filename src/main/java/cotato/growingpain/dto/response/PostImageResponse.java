package cotato.growingpain.dto.response;

import cotato.growingpain.domain.entity.PostImage;

public record PostImageResponse(

        Long postImageId,
        String imageUrl
) {
    public static PostImageResponse from(PostImage postImage) {
        return new PostImageResponse(
                postImage.getId(),
                postImage.getImageUrl()
        );
    }
}