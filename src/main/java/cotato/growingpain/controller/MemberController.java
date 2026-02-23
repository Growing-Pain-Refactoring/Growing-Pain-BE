package cotato.growingpain.controller;

import cotato.growingpain.common.Response;
import cotato.growingpain.common.exception.ImageException;
import cotato.growingpain.dto.request.AdditionalInfoRequest;
import cotato.growingpain.dto.request.UpdateDefaultInfoRequest;
import cotato.growingpain.dto.request.UpdateMemberProfileShowingRequest;
import cotato.growingpain.dto.response.MemberInfoResponse;
import cotato.growingpain.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "마이페이지", description = "마이페이지 관련된 api")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
@Slf4j
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "기본 정보 (소속, 직업, 학력, 스킬, 이력 및 활동, 수상내역, 어학성적) 등록 및 수정", description = "정보 업데이트 중 기본 정보 (소속, 직업, 학력, 스킬, 이력 및 활동, 수상내역, 어학성적) 수정을 위한 메소드")
    @ApiResponse(content = @Content(schema = @Schema(implementation = Response.class)))
    @PostMapping("/default-info")
    @ResponseStatus(HttpStatus.OK)
    public Response<?> registerPost(@Valid @RequestBody UpdateDefaultInfoRequest request,
                                    @AuthenticationPrincipal Long memberId) {
        log.info("기본 정보를 수정한 memberId: {}", memberId);
        memberService.updateDefaultInfo(request, memberId);
        return Response.createSuccessWithNoData("[마이페이지] 기본 정보 수정 완료");
    }

    @Operation(summary = "프로필 공개 여부 설정", description = "멤버의 프로필을 공개 또는 비공개로 설정")
    @ApiResponse(content = @Content(schema = @Schema(implementation = Response.class)))
    @PatchMapping("/update-profile-showing")
    @ResponseStatus(HttpStatus.OK)
    public Response<?> updateProfileShowing(@Valid @RequestBody UpdateMemberProfileShowingRequest request,
                                            @AuthenticationPrincipal Long memberId) {
        log.info("프로필 공개 여부를 수정한 memberId: {}", memberId);
        memberService.updateProfileShowing(memberId, request.memberProfileShowing());
        return Response.createSuccessWithNoData("[마이페이지] 프로필 공개 여부 설정");
    }

    @Operation(summary = "추가 정보 (경력, 수상 내역) 등록 및 수정", description = "정보 업데이트 중 추가 정보 등록을 위한 메소드")
    @ApiResponse(content = @Content(schema = @Schema(implementation = Response.class)))
    @PostMapping("/additional-info")
    @ResponseStatus(HttpStatus.OK)
    public Response<?> registerPost(@Valid @RequestBody AdditionalInfoRequest request,
                                    @AuthenticationPrincipal Long memberId) {
        log.info("추가 정보를 등록한 memberId: {}", memberId);
        memberService.registerAdditionalInfo(request, memberId);
        return Response.createSuccessWithNoData("[마이페이지] 추가 정보 입력 완료");
    }

    @Operation(summary = "멤버 정보 조회", description = "멤버 프로필 공개 여부에 따라 정보를 조회")
    @ApiResponse(content = @Content(schema = @Schema(implementation = MemberInfoResponse.class)))
    @GetMapping("/info")
    @ResponseStatus(HttpStatus.OK)
    public Response<MemberInfoResponse> getMemberInfo(@AuthenticationPrincipal Long memberId) {
        MemberInfoResponse memberInfo = memberService.getMemberInfo(memberId);
        return Response.createSuccess("[마이페이지] 프로필 공개 여부에 따른 정보 반환", memberInfo);
    }

    @Operation(summary = "프로필 이미지 등록", description = "멤버 프로필 이미지 등록 및 저장")
    @ApiResponse(content = @Content(schema = @Schema(implementation = Response.class)))
    @GetMapping("/profile-image")
    @ResponseStatus(HttpStatus.OK)
    public Response<?> registerProfileImage(@RequestParam("profile-image") MultipartFile profileImage,
                                            @AuthenticationPrincipal Long memberId) throws ImageException {
        log.info("프로필 이미지를 업로드한 memberId: {}", memberId);
        memberService.registerProfileImage(memberId, profileImage);
        return Response.createSuccessWithNoData("[마이페이지] 프로필 이미지 업로드 완료");
    }
}