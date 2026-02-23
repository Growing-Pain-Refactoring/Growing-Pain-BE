package cotato.growingpain.infrastructure.security.jwt.dto;

public record LoginResultDto(
        String accessToken,
        String refreshToken,
        boolean isSignUp
) {
}