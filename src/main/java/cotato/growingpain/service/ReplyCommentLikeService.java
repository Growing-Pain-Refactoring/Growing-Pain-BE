package cotato.growingpain.service;

import cotato.growingpain.common.exception.AppException;
import cotato.growingpain.common.exception.ErrorCode;
import cotato.growingpain.domain.entity.Member;
import cotato.growingpain.infrastructure.repository.MemberRepository;
import cotato.growingpain.domain.entity.ReplyComment;
import cotato.growingpain.domain.entity.ReplyCommentLike;
import cotato.growingpain.infrastructure.repository.ReplyCommentLikeRepository;
import cotato.growingpain.infrastructure.repository.ReplyCommentRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReplyCommentLikeService {

    private final ReplyCommentRepository replyCommentRepository;
    private final ReplyCommentLikeRepository replyCommentLikeRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void registerLike(Long replyCommentId, Long memberId) {
        ReplyComment replyComment = findReplyCommentById(replyCommentId);
        Member member = findMemberById(memberId);

        replyComment.validateReplyCommentLike(member);

        if (replyCommentLikeRepository.existsByMemberAndReplyComment(member, replyComment)) {
            log.info("이미 좋아요를 누른 답글입니다: replyCommentId={}, memberId={}", replyCommentId, memberId);
            throw new AppException(ErrorCode.ALREADY_LIKED);
        }

        ReplyCommentLike replyCommentLike = ReplyCommentLike.of(member, replyComment);
        replyCommentLike.increaseReplyCommentLikeCount();

        replyCommentLikeRepository.save(replyCommentLike);
    }

    @Transactional
    public void deleteLike(Long replyCommentId, Long memberId) {

        ReplyComment replyComment = findReplyCommentById(replyCommentId);
        Member member = findMemberById(memberId);
        ReplyCommentLike replyCommentLike = replyCommentLikeRepository.findAllByMemberAndReplyComment(member,
                        replyComment)
                .orElseThrow(() -> new AppException(ErrorCode.REPLY_COMMENT_LIKE_NOT_FOUND));

        replyCommentLike.decreaseReplyCommentLikeCount(member, replyComment);
        replyCommentLikeRepository.delete(replyCommentLike);
    }

    private ReplyComment findReplyCommentById(Long replyCommentId) {
        return replyCommentRepository.findById(replyCommentId)
                .orElseThrow(() -> new AppException(ErrorCode.REPLY_COMMENT_NOT_FOUND));
    }

    private Member findMemberById(Long memberId) {
        return memberRepository.getReferenceById(memberId);
    }

    @Transactional
    public List<ReplyComment> getLikedReplyComments(Long memberId) {
        List<ReplyCommentLike> replyCommentLikes = replyCommentLikeRepository.findAllByMemberId(memberId);

        return replyCommentLikes.stream()
                .map(ReplyCommentLike::getReplyComment)
                .toList();
    }
}
