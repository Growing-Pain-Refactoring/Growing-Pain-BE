package cotato.growingpain.infrastructure.security.jwt.dto.response;

import cotato.growingpain.infrastructure.security.oauth.AuthProvider;

public interface OAuthUserInfoResponse {
    String getEmail();

    String getName();

    AuthProvider getOAuthProvider();
}