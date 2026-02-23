package cotato.growingpain.dto.response;

import cotato.growingpain.domain.entity.Comment;
import java.time.LocalDateTime;

public record CommentResponse(
        Long postId,
        Long commentId,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt,
        String content,
        Integer likeCount,
        Long memberId,
        String profileImageUrl,
        String memberNickname,
        String memberField
) {
    public static CommentResponse from(Comment comment) {
        return new CommentResponse(

                comment.getPost().getId(),
                comment.getId(),
                comment.getCreatedAt(),
                comment.getModifiedAt(),
                comment.getContent(),
                comment.getLikeCount(),
                comment.getMember().getId(),
                comment.getMember().getProfileImageUrl(),
                comment.getMember().getName(),
                comment.getMember().getField()
        );
    }
}