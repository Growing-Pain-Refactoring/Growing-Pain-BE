package cotato.growingpain.infrastructure.security.jwt.filter;

import cotato.growingpain.infrastructure.security.jwt.JwtProvider;
import cotato.growingpain.infrastructure.security.jwt.dto.AccessTokenInfo;
import cotato.growingpain.service.AuthService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final AuthService authService;

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = resolveToken(request);

        if (token != null && !authService.isBlocked(token)) {
            Authentication authentication = getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String rawHeader = request.getHeader("Authorization");
        String bearer = "Bearer ";

        if (rawHeader != null && rawHeader.length() > bearer.length() && rawHeader.startsWith(bearer)) {
            return rawHeader.substring(bearer.length());
        }

        Cookie accessTokenCookie = WebUtils.getCookie(request, "accessToken");
        if (accessTokenCookie != null) {
            return accessTokenCookie.getValue();
        }

        return null;
    }

    public Authentication getAuthentication(String token) {
        AccessTokenInfo accessTokenInfo = jwtProvider.parseAccessToken(token);
        //authService.validateRegisteredUser(accessTokenInfo.userId()); todo: 검증 메소드 작성 후 주석 삭제
        Long memberId = accessTokenInfo.userId();
        String role = accessTokenInfo.role();
        String authority = role.startsWith("ROLE_") ? role : "ROLE_" + role;
        return new UsernamePasswordAuthenticationToken(
                memberId,
                "user",
                List.of(new SimpleGrantedAuthority(authority))
        );
    }
}
