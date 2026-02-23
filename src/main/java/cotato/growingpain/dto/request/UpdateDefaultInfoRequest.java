package cotato.growingpain.dto.request;

import cotato.growingpain.domain.enums.MemberJob;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateDefaultInfoRequest (

        @NotBlank(message = "분야는 필수 입력 항목입니다.")
        String field,

        @NotBlank(message = "소속은 필수 입력 항목입니다.")
        String belong,

        @NotNull
        MemberJob job,

        String educationBackground,

        String skill,

        String activityHistory,

        String award,

        String languageScore
) {
}