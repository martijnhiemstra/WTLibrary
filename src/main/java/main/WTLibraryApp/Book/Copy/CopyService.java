package main.WTLibraryApp.Book.Copy;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import main.WTLibraryApp.Book.Book;
import main.WTLibraryApp.Transaction.TransactionRepository;

@Service
public class CopyService {
	
	@Autowired
	private CopyRepository repo;
	
	@Autowired
	private TransactionRepository transactionRepository;
	
	public List<Copy> allCopies(){
		return repo.findAll();
	}
	
	public List<Copy> findByBookAndCopy(Book book, int version) {
		return repo.findByBookAndVersion(book, version);
	} 

	public Optional<Copy> findCopyById(long id) {
		return repo.findById(id);
	} 

	public void deleteCopy(Copy copy) {
		copy.getTransactions().stream().forEach(transaction -> {
			transaction.setCopy(null);
			transactionRepository.save(transaction);
		});

		repo.delete(copy);
	}
	
	public void updateCopy(Copy copy) {
		repo.save(copy);
	}
	
	public void saveCopy(Copy copy) {
		repo.save(copy);
	}
	
//	public List<Copy> findCopyByReservationUserId(Long userId){
//		return repo.findCopyByReservationUserId(userId);
//	}
}
