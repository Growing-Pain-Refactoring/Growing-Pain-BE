package cotato.growingpain.infrastructure.security.jwt.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import cotato.growingpain.infrastructure.security.oauth.AuthProvider;

public record KakaoUserInfoResponse(
        @JsonProperty("kakao_account") KakaoAccount kakaoAccount
) implements OAuthUserInfoResponse {

    @Override
    public String getEmail() {
        return kakaoAccount.email();
    }

    @Override
    public String getName() {
        return kakaoAccount.profile().nickname();
    }

    @Override
    public AuthProvider getOAuthProvider() {
        return AuthProvider.KAKAO;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record KakaoAccount(String email, Profile profile) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Profile(String nickname) {
    }
}
