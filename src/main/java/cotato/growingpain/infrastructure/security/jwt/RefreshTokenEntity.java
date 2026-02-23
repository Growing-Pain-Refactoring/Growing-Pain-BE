package cotato.growingpain.infrastructure.security.jwt;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
//@RedisHash(value = "jwtToken", timeToLive = 60 * 60 * 24 * 3)
public class RefreshTokenEntity {

    @Id
    private String email; // email이 id로 사용됨

    private String refreshToken;

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}