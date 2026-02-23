package cotato.growingpain.security.jwt.dto.response;

public record ReissueResponse(
        String accessToken,
        String refreshToken
) {
}