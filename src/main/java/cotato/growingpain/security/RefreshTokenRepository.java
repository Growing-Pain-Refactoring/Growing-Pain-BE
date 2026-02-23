package cotato.growingpain.security;

import cotato.growingpain.auth.domain.RefreshToken;
import cotato.growingpain.security.jwt.RefreshTokenEntity;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByRefreshToken(String refreshToken);
}
