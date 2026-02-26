package cotato.growingpain.infrastructure.s3;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;
import cotato.growingpain.common.exception.ErrorCode;
import cotato.growingpain.common.exception.FileException;
import cotato.growingpain.common.exception.ImageException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor    // final 멤버변수가 있으면 생성자 항목에 포함시킴
@Component
@Service
public class S3Uploader {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    // 이미지 파일 S3 업로드
    public String uploadImageFileToS3(MultipartFile multipartFile, String dirName) throws ImageException {
        log.info("{} 사진 업로드", multipartFile.getOriginalFilename());
        File uploadFile = convertImageFile(multipartFile)
                .orElseThrow(() -> new ImageException(ErrorCode.IMAGE_PROCESSING_FAIL));
        return upload(uploadFile, dirName);
    }

    // PDF 등 일반 파일 S3 업로드
    public String uploadFileToS3(MultipartFile multipartFile, String dirName) throws FileException {
        log.info("{} 파일 업로드", multipartFile.getOriginalFilename());
        File uploadFile = convertFile(multipartFile)
                .orElseThrow(() -> new FileException(ErrorCode.FILE_PROCESSING_FAIL));
        return upload(uploadFile, dirName);
    }

    public String upload(File uploadFile, String filePath) {
        String fileName = filePath + "/" + UUID.randomUUID() + uploadFile.getName();
        String uploadFileUrl = putS3(uploadFile, fileName);

        removeNewFile(uploadFile);  // 로컬에 생성된 File 삭제 (MultipartFile -> File 전환 하며 로컬에 파일 생성됨)

        return uploadFileUrl;      // 업로드된 파일의 S3 URL 주소 반환
    }

    //S3로 업로드
    private String putS3(File uploadFile, String fileName) {
        amazonS3Client.putObject(
                new PutObjectRequest(bucket, fileName, uploadFile)
                //.withCannedAcl(CannedAccessControlList.PublicRead)    // PublicRead 권한으로 업로드 됨
        );
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    //로컬에 저장된 파일 지우기
    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            log.info("파일이 삭제되었습니다.");
        } else {
            log.info("파일이 삭제되지 못했습니다.");
        }
    }

    private Optional<File> convertImageFile(MultipartFile file) throws ImageException {
        File convertFile = new File(System.getProperty("user.dir") + "/" + file.getOriginalFilename());
        try {
            if (convertFile.createNewFile()) { // 바로 위에서 지정한 경로에 File이 생성됨 (경로가 잘못되었다면 생성 불가능)
                FileOutputStream fos = new FileOutputStream(convertFile); // FileOutputStream 데이터를 파일에 바이트 스트림으로 저장하기 위함
                fos.write(file.getBytes());
                fos.close();
                return Optional.of(convertFile);
            }
        } catch (IOException e) {
            throw new ImageException(ErrorCode.IMAGE_PROCESSING_FAIL);
        }
        return Optional.empty();
    }

    private Optional<File> convertFile(MultipartFile file) throws FileException {
        File convertFile = new File(System.getProperty("user.dir") + "/" + file.getOriginalFilename());
        try {
            if (convertFile.createNewFile()) { // 바로 위에서 지정한 경로에 File이 생성됨 (경로가 잘못되었다면 생성 불가능)
                FileOutputStream fos = new FileOutputStream(convertFile); // FileOutputStream 데이터를 파일에 바이트 스트림으로 저장하기 위함
                fos.write(file.getBytes());
                fos.close();
                return Optional.of(convertFile);
            }
        } catch (IOException e) {
            throw new FileException(ErrorCode.FILE_PROCESSING_FAIL);
        }
        return Optional.empty();
    }
}
