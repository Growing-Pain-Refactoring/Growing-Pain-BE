package cotato.growingpain.service;

import cotato.growingpain.common.exception.AppException;
import cotato.growingpain.common.exception.ErrorCode;
import cotato.growingpain.domain.entity.Member;
import cotato.growingpain.infrastructure.repository.MemberRepository;
import cotato.growingpain.domain.entity.Post;
import cotato.growingpain.domain.entity.PostLike;
import cotato.growingpain.infrastructure.repository.PostLikeRepository;
import cotato.growingpain.infrastructure.repository.PostRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostLikeService {

    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void registerLike(Long postId, Long memberId) {

        Post post = findPostId(postId);
        Member member = findMemberById(memberId);
        post.validatePostLike(member);

        if (postLikeRepository.existsByMemberAndPost(member, post)) {
            log.info("이미 좋아요를 누른 포스트입니다: postId={}, memberId={}", postId, memberId);
            throw new AppException(ErrorCode.ALREADY_LIKED);
        }

        PostLike postLike = PostLike.of(member, post);
        postLike.increasePostLikeCount();

        postLikeRepository.save(postLike);
    }

    @Transactional
    public void deleteLike(Long postId, Long memberId) {

        Post post = findPostId(postId);
        Member member = findMemberById(memberId);

        PostLike postLike = postLikeRepository.findAllByMemberAndPost(member, post)
                .orElseThrow(() -> new AppException(ErrorCode.POST_LIKE_NOT_FOUND));

        postLike.decreasePostLikeCount(member, post);
        postLikeRepository.delete(postLike);
    }

    @Transactional
    public List<Post> getLikedPosts(Long memberId) {
        List<PostLike> postLikes = postLikeRepository.findAllByMemberId(memberId);

        return postLikes.stream()
                .map(PostLike::getPost)
                .collect(Collectors.toList());
    }

    private Post findPostId(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));
    }

    private Member findMemberById(Long memberId) {
        return memberRepository.getReferenceById(memberId);
    }
}