import java.util.*;

enum SeatType {
    SILVER, GOLD, PLATINUM
}

enum BookingStatus {
    CONFIRMED, CANCELLED, PENDING
}

class Movie {
    int id;
    String name;

    Movie(int id, String name) {
        this.id = id;
        this.name = name;
    }
}

class Seat {
    int id;
    int row;
    int col;
    SeatType type;

    Seat(int id, int row, int col, SeatType type) {
        this.id = id;
        this.row = row;
        this.col = col;
        this.type = type;
    }
}

class Screen {
    int id;
    List<Seat> seats = new ArrayList<>();

    Screen(int id, List<Seat> seats) {
        this.id = id;
        this.seats = seats;
    }
}

class Theatre {
    int id;
    String name;
    String city;
    List<Screen> screens = new ArrayList<>();

    Theatre(int id, String name, String city, List<Screen> screens) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.screens = screens;
    }
}

class ShowSeat {
    Seat seat;
    boolean isBooked;
    double price;

    ShowSeat(Seat seat, double price) {
        this.seat = seat;
        this.price = price;
        this.isBooked = false;
    }
}

class Show {
    int id;
    Movie movie;
    Screen screen;
    Map<Integer, ShowSeat> showSeats = new HashMap<>();

    Show(int id, Movie movie, Screen screen) {
        this.id = id;
        this.movie = movie;
        this.screen = screen;

        for (Seat seat : screen.seats) {
            showSeats.put(seat.id, new ShowSeat(seat, 200));
        }
    }
}

class User {
    int id;
    String name;

    User(int id, String name) {
        this.id = id;
        this.name = name;
    }
}

class Booking {
    int id;
    User user;
    Show show;
    List<ShowSeat> seats;
    BookingStatus status;

    Booking(int id, User user, Show show, List<ShowSeat> seats) {
        this.id = id;
        this.user = user;
        this.show = show;
        this.seats = seats;
        this.status = BookingStatus.CONFIRMED;
    }
}

class BookingService {

    public Booking createBooking(User user, Show show, List<Integer> seatIds) {

        List<ShowSeat> selectedSeats = new ArrayList<>();

        for (int seatId : seatIds) {
            ShowSeat ss = show.showSeats.get(seatId);
            if (ss == null || ss.isBooked) {
                throw new RuntimeException("Seat not available: " + seatId);
            }
            selectedSeats.add(ss);
        }

        for (ShowSeat ss : selectedSeats) {
            ss.isBooked = true;
        }

        return new Booking(new Random().nextInt(1000), user, show, selectedSeats);
    }

    public void cancelBooking(Booking booking) {
        for (ShowSeat ss : booking.seats) {
            ss.isBooked = false;
        }
        booking.status = BookingStatus.CANCELLED;
    }
}

public class MovieBookingSystem {

    public static void main(String[] args) {

        Movie movie = new Movie(1, "Avengers");

        List<Seat> seats = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            seats.add(new Seat(i, 1, i, SeatType.GOLD));
        }

        Screen screen = new Screen(1, seats);
        Theatre theatre = new Theatre(1, "PVR", "Bangalore", List.of(screen));

        Show show = new Show(1, movie, screen);

        User user = new User(1, "Amishi");

        BookingService bookingService = new BookingService();

        Booking booking = bookingService.createBooking(user, show, List.of(1, 2, 3));

        System.out.println("Booking Confirmed for seats: " + booking.seats.size());

        bookingService.cancelBooking(booking);
        System.out.println("Booking Status: " + booking.status);
    }
}