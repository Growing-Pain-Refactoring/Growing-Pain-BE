package cotato.growingpain.dto.response;

import cotato.growingpain.domain.entity.Comment;
import java.util.List;

public record CommentListResponse(
        List<CommentResponse> commentList
) {
    public static CommentListResponse from(List<Comment> comments) {
        List<CommentResponse> commentResponses = comments.stream()
                .map(CommentResponse::from)
                .toList();
        return new CommentListResponse(commentResponses);
    }
}