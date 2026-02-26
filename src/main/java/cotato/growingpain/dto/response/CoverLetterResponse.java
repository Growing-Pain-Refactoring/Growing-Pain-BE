package cotato.growingpain.dto.response;

import cotato.growingpain.domain.entity.CoverLetter;
import cotato.growingpain.domain.enums.AnalysisStatus;
import java.time.LocalDateTime;

public record CoverLetterResponse(
        Long id,
        String originalFileName,
        String s3Url,
        AnalysisStatus analysisStatus,
        LocalDateTime createdAt
) {
    public static CoverLetterResponse from(CoverLetter coverLetter) {
        return new CoverLetterResponse(
                coverLetter.getId(),
                coverLetter.getOriginalFileName(),
                coverLetter.getS3Url(),
                coverLetter.getAnalysisStatus(),
                coverLetter.getCreatedAt()
        );
    }
}
