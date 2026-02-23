package cotato.growingpain.dto.response;

import cotato.growingpain.domain.entity.ReplyComment;
import java.time.LocalDateTime;

public record ReplyCommentResponse(
        Long commentId,
        Long replyCommentId,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt,
        String content,
        Integer likeCount,
        Long memberId,
        String profileImageUrl,
        String memberNickname,
        String memberField

) {
    public static ReplyCommentResponse from(ReplyComment replyComment) {
        return new ReplyCommentResponse(

                replyComment.getComment().getId(),
                replyComment.getId(),
                replyComment.getCreatedAt(),
                replyComment.getModifiedAt(),
                replyComment.getContent(),
                replyComment.getLikeCount(),
                replyComment.getMember().getId(),
                replyComment.getMember().getProfileImageUrl(),
                replyComment.getMember().getName(),
                replyComment.getMember().getField()
        );
    }
}
