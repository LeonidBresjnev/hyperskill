package cinema;

public class TokenWithTicket {
    private String token;
    private SeatWithPrice ticket;
    TokenWithTicket(String s, SeatWithPrice t) {
        this.token = s;
        this.ticket = t;
    }

    public String getToken() {
        return token;
    }

    public SeatWithPrice getTicket() {
        return ticket;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setTicket(SeatWithPrice seat) {
        this.ticket = seat;
    }
}
