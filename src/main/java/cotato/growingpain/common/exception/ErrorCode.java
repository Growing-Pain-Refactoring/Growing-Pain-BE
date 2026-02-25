package cotato.growingpain.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    //Todo: message 없애고 status 없애고? 코드로 바꾸기?

    //회원가입
    EMAIL_DUPLICATED(HttpStatus.CONFLICT, "존재하는 이메일입니다."),
    NICKNAME_DUPLICATED(HttpStatus.CONFLICT, "존재하는 닉네임입니다."),
    WRONG_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 틀렸습니다."),
    ACCESS_DENIED_USER(HttpStatus.FORBIDDEN,"권한이 없는 유저입니다."),

    EMAIL_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 이메일이 존재하지 않습니다."),
    LOGIN_FAIL(HttpStatus.UNAUTHORIZED, "로그인 요청에 실패했습니다."),
    JWT_NOT_EXISTS(HttpStatus.BAD_REQUEST, "Jwt 토큰이 존재하지 않습니다."),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND,"해당 유저를 찾을 수 없습니다."),
    USER_ALREADY_EXISTS(HttpStatus.BAD_REQUEST,"이미 존재하는 유저입니다."),
    EXPIRED_TOKEN(HttpStatus.BAD_REQUEST, "토큰이 만료되었습니다."),
    INVALID_JWT(HttpStatus.UNAUTHORIZED, "해당 jwt가 DB에 존재하지 않습니다."),
    INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "."),
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 refresh 토큰이 DB에 존재하지 않습니다."),
    REISSUE_FAIL(HttpStatus.UNAUTHORIZED, "액세스 토큰 재발급 요청 실패"),

    //커뮤니티
    POST_NOT_FOUND(HttpStatus.NOT_FOUND,"해당 게시글 정보를 찾을 수 없습니다."),
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND,"해당 댓글 정보를 찾을 수 없습니다."),
    REPLY_COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND,"해당 답글 정보를 찾을 수 없습니다."),
    ALREADY_LIKED(HttpStatus.CONFLICT, "이미 좋아요를 눌렀습니다."),
    POST_LIKE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 게시글의 좋아요 정보를 찾을 수 없습니다."),
    COMMENT_LIKE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 댓글의 좋아요 정보를 찾을 수 없습니다."),
    REPLY_COMMENT_LIKE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 답글의 좋아요 정보를 찾을 수 없습니다."),
    CANNOT_LIKE_OWN_POST(HttpStatus.BAD_REQUEST, "본인이 작성한 게시글은 좋아요를 누를 수 없습니다."),
    CANNOT_LIKE_OWN_COMMENT(HttpStatus.BAD_REQUEST, "본인이 작성한 댓글은 좋아요를 누를 수 없습니다."),
    CANNOT_LIKE_OWN_REPLY_COMMENT(HttpStatus.BAD_REQUEST, "본인이 작성한 답글은 좋아요를 누를 수 없습니다."),
    ALREADY_DELETED(HttpStatus.CONFLICT, "이미 삭제되었습니다."),
    ALREADY_SAVED(HttpStatus.CONFLICT, "이미 저장되었습니다."),

    //S3
    IMAGE_PROCESSING_FAIL(HttpStatus.INTERNAL_SERVER_ERROR,"이미지 처리에 실패했습니다."),

    //자소서
    INVALID_FILE_EXTENSION(HttpStatus.BAD_REQUEST, "PDF 파일만 업로드 가능합니다."),
    COVER_LETTER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 자소서를 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
