package cotato.growingpain.domain.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import cotato.growingpain.common.domain.BaseTimeEntity;
import cotato.growingpain.domain.enums.ApplicationType;
import cotato.growingpain.domain.enums.Result;
import cotato.growingpain.dto.request.ApplicationDetailRequestDTO;
import cotato.growingpain.dto.request.JobApplicationRequestDTO;
import cotato.growingpain.infrastructure.repository.ApplicationDetailRepository;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Entity
@Getter
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JobApplication extends BaseTimeEntity {
    /* -------------------------------------------- */
    /* -------------- Default Column -------------- */
    /* -------------------------------------------- */
    @Id
    @Column(name = "job_application_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* -------------------------------------------- */
    /* ------------ Information Column ------------ */
    /* -------------------------------------------- */
    @Enumerated(EnumType.STRING)
    @Column(name = "applicaton_type")
    private ApplicationType applicationType;

    @Column(name = "place")
    private String place;

    @Enumerated(EnumType.STRING)
    @Column(name = "result")
    private Result result;

    @Column(name = "submission_status")
    private boolean submissionStatus;

    @Column(name = "result_status")
    private boolean resultStatus;

    @Column(name = "application_start_date")
    private String applicationStartDate;

    @Column(name = "job_application_close_date")
    private String applicationCloseDate;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime modifiedAt;

    /* -------------------------------------------- */
    /* -------------- Relation Column ------------- */
    /* -------------------------------------------- */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @JsonIgnore
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_post_id")
    @JsonBackReference
    private JobPost jobPost;

    @OneToMany(mappedBy = "jobApplication", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ApplicationDetail> applicationDetails = new ArrayList<>();

    /* -------------------------------------------- */
    /* ----------------- Functions ---------------- */
    /* -------------------------------------------- */
    @Builder
    public JobApplication(
            ApplicationType applicationType,
            String place,
            Result result,
            String applicationStartDate,
            String applicationCloseDate,
            Member member,
            JobPost jobPost,
            List<ApplicationDetail> applicationDetails
    ) {
        // Relation Column
        this.member = member;
        this.jobPost = jobPost;
        this.applicationDetails = applicationDetails;

        // Information Column
        this.applicationType = applicationType;
        this.place = place;
        this.result = result;
        this.applicationStartDate = applicationStartDate;
        this.applicationCloseDate = applicationCloseDate;
    }

    public void addApplicationDetail(ApplicationDetail applicationDetail) {
        this.applicationDetails.add(applicationDetail);
        applicationDetail.setJobApplication(this);
    }

    public void setJobPost(JobPost jobPost) {
        this.jobPost = jobPost;
        if (!jobPost.getJobApplications().contains(this)) {
            jobPost.getJobApplications().add(this);
        }
    }

    public void update(JobApplicationRequestDTO jobApplicationRequestDTO,
                       ApplicationDetailRepository applicationDetailRepository) {
        if (jobApplicationRequestDTO.applicationType() != null) {
            this.applicationType = ApplicationType.valueOf(jobApplicationRequestDTO.applicationType());
        }
        if (jobApplicationRequestDTO.place() != null) {
            this.place = jobApplicationRequestDTO.place();
        }
        if (jobApplicationRequestDTO.result() != null) {
            this.result = Result.valueOf(jobApplicationRequestDTO.result());
        }
        if (jobApplicationRequestDTO.applicationStartDate() != null) {
            this.applicationStartDate = jobApplicationRequestDTO.applicationStartDate();
        }
        if (jobApplicationRequestDTO.applicationCloseDate() != null) {
            this.applicationCloseDate = jobApplicationRequestDTO.applicationCloseDate();
        }

        // ApplicationDetail 업데이트
        for (ApplicationDetailRequestDTO detailRequestDTO : jobApplicationRequestDTO.applicationDetails()) {
            if (detailRequestDTO.id() != null) {
                // 기존 ApplicationDetail 업데이트
                ApplicationDetail applicationDetail = applicationDetailRepository.findById(detailRequestDTO.id())
                        .orElseThrow(() -> new RuntimeException(
                                "ApplicationDetail not found with ID: " + detailRequestDTO.id()));
                applicationDetail.update(detailRequestDTO);
            } else {
                // 새로운 ApplicationDetail 추가
                ApplicationDetail applicationDetail = detailRequestDTO.toEntity(this);
                applicationDetailRepository.save(applicationDetail);
                this.addApplicationDetail(applicationDetail);
            }
        }
    }
}
