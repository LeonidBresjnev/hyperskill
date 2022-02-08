package platform;

import lombok.Data;

import java.time.LocalDate;


public class Code {
    private String code;
    private LocalDate date;
    private int time;
    private int views;
    private boolean timerestrictions;
    private boolean viewrestrictions;

    Code(String code, LocalDate date, int time, int views, boolean timerestrictions, boolean viewrestrictions) {
        this.code = code;
        this.date = date;
        this.time = time;
        this.views = views;
        this.timerestrictions = timerestrictions;
        this.viewrestrictions = viewrestrictions;
    }

    public String getCode() {
        return code;
    }
    public LocalDate getDate() {
        return date;
    }

    public int getTime() {
        return time;
    }



    public int getViews() {
        return views;
    }

    public boolean timerestric() {
        return timerestrictions;
    }

    public boolean viewrestric() {
        return viewrestrictions;
    }
}
