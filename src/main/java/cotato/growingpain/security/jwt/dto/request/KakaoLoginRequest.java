package cotato.growingpain.security.jwt.dto.request;

import jakarta.validation.constraints.NotBlank;

public record KakaoLoginRequest(
        @NotBlank
        String accessToken
) {
}
