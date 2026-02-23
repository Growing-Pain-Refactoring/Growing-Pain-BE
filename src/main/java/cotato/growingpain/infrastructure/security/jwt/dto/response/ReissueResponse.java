package cotato.growingpain.infrastructure.security.jwt.dto.response;

public record ReissueResponse(
        String accessToken,
        String refreshToken
) {
}