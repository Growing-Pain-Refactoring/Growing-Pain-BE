package cotato.growingpain.auth.controller;

import cotato.growingpain.auth.dto.request.CompleteSignupRequest;
import cotato.growingpain.auth.dto.request.LogoutRequest;
import cotato.growingpain.auth.dto.response.DuplicateCheckResponse;
import cotato.growingpain.auth.service.AuthService;
import cotato.growingpain.auth.service.ValidateService;
import cotato.growingpain.common.Response;
import cotato.growingpain.security.jwt.dto.request.ReissueRequest;
import cotato.growingpain.security.jwt.dto.response.ReissueResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth", description = "Auth 관련된 api")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final ValidateService validateService;

    @Operation(summary = "추가 정보 입력", description = "최초 로그인 시 추가 정보를 입력하는 메소드")
    @ApiResponse(content = @Content(schema = @Schema(implementation = Response.class)))
    @PostMapping("/complete-signup")
    @ResponseStatus(HttpStatus.OK)
    public Response<?> completeSignup(@RequestBody @Valid CompleteSignupRequest request,
                                      @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        String accessToken = authService.resolveAccessToken(authorizationHeader);
        return Response.createSuccess("추가 정보 입력 완료", authService.completeSignup(request, accessToken));
    }

    @Operation(summary = "리이슈", description = "리이슈 및 토큰 재발급을 위한 메소드")
    @ApiResponse(content = @Content(schema = @Schema(implementation = ReissueResponse.class)))
    @PostMapping("/reissue")
    @ResponseStatus(HttpStatus.OK)
    public Response<ReissueResponse> tokenRefresh(@RequestBody ReissueRequest request) {
        ReissueResponse reissueResponse = authService.tokenReissue(request);
        return Response.createSuccess("리이슈 완료", reissueResponse);
    }

    @Operation(summary = "로그아웃", description = "로그아웃을 위한 메소드")
    @ApiResponse(content = @Content(schema = @Schema(implementation = Response.class)))
    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.OK)
    public Response<?> logout(@RequestBody LogoutRequest request) {
        authService.logout(request);
        return Response.createSuccessWithNoData("로그아웃 성공");
    }

    @Operation(summary = "닉네임 중복 검증", description = "추가 정보 입력 시 기존 사용자와 닉네임이 중복되는지 확인하는 메소드")
    @ApiResponse(content = @Content(schema = @Schema(implementation = DuplicateCheckResponse.class)))
    @GetMapping("/validate/nickname")
    @ResponseStatus(HttpStatus.OK)
    public Response<DuplicateCheckResponse> checkDuplicateNickname(@Parameter String nickName) {
        boolean isDuplicate = validateService.isDuplicateNickname(nickName);
        DuplicateCheckResponse response = new DuplicateCheckResponse(isDuplicate);

        if (isDuplicate) {
            return Response.createSuccess("이미 사용 중인 닉네임입니다.", response);
        } else {
            return Response.createSuccess("사용 가능한 닉네임입니다.", response);
        }
    }
}
