package cotato.growingpain.service;

import cotato.growingpain.common.exception.AppException;
import cotato.growingpain.common.exception.ErrorCode;
import cotato.growingpain.domain.entity.CoverLetter;
import cotato.growingpain.domain.entity.Member;
import cotato.growingpain.domain.enums.AnalysisStatus;
import cotato.growingpain.dto.response.CoverLetterResponse;
import cotato.growingpain.infrastructure.client.AiAnalysisClient;
import cotato.growingpain.infrastructure.repository.CoverLetterRepository;
import cotato.growingpain.infrastructure.repository.MemberRepository;
import cotato.growingpain.infrastructure.s3.S3Uploader;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CoverLetterService {

    private final CoverLetterRepository coverLetterRepository;
    private final MemberRepository memberRepository;
    private final S3Uploader s3Uploader;
    private final AiAnalysisClient aiAnalysisClient;

    @Transactional
    public CoverLetterResponse uploadCoverLetter(MultipartFile file, Long memberId) {
        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null || !originalFileName.toLowerCase().endsWith(".pdf")) {
            throw new AppException(ErrorCode.INVALID_FILE_EXTENSION);
        }

        String s3Url;
        try {
            s3Url = s3Uploader.uploadFileToS3(file, "cover-letters");
        } catch (IOException e) {
            throw new AppException(ErrorCode.FILE_PROCESSING_FAIL);
        }

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new AppException(ErrorCode.MEMBER_NOT_FOUND));

        CoverLetter coverLetter = CoverLetter.builder()
                .member(member)
                .originalFileName(originalFileName)
                .s3Url(s3Url)
                .analysisStatus(AnalysisStatus.PENDING)
                .build();
        coverLetterRepository.save(coverLetter);

        Long coverLetterId = coverLetter.getId();
        CompletableFuture.runAsync(() -> aiAnalysisClient.requestAnalysis(coverLetterId, s3Url));

        return CoverLetterResponse.from(coverLetter);
    }

    public List<CoverLetterResponse> getCoverLetters(Long memberId) {
        return coverLetterRepository.findByMemberId(memberId).stream()
                .map(CoverLetterResponse::from)
                .toList();
    }
}
