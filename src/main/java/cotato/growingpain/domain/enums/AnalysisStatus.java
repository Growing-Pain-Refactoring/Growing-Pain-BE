package cotato.growingpain.domain.enums;

import lombok.Getter;

@Getter
public enum AnalysisStatus {

    PENDING("분석 대기 중"),
    IN_PROGRESS("분석 중"),
    COMPLETED("분석 완료"),
    FAILED("분석 실패");

    private final String description;

    AnalysisStatus(String description) {
        this.description = description;
    }
}
