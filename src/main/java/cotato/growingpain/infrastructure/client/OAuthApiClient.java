package cotato.growingpain.infrastructure.client;

import cotato.growingpain.security.jwt.dto.response.OAuthUserInfoResponse;
import cotato.growingpain.security.oauth.AuthProvider;

public interface OAuthApiClient {
    AuthProvider oAuthProvider();

    OAuthUserInfoResponse requestOauthUserInfo(String accessToken);
}
