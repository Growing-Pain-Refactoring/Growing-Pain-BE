package cotato.growingpain.domain.enums;

import lombok.Getter;

@Getter
public enum ApplicationType {

    DOCUMENT("서류"),
    INTERVIEW("면접"),
    INTERVIEW_FEEDBACK("면접 피드백"),
    BUSINESS_ANALYSIS("기업 분석");

    private final String description;

    ApplicationType(String description) {
        this.description = description;
    }
}