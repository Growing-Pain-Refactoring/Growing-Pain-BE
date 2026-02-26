package cotato.growingpain.common.exception;

import java.io.IOException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class FileException extends IOException {
    ErrorCode errorCode;
}
