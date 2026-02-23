package cotato.growingpain.domain.enums;

import lombok.Getter;

@Getter
public enum MemberJob {

    COLLEGE_STUDENT("대학생이에요"),
    GRADUATE("졸업생이에요"),
    JOB_SEEKER("취준생이에요"),
    PREPARING_FOR_JOB_CHANGE("이직 준비 중이에요");

    private final String description;

    MemberJob(String description) {
        this.description = description;
    }
}