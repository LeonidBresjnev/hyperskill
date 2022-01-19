package cinema;

import java.util.*;

public class Cinema {
    private final int total_rows;
    private final int total_columns;
    private final Map<Integer,SeatWithPrice> available_seats;
    private final Map<String, TokenWithTicket> soldtickets;
    //public int income = 0;
    //public int availableseats;

    Cinema(int r, int c) {
        this.total_columns = c;
        this.total_rows = r;
        //availableseats = this.total_columns * this.total_rows;
        available_seats = new HashMap<>();
        soldtickets = new HashMap<>();
        for (r = 1; r <= 9; r++) {
            for (c = 1; c <= 9; c++) {
                available_seats.put(100 * r + c, new SeatWithPrice(r, c, r <= 4 ? 10 : 8));
            }
        }
    }

    public int getTotal_rows() {
        return total_rows;
    }
    public int getTotal_columns() {
        return total_columns;
    }

    public Set<SeatWithPrice> getAvailable_seats() {
        return  new HashSet<>(available_seats.values());
    }

    public boolean free(int r, int c) {
        return available_seats.containsKey(100 * r + c);
    }

    public TokenWithTicket book(int r, int c) {
        //availableseats--;
        SeatWithPrice ticket = available_seats.remove(100 * r + c);
        TokenWithTicket soldticket = new TokenWithTicket(UUID.randomUUID().toString(), ticket);
        soldtickets.put(soldticket.getToken(), soldticket);
        return soldticket;
    }

    public boolean tokenExists(String token) {
        return soldtickets.containsKey(token);
    }

    public Map<String,SeatWithPrice> returnTicket(String token) {
        //availableseats++;
        return Map.of("returned_ticket", soldtickets.get(token).getTicket());
    }
}
