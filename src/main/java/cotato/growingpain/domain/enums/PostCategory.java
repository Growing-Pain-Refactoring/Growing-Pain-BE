package cotato.growingpain.domain.enums;

import lombok.Getter;

@Getter
public enum PostCategory {

    ALL("전체", null),
    FREE("자유게시판", null),
    TEAM("팀원 모집", null),
    CONTEST("공모전", TEAM),
    PROJECT("프로젝트", TEAM),
    STUDY("스터디", TEAM),
    PORTFOLIO("포트폴리오", null);

    private final String description;
    private final PostCategory parent;

    PostCategory(String description, PostCategory parent) {
        this.description = description;
        this.parent = parent;
    }
}