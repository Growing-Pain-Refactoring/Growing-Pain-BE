package cotato.growingpain.service;

import cotato.growingpain.common.exception.AppException;
import cotato.growingpain.common.exception.ErrorCode;
import cotato.growingpain.common.exception.ImageException;
import cotato.growingpain.domain.enums.MemberProfileShowing;
import cotato.growingpain.domain.entity.Member;
import cotato.growingpain.dto.request.AdditionalInfoRequest;
import cotato.growingpain.dto.request.UpdateDefaultInfoRequest;
import cotato.growingpain.dto.response.MemberInfoResponse;
import cotato.growingpain.infrastructure.repository.MemberRepository;
import cotato.growingpain.infrastructure.s3.S3Uploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final S3Uploader s3Uploader;

    @Transactional
    public void updateDefaultInfo(UpdateDefaultInfoRequest request, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(()->new AppException(ErrorCode.MEMBER_NOT_FOUND));

        member.updateDefaultInfo(
                request.field(),
                request.belong(),
                request.job(),
                request.educationBackground(),
                request.skill(),
                request.activityHistory(),
                request.award(),
                request.languageScore()
        );
        memberRepository.save(member);
    }

    @Transactional
    public void registerAdditionalInfo(AdditionalInfoRequest request, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(()->new AppException(ErrorCode.MEMBER_NOT_FOUND));

        member.updateAdditionalInfo(
                request.career(),
                request.aboutMe()
        );
        memberRepository.save(member);
    }

    @Transactional
    public void updateProfileShowing(Long memberId, MemberProfileShowing memberProfileShowing) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new AppException(ErrorCode.MEMBER_NOT_FOUND));

        member.updateProfilePublic(memberProfileShowing);
        memberRepository.save(member);
    }

    @Transactional
    public MemberInfoResponse getMemberInfo(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new AppException(ErrorCode.MEMBER_NOT_FOUND));

        if (member.getMemberProfileShowing() == MemberProfileShowing.PUBLIC) {
            return MemberInfoResponse.fromMember(member);
        } else {
            return MemberInfoResponse.defaultInfoFromMember(member);
        }
    }

    @Transactional
    public void registerProfileImage(Long memberId, MultipartFile profileImage) throws ImageException {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new AppException(ErrorCode.MEMBER_NOT_FOUND));

        if (profileImage != null && !profileImage.isEmpty()) {
            String imageUrl = s3Uploader.uploadFileToS3(profileImage, "profile-image");
            member.updateProfileImage(imageUrl);
            memberRepository.save(member);
        }
    }
}