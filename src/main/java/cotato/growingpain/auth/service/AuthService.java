package cotato.growingpain.auth.service;

import cotato.growingpain.auth.domain.BlackList;
import cotato.growingpain.auth.dto.request.CompleteSignupRequest;
import cotato.growingpain.auth.dto.request.LogoutRequest;
import cotato.growingpain.auth.repository.BlackListRepository;
import cotato.growingpain.common.exception.AppException;
import cotato.growingpain.common.exception.ErrorCode;
import cotato.growingpain.member.domain.MemberRole;
import cotato.growingpain.member.domain.entity.Member;
import cotato.growingpain.member.repository.MemberRepository;
import cotato.growingpain.security.RefreshTokenRepository;
import cotato.growingpain.security.jwt.JwtTokenProvider;
import cotato.growingpain.security.jwt.RefreshTokenEntity;
import cotato.growingpain.security.jwt.Token;
import cotato.growingpain.security.jwt.dto.request.ReissueRequest;
import cotato.growingpain.security.jwt.dto.response.ReissueResponse;
import cotato.growingpain.security.oauth.AuthProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AuthService {

    private final MemberRepository memberRepository;
    private final ValidateService validateService;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final BlackListRepository blackListRepository;

    @Transactional
    public Member findOrRegisterMember(String email, String oauth2Id, AuthProvider authProvider) {
        return memberRepository.findByEmail(email)
                .map(member -> {
                    member.updateOAuthInfo(oauth2Id, authProvider);
                    return memberRepository.save(member);
                })
                .orElseGet(() -> {
                    log.info("[OAuth 신규 회원 등록] email: {}, provider: {}", email, authProvider);
                    Member newMember = Member.builder()
                            .email(email)
                            .oauth2Id(oauth2Id)
                            .authProvider(authProvider)
                            .memberRole(MemberRole.PENDING)
                            .build();
                    return memberRepository.save(newMember);
                });
    }

    @Transactional
    public Token completeSignup(CompleteSignupRequest request, String accessToken) {

        String email = jwtTokenProvider.getEmail(accessToken);

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.MEMBER_NOT_FOUND));

        log.info("추가 정보 입력 받는 이메일: {}", email);

        if (member.getMemberRole() == MemberRole.PENDING) {

            validateService.checkDuplicateNickName(request.name());

            member.updateMemberInfo(request.name(), request.field(), request.belong(), request.job());
            member.updateRole(MemberRole.MEMBER);

            memberRepository.save(member);

            Token token = jwtTokenProvider.createToken(member.getId(), member.getEmail(), MemberRole.MEMBER.getDescription());

            saveOrUpdateRefreshToken(member.getEmail(), token.getRefreshToken());

            return token;
        }

        log.info("memberRole = {}", member.getMemberRole());
        return null;
    }

    public String resolveAccessToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.replace("Bearer ", "");
        }
        return null;
    }

    @Transactional
    public ReissueResponse tokenReissue(ReissueRequest request) {

        String email = jwtTokenProvider.getEmail(request.refreshToken());
        String role = jwtTokenProvider.getRole(request.refreshToken());
        Long memberId = jwtTokenProvider.getMemberId(request.refreshToken());

        log.info("재발급 요청된 이메일: {}", email);
        log.info("재발급 요청된 role: {}", role);

        RefreshTokenEntity findToken = refreshTokenRepository.findById(email)
                .orElseThrow(() -> new AppException(ErrorCode.EMAIL_NOT_FOUND));

        if (jwtTokenProvider.isExpired(request.refreshToken())) {
            throw new AppException(ErrorCode.TOKEN_EXPIRED);
        }

        if (!findToken.getRefreshToken().equals(request.refreshToken())) {
            log.warn("[쿠키로 들어온 토큰과 DB의 토큰이 일치하지 않음.]");
            throw new AppException(ErrorCode.REFRESH_TOKEN_NOT_EXIST);
        }

        Token token = jwtTokenProvider.createToken(memberId, email, role);

        log.info("재발급 된 액세스 토큰: {}", token.getAccessToken());
        log.info("재발급 된 refresh 토큰: {}", token.getRefreshToken());

        saveOrUpdateRefreshToken(email, token.getRefreshToken());
        return ReissueResponse.from(token.getAccessToken(), token.getRefreshToken());
    }

    @Transactional
    public void saveOrUpdateRefreshToken(String email, String refreshToken) {
        RefreshTokenEntity refreshTokenEntity = refreshTokenRepository.findById(email)
                .orElse(RefreshTokenEntity.builder().email(email).build());

        refreshTokenEntity.updateRefreshToken(refreshToken);
        refreshTokenRepository.save(refreshTokenEntity);
    }

    @Transactional
    public void logout(LogoutRequest request) {
        String email = jwtTokenProvider.getEmail(request.refreshToken());

        RefreshTokenEntity existRefreshToken = refreshTokenRepository.findById(email)
                .orElseThrow(() -> new AppException(ErrorCode.REFRESH_TOKEN_NOT_EXIST));

        setBlackList(request.refreshToken());
        log.info("[로그아웃 된 리프레시 토큰 블랙리스트 처리]");
        refreshTokenRepository.delete(existRefreshToken);
        log.info("삭제 요청된 refreshToken: {}", request.refreshToken());
    }

    private void setBlackList(String token) {
        BlackList blackList = BlackList.builder()
                .id(token)
                .ttl(jwtTokenProvider.getExpiration(token))
                .build();
        blackListRepository.save(blackList);
    }
}
