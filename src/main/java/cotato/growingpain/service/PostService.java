package cotato.growingpain.service;

import cotato.growingpain.domain.entity.Comment;
import cotato.growingpain.infrastructure.repository.CommentRepository;
import cotato.growingpain.common.exception.AppException;
import cotato.growingpain.common.exception.ErrorCode;
import cotato.growingpain.common.exception.ImageException;
import cotato.growingpain.domain.entity.Member;
import cotato.growingpain.infrastructure.repository.MemberRepository;
import cotato.growingpain.domain.enums.PostCategory;
import cotato.growingpain.domain.entity.Post;
import cotato.growingpain.domain.entity.PostImage;
import cotato.growingpain.dto.request.PostRequest;
import cotato.growingpain.infrastructure.repository.PostImageRepository;
import cotato.growingpain.infrastructure.repository.PostLikeRepository;
import cotato.growingpain.infrastructure.repository.PostRepository;
import cotato.growingpain.infrastructure.repository.ReplyCommentRepository;
import cotato.growingpain.infrastructure.s3.S3Uploader;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;
    private final ReplyCommentRepository replyCommentRepository;
    private final PostImageRepository postImageRepository;
    private final S3Uploader s3Uploader;

    @Transactional
    public void registerPost(PostRequest request, Long memberId) throws ImageException {
        Member member = memberRepository.getReferenceById(memberId);
        PostCategory parentCategory = request.category().getParent();

        Post post = Post.of(member, request.title(), request.content(), parentCategory, request.category());
        postRepository.save(post);

        uploadAndSavePostImage(request.postImages(), post);
    }

    @Transactional
    public List<Post> getPostsByMemberId(Long memberId) {
        return postRepository.findAllByMemberId(memberId);
    }

    @Transactional
    public List<Post> getPostsByCategory(PostCategory category) {
        return postRepository.findAllByCategory(category);
    }

    @Transactional
    public List<Post> getAllPostsByCategory() {
        return postRepository.findAll();
    }

    @Transactional
    public void deletePost(Long postId, Long memberId) {
        Post post = findByPostIdAndMemberId(postId, memberId);

        List<Comment> comments = commentRepository.findAllByPostId(postId);
        for (Comment comment : comments) {
            replyCommentRepository.deleteAllByCommentId(comment.getId());
            commentRepository.delete(comment);
        }

        postImageRepository.deleteAllByPostId(postId);
        postLikeRepository.deleteAllByPostId(postId);

        postRepository.delete(post);
    }

    @Transactional
    public void updatePost(Long postId, PostRequest request, Long memberId) throws ImageException {
        Post post = findByPostIdAndMemberId(postId, memberId);

        postImageRepository.deleteAllByPostId(postId);

        post.updatePost(request.title(), request.content(), request.category());
        uploadAndSavePostImage(request.postImages(), post);

        postRepository.save(post);
    }

    @Transactional
    public List<PostImage> getPostImageByPostId(Long postId) {
        return postImageRepository.findAllByPostId(postId);

    }

    private Post findByPostIdAndMemberId(Long postId, Long memberId) {
        return postRepository.findAllByIdAndMemberId(postId, memberId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));
    }

    private void uploadAndSavePostImage(List<MultipartFile> imageFiles, Post post) throws ImageException {
         if (imageFiles != null && !imageFiles.isEmpty()) { // null 체크 추가
            for (MultipartFile imageFile : imageFiles) {
                if (!imageFile.isEmpty()) {
                    String imageUrl = s3Uploader.uploadFileToS3(imageFile, "post");
                    PostImage postImage = new PostImage(post.getId(), imageUrl);
                    postImageRepository.save(postImage);
                }
            }
        }
    }
}