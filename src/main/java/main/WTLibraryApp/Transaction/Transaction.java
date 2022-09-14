package main.WTLibraryApp.Transaction;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import main.WTLibraryApp.Book.Book;
import main.WTLibraryApp.Book.Copy.Copy;
import main.WTLibraryApp.User.User;

@Entity
@Table(name="transactions")
public class Transaction {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long transaction_id;
	
	@ManyToOne(optional = false)
	private Book book;

	@ManyToOne(optional = true)
	private Copy copy;

	@ManyToOne(optional = false)
	private User user;

	@Enumerated(EnumType.ORDINAL)
	private TransactionType transaction_type;
	
	// constructor
	public Transaction() {
		
	}
	
	public Transaction(User user, TransactionType transaction_type) {
		this.user = user;
		this.transaction_type = transaction_type;
	}

	public long getTransaction_id() {
		return transaction_id;
	}

	public void setTransaction_id(long transaction_id) {
		this.transaction_id = transaction_id;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public Copy getCopy() {
		return copy;
	}

	public void setCopy(Copy copy) {
		this.copy = copy;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public TransactionType getTransaction_type() {
		return transaction_type;
	}

	public void setTransaction_type(TransactionType transaction_type) {
		this.transaction_type = transaction_type;
	}

}
