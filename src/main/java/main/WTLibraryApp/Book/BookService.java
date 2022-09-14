package main.WTLibraryApp.Book;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookService {
	
	@Autowired
	private BookRepository repo;

    public List<Book> findByKeyword(String keyword) {
        return repo.findByKeyword(keyword);
    }

	public List<Book> findAll() {
		return repo.findAll();
	}
	
	public void saveBook(Book book) {
		repo.save(book);
	}
	
	public Optional<Book> find(long id) {
		return repo.findById(id);
	}
	
	public void delete(Book book) {
		repo.delete(book);
	}
	
	public List<Book> findBookByUserId(long userId)
	{
		return repo.findBookByUserId(userId);
	}
	
	public List<Book> findBookByReservationUserId(long userId)
	{
		return repo.findBookByReservationUserId(userId);
	}
}
