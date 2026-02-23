package cotato.growingpain.domain.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import cotato.growingpain.dto.request.ApplicationDetailRequestDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Getter
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApplicationDetail {
    /* -------------------------------------------- */
    /* -------------- Default Column -------------- */
    /* -------------------------------------------- */
    @Id
    @Column(name = "application_detail_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* -------------------------------------------- */
    /* ------------ Information Column ------------ */
    /* -------------------------------------------- */
    @Column(name = "title")
    private String title;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    /* -------------------------------------------- */
    /* -------------- Relation Column ------------- */
    /* -------------------------------------------- */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_application_id")
    @JsonBackReference
    @JsonIgnore
    private JobApplication jobApplication;

    @Builder
    public ApplicationDetail(
            String title,
            String content,
            JobApplication jobApplication
    ) {
        // Relation Column
        this.jobApplication = jobApplication;

        // Information Column
        this.title = title;
        this.content = content;
    }

    public void setJobApplication(JobApplication jobApplication) {
        this.jobApplication = jobApplication;
        if (!jobApplication.getApplicationDetails().contains(this)) {
            jobApplication.getApplicationDetails().add(this);
        }
    }

    public void update(ApplicationDetailRequestDTO detailRequestDTO) {
        if (detailRequestDTO.title() != null) {
            this.title = detailRequestDTO.title();
        }
        if (detailRequestDTO.content() != null) {
            this.content = detailRequestDTO.content();
        }
    }


}
