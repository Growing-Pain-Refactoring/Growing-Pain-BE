package cotato.growingpain.dto.response;

import java.util.List;

public record JobApplicationListResponse(
        List<JobApplicationResponse> jobApplicaionList
) {
}
