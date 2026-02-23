package cotato.growingpain.dto.response;

import cotato.growingpain.domain.entity.ReplyComment;
import java.util.List;

public record ReplyCommentListResponse(
        List<ReplyCommentResponse> replyCommentList
) {
    public static ReplyCommentListResponse from(List<ReplyComment> replyComments) {
        List<ReplyCommentResponse> replyCommentResponses = replyComments.stream()
                .map(ReplyCommentResponse::from)
                .toList();
        return new ReplyCommentListResponse(replyCommentResponses);
    }
}