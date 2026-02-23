package cotato.growingpain.auth.service;

import cotato.growingpain.auth.domain.BlackList;
import cotato.growingpain.auth.domain.RefreshToken;
import cotato.growingpain.auth.dto.request.CompleteSignupRequest;
import cotato.growingpain.auth.dto.request.LogoutRequest;
import cotato.growingpain.auth.repository.BlackListRepository;
import cotato.growingpain.common.exception.AppException;
import cotato.growingpain.common.exception.ErrorCode;
import cotato.growingpain.converter.AuthConverter;
import cotato.growingpain.member.domain.MemberRole;
import cotato.growingpain.member.domain.entity.Member;
import cotato.growingpain.member.repository.MemberRepository;
import cotato.growingpain.security.RefreshTokenRepository;
import cotato.growingpain.security.jwt.JwtProvider;
import cotato.growingpain.security.jwt.dto.AccessTokenInfo;
import cotato.growingpain.security.jwt.dto.LoginResultDto;
import cotato.growingpain.security.jwt.dto.request.KakaoLoginRequest;
import cotato.growingpain.security.jwt.dto.response.OAuthUserInfoResponse;
import cotato.growingpain.security.jwt.dto.response.ReissueResponse;
import cotato.growingpain.security.jwt.properties.JwtProperties;
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
    private final JwtProvider jwtProvider;
    private final JwtProperties jwtProperties;
    private final RequestOAuthUserInfoService requestOAuthUserInfoService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final BlackListRepository blackListRepository;
    private final ValidateService validateService;

    @Transactional
    public LoginResultDto loginKakao(KakaoLoginRequest request) {
        // 1. accessToken으로 사용자 정보 요청
        OAuthUserInfoResponse userInfo = requestOAuthUserInfoService.request(AuthProvider.KAKAO, request.accessToken());

        String email = userInfo.getEmail();
        String nickname = userInfo.getName();
        AuthProvider authProvider = userInfo.getOAuthProvider();

        // 2. 이메일로 기존 사용자 조회 및 신규 등록
        Member member = memberRepository.findByEmail(email)
                .orElseGet(() -> memberRepository.save(AuthConverter.toUserEntity(email, nickname, authProvider)));

        if (member.getMemberRole() == null) {
            member.updateRole(MemberRole.PENDING);
            memberRepository.save(member);
        }

        // 3. JWT 발급
        String accessToken = jwtProvider.generateAccessToken(member.getId(), member.getMemberRole().name());
        String refreshToken = jwtProvider.generateRefreshToken(member.getId());

        // 4. refreshToken Redis 저장
        RefreshToken tokenEntity = AuthConverter.toRefreshTokenEntity(
                member.getId(),
                refreshToken,
                jwtProperties.getRefreshTokenTime()
        );
        refreshTokenRepository.save(tokenEntity);

        // 5. converter 사용해서 dto로 변환
        return AuthConverter.toLoginResultDto(member, accessToken, refreshToken);
    }

    @Transactional
    public ReissueResponse completeSignup(CompleteSignupRequest request, String accessToken) {

        AccessTokenInfo accessTokenInfo = jwtProvider.parseAccessToken(accessToken);
        Long memberId = accessTokenInfo.userId();

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new AppException(ErrorCode.MEMBER_NOT_FOUND));

        log.info("추가 정보 입력 받는 이메일: {}", email);
        log.info("추가 정보 입력 받는 회원 ID: {}", memberId);

        if (member.getName() == null || !member.getName().equals(request.name())) {
            validateService.checkDuplicateNickName(request.name());
        }

        member.updateMemberInfo(request.name(), request.field(), request.belong(), request.job());
        member.updateRole(MemberRole.MEMBER);
        memberRepository.save(member);

        String newAccessToken = jwtProvider.generateAccessToken(member.getId(), MemberRole.MEMBER.name());
        String newRefreshToken = jwtProvider.generateRefreshToken(member.getId());

        RefreshToken tokenEntity = AuthConverter.toRefreshTokenEntity(
                member.getId(),
                newRefreshToken,
                jwtProperties.getRefreshTokenTime()
        );
        refreshTokenRepository.save(tokenEntity);

        return new ReissueResponse(newAccessToken, newRefreshToken);
    }


    public ReissueResponse reissueToken(String refreshToken) {
        // 1. refreshToken 유효성 검증 및 userId 추출
        Long userId = jwtProvider.parseRefreshToken(refreshToken);

        // 2. Redis 저장값 확인 (id 기준)
        RefreshToken storedRefreshToken = refreshTokenRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.REFRESH_TOKEN_NOT_FOUND));

        // 3. 값 비교
        if (!storedRefreshToken.getRefreshToken().equals(refreshToken)) {
            throw new AppException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        // 4. 기존 refreshToken 삭제
        refreshTokenRepository.deleteById(userId);

        // 5. 새 토큰 발급
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.MEMBER_NOT_FOUND));
        MemberRole memberRole = member.getMemberRole() != null ? member.getMemberRole() : MemberRole.PENDING;

        String newAccessToken = jwtProvider.generateAccessToken(userId, memberRole.name());
        String newRefreshToken = jwtProvider.generateRefreshToken(userId);

        // 6. 새 refreshToken Redis 저장
        RefreshToken tokenEntity = AuthConverter.toRefreshTokenEntity(
                userId,
                newRefreshToken,
                jwtProperties.getRefreshTokenTime()
        );
        refreshTokenRepository.save(tokenEntity);

        // 7. 결과 반환
        return new ReissueResponse(newAccessToken, newRefreshToken);
    }

    @Transactional
    public void logout(LogoutRequest logoutRequest) {
        // 1. accessToken → 블랙리스트 등록
        setBlackList(logoutRequest.accessToken());

        // 2. refreshToken → Redis에서 삭제
        deleteRefreshToken(logoutRequest.refreshToken());
    }


    public boolean isBlocked(String accessToken) {
        return blackListRepository.findById(accessToken).isPresent();
    }

    private void setBlackList(String accessToken) {
        long ttl = jwtProvider.getExpiration(accessToken);
        BlackList blacklist = AuthConverter.toBlackList(accessToken, ttl);
        blackListRepository.save(blacklist);
    }

    private void deleteRefreshToken(String refreshToken) {
        RefreshToken refreshTokenEntity = refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new AppException(ErrorCode.REFRESH_TOKEN_NOT_FOUND));

        refreshTokenRepository.delete(refreshTokenEntity);
    }
}
