package cotato.growingpain.service;

import cotato.growingpain.domain.entity.Comment;
import cotato.growingpain.domain.entity.CommentLike;
import cotato.growingpain.infrastructure.repository.CommentLikeRepository;
import cotato.growingpain.infrastructure.repository.CommentRepository;
import cotato.growingpain.common.exception.AppException;
import cotato.growingpain.common.exception.ErrorCode;
import cotato.growingpain.domain.entity.Member;
import cotato.growingpain.infrastructure.repository.MemberRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentLikeService {

    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void registerLike(Long commentId, Long memberId) {
        Comment comment = findCommentId(commentId);
        Member member = findMemberById(memberId);
        comment.validateCommentLike(member);

        if (commentLikeRepository.existsByMemberAndComment(member, comment)) {
            log.info("이미 좋아요를 누른 댓글입니다: commentId={}, memberId={}", commentId, memberId);
            throw new AppException(ErrorCode.ALREADY_LIKED);
        }

        CommentLike commentLike = CommentLike.of(member, comment);
        commentLike.increaseCommentLikeCount();

        commentLikeRepository.save(commentLike);
    }

    @Transactional
    public void deleteLike(Long commentId, Long memberId) {

        Comment comment = findCommentId(commentId);
        Member member = findMemberById(memberId);
        CommentLike commentLike = commentLikeRepository.findAllByMemberAndComment(member, comment)
                .orElseThrow(() -> new AppException(ErrorCode.COMMENT_LIKE_NOT_FOUND));

        commentLike.decreaseCommentLikeCount(member, comment);
        commentLikeRepository.delete(commentLike);
    }

    @Transactional
    public List<Comment> getLikedComments(Long memberId) {
        List<CommentLike> commentLikes = commentLikeRepository.findAllByMemberId(memberId);

        return commentLikes.stream()
                .map(CommentLike::getComment)
                .toList();
    }

    private Comment findCommentId(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_FOUND));
    }

    private Member findMemberById(Long memberId) {
        return memberRepository.getReferenceById(memberId);
    }
}
