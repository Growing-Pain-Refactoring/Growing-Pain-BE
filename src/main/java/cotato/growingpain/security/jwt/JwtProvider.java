package cotato.growingpain.security.jwt;

import cotato.growingpain.common.exception.AppException;
import cotato.growingpain.common.exception.ErrorCode;
import cotato.growingpain.security.jwt.dto.AccessTokenInfo;
import cotato.growingpain.security.jwt.properties.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtProvider {

    private final JwtProperties jwtProperties;

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(jwtProperties
                .getSecretKey()
                .getBytes(StandardCharsets.UTF_8));
    }

    public Long getExpiration(String token) {
        Date expiration = getClaims(token).getExpiration();
        return expiration.getTime() - new Date().getTime();
    }

    private String buildAccessToken(Long id, Date issuedAt, Date accessTokenExpiresIn, String role) {
        final Key encodedKey = getSecretKey();
        return Jwts.builder()
                .issuer("gamara")   //todo:변경
                .issuedAt(issuedAt)
                .subject(id.toString())
                .claim("type", "ACCESS_TOKEN")
                .claim("role", role)
                .expiration(accessTokenExpiresIn)
                .signWith(encodedKey)
                .compact();
    }

    private String buildRefreshToken(Long id, Date issuedAt, Date refreshTokenExpiresIn) {
        Key encodedKey = getSecretKey();
        return Jwts.builder()
                .issuer("gamara")   //todo:변경
                .issuedAt(issuedAt)
                .subject(id.toString())
                .claim("type", "REFRESH_TOKEN")
                .expiration(refreshTokenExpiresIn)
                .signWith(encodedKey)
                .compact();
    }

    public String generateAccessToken(Long id, String role) {
        Date issuedAt = new Date();
        Date accessTokenExpiresIn = new Date(issuedAt.getTime() + getAccessTokenTtlMilliSecond());

        return buildAccessToken(id, issuedAt, accessTokenExpiresIn, role);
    }

    public String generateRefreshToken(Long id) {
        Date issuedAt = new Date();
        Date refreshTokenExpiresIn = new Date(issuedAt.getTime() + getRefreshTokenTtlMilliSecond());

        return buildRefreshToken(id, issuedAt, refreshTokenExpiresIn);
    }

    public boolean isAccessToken(String token) {
        return getClaims(token).get("type").equals("ACCESS_TOKEN");
    }

    public boolean isRefreshToken(String token) {
        return getClaims(token).get("type").equals("REFRESH_TOKEN");
    }

    public AccessTokenInfo parseAccessToken(String token) {
        try {
            if (isAccessToken(token)) {
                Claims claims = getClaims(token);
                return AccessTokenInfo.of(
                        Long.parseLong(claims.getSubject()),
                        (String) claims.get("role")
                );
            }
            throw new AppException(ErrorCode.INVALID_ACCESS_TOKEN);
        } catch (ExpiredJwtException e) {
            throw new AppException(ErrorCode.EXPIRED_TOKEN);
        } catch (Exception e) {
            throw new AppException(ErrorCode.INVALID_JWT);
        }
    }

    public Long parseRefreshToken(String token) {
        try {
            if (isRefreshToken(token)) {
                Claims claims = getClaims(token);
                return Long.parseLong(claims.getSubject());
            }
            throw new AppException(ErrorCode.INVALID_REFRESH_TOKEN);
        } catch (ExpiredJwtException e) {
            throw new AppException(ErrorCode.EXPIRED_TOKEN);
        } catch (Exception e) {
            throw new AppException(ErrorCode.INVALID_JWT);
        }
    }

    public Long getAccessTokenTtlMilliSecond() {
        return jwtProperties.getAccessTokenTime();
    }

    public Long getRefreshTokenTtlMilliSecond() {
        return jwtProperties.getRefreshTokenTime();
    }
}