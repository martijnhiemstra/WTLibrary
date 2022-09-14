package main.WTLibraryApp.Transaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import main.WTLibraryApp.Book.Book;
import main.WTLibraryApp.Book.Copy.Copy;
import main.WTLibraryApp.User.User;

@Service
public class TransactionService {
	
	@Autowired
	private TransactionRepository repo;
	
	public void logReservation(User user, Book book, TransactionType type) {
		Transaction newTransaction = new Transaction(user, type);
		newTransaction.setBook(book);
		repo.save(newTransaction);
	}
	
	public void logLoan(User user, Copy copy, TransactionType type) {
		Transaction newTransaction = new Transaction(user, type);
		System.out.println(copy.getBook().getId() + "." + copy.getId());
		newTransaction.setBook(copy.getBook());
		newTransaction.setCopy(copy);
		repo.save(newTransaction);
	}
	
	
//	public void logLoan(long user_id, CopyPK id,TransactionType type) {
//		Transaction newTransaction = new Transaction(user_id, type);
//		newTransaction.setCopy_id(id.getCopyId());
//		repo.save(newTransaction);
//	}
}
