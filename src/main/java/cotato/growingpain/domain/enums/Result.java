package cotato.growingpain.domain.enums;

import lombok.Getter;

@Getter
public enum Result {

    PENDING("대기중"),
    PASSED("합격"),
    FAILED("불합격"),
    SUBMITTED("제출"),
    NOT_SUBMITTED("미제출");


    private final String description;

    Result(String description) {
        this.description = description;
    }
}