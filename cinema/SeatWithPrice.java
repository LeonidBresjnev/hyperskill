package cinema;

public class SeatWithPrice extends Seat{
    private int price;
    SeatWithPrice(int r, int c, int price) {
        super(r, c);
        this.price = price;
    }
    SeatWithPrice(Seat seat, int price) {
        super(seat.row, seat.column);
        this.price = price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getPrice() {
        return price;
    }
}
