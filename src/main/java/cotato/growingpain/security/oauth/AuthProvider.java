package cotato.growingpain.security.oauth;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum AuthProvider {
    GENERAL("일반 로그인"),
    KAKAO("카카오 소셜 로그인"),
    GOOGLE("구글 소셜 로그인");

    private final String description;
}