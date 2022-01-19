package cinema;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
class SoldException extends RuntimeException {
    public SoldException(String cause) {
        super(cause);
    }
}

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
class WrongTokenException extends RuntimeException {
    public WrongTokenException(String cause) {
        super(cause);
    }
}

@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
class WrongPassword extends RuntimeException {
    public WrongPassword(String cause) {super(cause);
    }
}



@RestController
public class TaskController {
    Cinema cinema = new Cinema(9, 9);
    int income = 0;
    int available = cinema.getTotal_columns() * cinema.getTotal_rows();
    int sold = 0;

    @ExceptionHandler(SoldException.class)
    public ResponseEntity<Error> handleException(SoldException e) {
        Error error = new Error(HttpStatus.BAD_REQUEST, e.getLocalizedMessage());
        return new ResponseEntity<>(error, error.getHttpStatus());
    }

    @ExceptionHandler(WrongTokenException.class)
    public ResponseEntity<WrongToken> handleException2(WrongTokenException e) {
        WrongToken error = new WrongToken(e.getLocalizedMessage());
        return new ResponseEntity<WrongToken>(error, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(WrongPassword.class)
    public ResponseEntity<WrongToken> handleException2(WrongPassword e) {
        WrongToken error = new WrongToken(e.getLocalizedMessage());
        return new ResponseEntity<WrongToken>(error, HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/seats")
    public Cinema returnTask() {
        return this.cinema;
    }

    @PostMapping("/stats")
    public Map<String, Integer> returnStat(@RequestParam(value = "password", required = false) String password ) {
        if ("super_secret".equals(password)){
            return Map.of("current_income", this.income,
                    "number_of_available_seats", this.available,
                    "number_of_purchased_tickets", this.sold);
        }
        throw new WrongPassword("The password is wrong!");
    }


    @PostMapping("/purchase")
    public TokenWithTicket userStatus(@RequestBody Seat yourseat)  {
        if (yourseat.row < 1 || yourseat.row > cinema.getTotal_rows()
        || yourseat.column < 1 || yourseat.column > cinema.getTotal_columns()) {
            throw new SoldException("The number of a row or a column is out of bounds!");
        } else if (cinema.free(yourseat.row, yourseat.column)) {
            //income += new Se
            TokenWithTicket myseat = cinema.book(yourseat.row, yourseat.column);
            income += myseat.getTicket().getPrice();
            available--;
            sold++;
            return myseat;
        } else {
            throw new SoldException("The ticket has been already purchased!");
        }
    }

    @PostMapping("/return")
    public Map<String,SeatWithPrice> returnTicket(@RequestBody TokenWithTicket ticket)  {

        if (cinema.tokenExists(ticket.getToken())) {
            sold--;
            available++;
            Map<String,SeatWithPrice> myseat = cinema.returnTicket(ticket.getToken());

            income -= myseat.get("returned_ticket").getPrice();
            return myseat;
        } else {
            throw new WrongTokenException("Wrong token!");
        }
    }
}