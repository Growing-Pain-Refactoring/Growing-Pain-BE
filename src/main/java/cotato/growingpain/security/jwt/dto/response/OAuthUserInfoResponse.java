package cotato.growingpain.security.jwt.dto.response;

import cotato.growingpain.security.oauth.AuthProvider;

public interface OAuthUserInfoResponse {
    String getEmail();

    String getName();

    AuthProvider getOAuthProvider();
}