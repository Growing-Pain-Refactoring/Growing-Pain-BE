package cotato.growingpain.infrastructure.client;

import cotato.growingpain.infrastructure.security.jwt.dto.response.OAuthUserInfoResponse;
import cotato.growingpain.infrastructure.security.oauth.AuthProvider;

public interface OAuthApiClient {
    AuthProvider oAuthProvider();

    OAuthUserInfoResponse requestOauthUserInfo(String accessToken);
}
