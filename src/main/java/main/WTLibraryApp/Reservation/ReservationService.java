package main.WTLibraryApp.Reservation;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import main.WTLibraryApp.Book.Book;
import main.WTLibraryApp.User.User;

@Service
public class ReservationService {
	
	@Autowired
	private ReservationRepository repo;
	
	public List<Reservation> allReservations() {
		return repo.findAll();
	}
	
	public Optional<Reservation> findById(long id) {
		return repo.findById(id);
	}
	
	public void saveReservation(Reservation newReservation) {
		repo.save(newReservation);
	}
	
	public void deleteReservation(Reservation reservation) {
		repo.delete(reservation);
	}

	public List<Reservation> reservationsByUserId(Reservation userReservation) {
		return repo.findAll(Example.of(userReservation));
	}
	
	public List<Reservation> findByBookAndUser(Book book, User user){
		return repo.findByBookAndUser(book, user);
	}
	
}
