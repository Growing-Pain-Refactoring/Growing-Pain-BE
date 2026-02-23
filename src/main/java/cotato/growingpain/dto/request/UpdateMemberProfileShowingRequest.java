package cotato.growingpain.dto.request;

import cotato.growingpain.domain.enums.MemberProfileShowing;
import jakarta.validation.constraints.NotNull;

public record UpdateMemberProfileShowingRequest(

        @NotNull(message = "프로필 공개 여부는 필수 항목입니다.")
        MemberProfileShowing memberProfileShowing) {
}
