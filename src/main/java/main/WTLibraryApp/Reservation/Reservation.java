package main.WTLibraryApp.Reservation;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import main.WTLibraryApp.Book.Book;
import main.WTLibraryApp.User.User;

@Entity
@Table(name="reservations")
public class Reservation {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long reservationId;

	@ManyToOne(optional = false)
	private Book book;

	@ManyToOne(optional = false)
	private User user;

	public long getReservationId() {
		return reservationId;
	}

	public void setReservationId(long reservationId) {
		this.reservationId = reservationId;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public Reservation() {
		
	}

	public Reservation(long reservationId, Book book, User user) {
		super();
		this.reservationId = reservationId;
		this.book = book;
		this.user = user;
	}
	
}
