package cotato.growingpain.security.jwt.dto;

public record LoginResultDto(
        String accessToken,
        String refreshToken,
        boolean isSignUp
) {
}