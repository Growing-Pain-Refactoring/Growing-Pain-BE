package cotato.growingpain.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import cotato.growingpain.dto.request.JobApplicationRequestDTO;
import cotato.growingpain.dto.request.JobPostRequestDTO;
import cotato.growingpain.infrastructure.repository.ApplicationDetailRepository;
import cotato.growingpain.infrastructure.repository.JobApplicationRepository;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Getter
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JobPost {
    /* -------------------------------------------- */
    /* -------------- Default Column -------------- */
    /* -------------------------------------------- */
    @Id
    @Column(name = "job_post")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* -------------------------------------------- */
    /* ------------ Information Column ------------ */
    /* -------------------------------------------- */
    @Column(name = "company_name")
    private String companyName;

    @Column(name = "job_part")
    private String jobPart;

    /* -------------------------------------------- */
    /* -------------- Relation Column ------------- */
    /* -------------------------------------------- */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @JsonIgnore
    private Member member;

    @OneToMany(mappedBy = "jobPost", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<JobApplication> jobApplications = new ArrayList<>();

    /* -------------------------------------------- */
    /* ----------------- Functions ---------------- */
    /* -------------------------------------------- */
    @Builder
    public JobPost(
            @NotBlank String companyName,
            @NotBlank String jobPart,
            Member member,
            List<JobApplication> jobApplications
    ) {
        // Relation Column
        this.member = member;
        this.jobApplications = jobApplications;

        // Information Column
        this.companyName = companyName;
        this.jobPart = jobPart;
    }

    public void addJobApplication(JobApplication jobApplication) {
        this.jobApplications.add(jobApplication);
        jobApplication.setJobPost(this);
    }

    public void update(JobPostRequestDTO jobPostRequestDTO, JobApplicationRepository jobApplicationRepository,
                       ApplicationDetailRepository applicationDetailRepository) {
        if (jobPostRequestDTO.companyName() != null) {
            this.companyName = jobPostRequestDTO.companyName();
        }
        if (jobPostRequestDTO.jobPart() != null) {
            this.jobPart = jobPostRequestDTO.jobPart();
        }

        // JobApplication 리스트 업데이트
        for (JobApplicationRequestDTO jobApplicationRequestDTO : jobPostRequestDTO.jobApplications()) {
            if (jobApplicationRequestDTO.id() != null) {
                // 기존 JobApplication 업데이트
                JobApplication jobApplication = jobApplicationRepository.findById(jobApplicationRequestDTO.id())
                        .orElseThrow(() -> new RuntimeException(
                                "JobApplication not found with ID: " + jobApplicationRequestDTO.id()));
                jobApplication.update(jobApplicationRequestDTO, applicationDetailRepository);
            } else {
                // 새로운 JobApplication 추가
                JobApplication jobApplication = jobApplicationRequestDTO.toEntity(this.member, this);
                jobApplicationRepository.save(jobApplication);
                this.addJobApplication(jobApplication);
            }
        }
    }

}
