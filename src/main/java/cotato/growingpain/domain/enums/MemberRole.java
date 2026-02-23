package cotato.growingpain.domain.enums;

import lombok.Getter;

@Getter
public enum MemberRole {

    GENERAL("ROLE_GENERAL"),
    PENDING("ROLE_PENDING"),
    MEMBER("ROLE_MEMBER");

    private final String description;

    MemberRole(String description) {
        this.description = description;
    }
}