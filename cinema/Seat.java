package cinema;

public class Seat {
    protected int row;
    protected int column;
    Seat(int r, int c) {
        this.row = r;
        this.column = c;
    }
    public int getRow() {
        return this.row;
    }
    public int getColumn() {
        return this.column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public void setRow(int row) {
        this.row = row;
    }
}
