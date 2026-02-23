package cotato.growingpain.domain.enums;

import lombok.Getter;

@Getter
public enum MemberProfileShowing {

    PUBLIC("공개"),
    PRIVATE("비공개");

    private final String description;

    MemberProfileShowing(String description) {
        this.description = description;
    }
}
