package cotato.growingpain.controller;

import cotato.growingpain.common.Response;
import cotato.growingpain.dto.response.CoverLetterResponse;
import cotato.growingpain.service.CoverLetterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "자소서", description = "자기소개서 업로드 및 AI 분석 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cover-letter")
@Slf4j
public class CoverLetterController {

    private final CoverLetterService coverLetterService;

    @Operation(summary = "자소서 PDF 업로드", description = "PDF 파일을 업로드하고 AI 분석을 요청합니다.")
    @PostMapping("/upload")
    @ResponseStatus(HttpStatus.CREATED)
    public Response<CoverLetterResponse> uploadCoverLetter(
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal Long memberId) {
        log.info("자소서 업로드 요청: memberId={}", memberId);
        CoverLetterResponse response = coverLetterService.uploadCoverLetter(file, memberId);
        return Response.createSuccess("자소서 업로드 완료", response);
    }
}
