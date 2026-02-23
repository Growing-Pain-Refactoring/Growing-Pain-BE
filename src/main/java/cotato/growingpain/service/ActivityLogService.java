package cotato.growingpain.service;

import cotato.growingpain.domain.entity.ActivityLog;
import cotato.growingpain.dto.ActivityLogDTO;
import cotato.growingpain.dto.request.ActivityLogRequestDTO;
import cotato.growingpain.domain.entity.Member;
import cotato.growingpain.infrastructure.repository.ActivityLogRepository;
import cotato.growingpain.infrastructure.repository.MemberRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ActivityLogService {

    private final MemberRepository memberRepository;
    private final ActivityLogRepository activityLogRepository;

    @Transactional
    public void createActivityLog(final ActivityLogRequestDTO activityLogRequestDTO, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found with ID: " + memberId));
        ActivityLog activityLog = activityLogRequestDTO.toEntity(member);
        activityLogRepository.save(activityLog);
    }

    @Transactional
    public List<ActivityLogRequestDTO> retrieveActivityLogsByMemberId(Long memberId) {
        return activityLogRepository.findByMemberId(memberId);
    }

    @Transactional
    public ActivityLogDTO retrieveActivityLogById(Long activityLogId) {
        ActivityLog activityLog = activityLogRepository.findById(activityLogId)
                .orElseThrow(() -> new NoSuchElementException("ActivityLog not found with ID: " + activityLogId));

        return ActivityLogDTO.fromEntity(activityLog);
    }

    @Transactional
    public ActivityLogDTO updateActivityLog(Long activityLogId, ActivityLogDTO updatedActivityLogDTO) {
        ActivityLog existingActivityLog = activityLogRepository.findById(activityLogId)
                .orElseThrow(() -> new NoSuchElementException("ActivityLog not found with ID: " + activityLogId));
        existingActivityLog.updateFromDTO(updatedActivityLogDTO);
        ActivityLog savedActivityLog = activityLogRepository.save(existingActivityLog);

        return ActivityLogDTO.fromEntity(savedActivityLog);
    }

    @Transactional
    public void deleteActivityLog(Long activityLogId, Long memberId) {
        ActivityLog existingActivityLog = activityLogRepository.findById(activityLogId)
                .orElseThrow(() -> new NoSuchElementException("ActivityLog not found with ID: " + activityLogId));
        activityLogRepository.delete(existingActivityLog);
    }
}
