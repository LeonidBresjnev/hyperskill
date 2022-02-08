package platform;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Data
@AllArgsConstructor
public class simpleCode {
    String code;
    int time;
    int views;

    String getCode() {
        return code;
    }
    int getTime() {return time;}
    int getViews() { return views;}

    public void setCode(String code) {
        this.code = code;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
