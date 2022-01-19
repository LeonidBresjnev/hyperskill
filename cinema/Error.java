package cinema;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@RequiredArgsConstructor
public class Error {
    private HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
    private String error = "";
    Error(HttpStatus h, String s) {
        this.httpStatus = h;
        this.error = s;
    }

    public String getError() {
        return error;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
