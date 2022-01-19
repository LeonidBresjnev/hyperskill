package cinema;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class WrongToken {
    private String error = "";
    WrongToken(String s) {
        this.error = s;
    }

    public void setError(String error) {
        this.error = error;
    }
    public String getError() {
        return error;
    }
}
