package cotato.growingpain.service;

import cotato.growingpain.domain.entity.Comment;
import cotato.growingpain.domain.entity.ReplyComment;
import cotato.growingpain.dto.request.CommentRegisterRequest;
import cotato.growingpain.dto.response.CommentListResponse;
import cotato.growingpain.dto.response.CommentResponse;
import cotato.growingpain.infrastructure.repository.CommentLikeRepository;
import cotato.growingpain.infrastructure.repository.CommentRepository;
import cotato.growingpain.common.exception.AppException;
import cotato.growingpain.common.exception.ErrorCode;
import cotato.growingpain.domain.entity.Member;
import cotato.growingpain.infrastructure.repository.MemberRepository;
import cotato.growingpain.domain.entity.Post;
import cotato.growingpain.infrastructure.repository.PostRepository;
import cotato.growingpain.infrastructure.repository.ReplyCommentRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final ReplyCommentRepository replyCommentRepository;

    @Transactional
    public void registerComment(CommentRegisterRequest request, Long memberId, Long postId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new AppException(ErrorCode.MEMBER_NOT_FOUND));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));

        commentRepository.save(
                Comment.of(member, post, request.content())
        );
    }

    @Transactional(readOnly = true)
    public CommentListResponse getCommentsByMemberId(Long memberId) {
        List<CommentResponse> commentList = commentRepository.findByMemberId(memberId);
        return new CommentListResponse(commentList);
    }

    @Transactional(readOnly = true)
    public CommentListResponse getCommentsByPostId(Long postId) {
        List<CommentResponse> commentList = commentRepository.findByPostId(postId);
        return new CommentListResponse(commentList);
    }

    @Transactional(readOnly = true)
    public CommentListResponse getAllPostsAndCommentsByMemberId(Long memberId) {
        // 사용자가 작성한 모든 포스트 조회
        List<Post> posts = postRepository.findAllByMemberId(memberId);
        List<CommentResponse> commentList = new ArrayList<>();

        // 각 포스트의 댓글 조회
        for (Post post : posts) {
            List<CommentResponse> comments = commentRepository.findByPostId(post.getId());
            commentList.addAll(comments);
        }
        return new CommentListResponse(commentList);
    }

    @Transactional
    public void deleteComment(Long commentId, Long memberId) {
        Comment comment = commentRepository.findAllByIdAndMemberId(commentId, memberId)
                .orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_FOUND));

        List<ReplyComment> replyComments = replyCommentRepository.findReplyCommentByCommentId(commentId);
        replyCommentRepository.deleteAll(replyComments);

        commentLikeRepository.deleteByCommentId(commentId);
        commentRepository.delete(comment);
    }
}