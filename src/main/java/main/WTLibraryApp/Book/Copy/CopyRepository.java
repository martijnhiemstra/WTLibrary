package main.WTLibraryApp.Book.Copy;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import main.WTLibraryApp.Book.Book;


@Repository
public interface CopyRepository extends JpaRepository<Copy, Long>{
	
	List<Copy> findCopyByUserId(Long id);

	List<Copy> findByBookAndVersion(Book book, int version);

	List<Copy> findByBook(Book book);

}
 