package cotato.growingpain.service;

import cotato.growingpain.common.exception.AppException;
import cotato.growingpain.common.exception.ErrorCode;
import cotato.growingpain.domain.entity.Member;
import cotato.growingpain.infrastructure.repository.MemberRepository;
import cotato.growingpain.domain.entity.Post;
import cotato.growingpain.domain.entity.PostSave;
import cotato.growingpain.infrastructure.repository.PostRepository;
import cotato.growingpain.infrastructure.repository.PostSaveRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostSaveService {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final PostSaveRepository postSaveRepository;

    @Transactional
    public void savePost(Long postId, Long memberId) {
        Member member = findByMemberId(memberId);
        Post post = findByPostId(postId);

        if (postSaveRepository.existsByMemberIdAndPostId(memberId, postId)) {
            throw new AppException(ErrorCode.ALREADY_SAVED);
        }

        PostSave postSave = PostSave.createPostSave(member, post);
        postSaveRepository.save(postSave);
    }

    @Transactional
    public void deleteSavePost(Long postId, Long memberId) {

        if (!postSaveRepository.existsByMemberIdAndPostId(memberId, postId)) {
            throw new AppException(ErrorCode.POST_NOT_FOUND);
        }

        postSaveRepository.deleteByMemberIdAndPostId(memberId, postId);
    }

    @Transactional
    public List<Post> getSavedPosts(Long memberId) {
        List<PostSave> postSaves = postSaveRepository.findAllByMemberId(memberId);

        return postSaves.stream()
                .map(PostSave::getPost)
                .collect(Collectors.toList());
    }

    private Post findByPostId(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));
    }

    private Member findByMemberId(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new AppException(ErrorCode.MEMBER_NOT_FOUND));
    }
}