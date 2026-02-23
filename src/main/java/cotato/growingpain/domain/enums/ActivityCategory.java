package cotato.growingpain.domain.enums;

import lombok.Getter;

@Getter
public enum ActivityCategory {

    EXTRA_ACTIVITY("대외활동"),
    VOLUNTEER_ACTIVITY("봉사활동"),
    PROJECT("프로젝트"),
    COMPETITIONS("공모전"),
    CLUB("동아리"),
    EXTRA_SPACE("여분통");

    private final String description;

    ActivityCategory(String description) {
        this.description = description;
    }
}