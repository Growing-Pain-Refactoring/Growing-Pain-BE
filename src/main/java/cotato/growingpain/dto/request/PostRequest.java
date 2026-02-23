package cotato.growingpain.dto.request;

import cotato.growingpain.domain.enums.PostCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public record PostRequest(

        @NotBlank(message = "제목은 필수 항목입니다.")
        @Size(max = 50)
        String title,

        @NotBlank(message = "내용은 필수 항목입니다.")
        @Size(max = 3000)
        String content,

        List<MultipartFile> postImages,

        PostCategory category
){
}