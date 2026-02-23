package cotato.growingpain.service;

import cotato.growingpain.infrastructure.client.OAuthApiClient;
import cotato.growingpain.infrastructure.security.jwt.dto.response.OAuthUserInfoResponse;
import cotato.growingpain.infrastructure.security.oauth.AuthProvider;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class RequestOAuthUserInfoService {

    private final Map<AuthProvider, OAuthApiClient> clients = new EnumMap<>(AuthProvider.class);

    public RequestOAuthUserInfoService(List<OAuthApiClient> clientList) {
        for (OAuthApiClient client : clientList) {
            clients.put(client.oAuthProvider(), client);
        }
    }

    public OAuthUserInfoResponse request(AuthProvider provider, String accessToken) {
        OAuthApiClient client = clients.get(provider);
        if (client == null) {
            throw new IllegalArgumentException("지원하지 않는 OAuth Provider입니다: " + provider);
        }
        return client.requestOauthUserInfo(accessToken);
    }
}
