package cotato.growingpain.infrastructure.client;

import cotato.growingpain.infrastructure.client.dto.AiAnalysisRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class AiAnalysisClient {

    private final RestClient restClient;

    @Value("${ai.server.url}")
    private String aiServerUrl;

    public void requestAnalysis(Long coverLetterId, String s3Url) {
        try {
            restClient.post()
                    .uri(aiServerUrl + "/analyze")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new AiAnalysisRequest(coverLetterId, s3Url))
                    .retrieve()
                    .toBodilessEntity();
            log.info("AI 분석 요청 완료: coverLetterId={}", coverLetterId);
        } catch (Exception e) {
            log.error("AI 분석 요청 실패: coverLetterId={}, error={}", coverLetterId, e.getMessage());
        }
    }
}
