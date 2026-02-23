package cotato.growingpain.infrastructure.converter;

import cotato.growingpain.domain.entity.redis.BlackList;
import cotato.growingpain.domain.entity.redis.RefreshToken;
import cotato.growingpain.domain.entity.Member;
import cotato.growingpain.infrastructure.security.jwt.dto.LoginResultDto;
import cotato.growingpain.infrastructure.security.oauth.AuthProvider;
import java.time.LocalDateTime;

public class AuthConverter {

    public static LoginResultDto toLoginResultDto(Member member, String accessToken, String refreshToken) {
        boolean isSignUp = member.getCreatedAt().equals(member.getModifiedAt());
        return new LoginResultDto(accessToken, refreshToken, isSignUp);
    }

    public static Member toUserEntity(String email, String name, AuthProvider authProvider) {
        return Member.builder()
                .name(name)
                .email(email)
                .authProvider(authProvider)
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .deleted(false)
                .build();
    }

    public static RefreshToken toRefreshTokenEntity(Long memberId, String refreshToken, long ttl) {
        return RefreshToken.builder()
                .id(memberId)
                .refreshToken(refreshToken)
                .ttl(ttl)
                .build();
    }

    public static BlackList toBlackList(String token, long ttl) {
        return BlackList.builder()
                .id(token)
                .ttl(ttl)
                .build();
    }
}
