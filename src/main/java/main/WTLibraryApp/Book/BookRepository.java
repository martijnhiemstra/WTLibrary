package main.WTLibraryApp.Book;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>{
	
	List<Book> findByTitle(String title);

	//finds all book loaned by one user
	@Query(value = "SELECT * FROM books WHERE book_id IN \r\n"
			+ "(SELECT book_id FROM copies WHERE loaned_by_user LIKE :userId)", nativeQuery = true)
	List<Book> findBookByUserId(long userId);
	
	//finds all book reserved by one user
	@Query(value = "SELECT * FROM books WHERE book_id IN\r\n"
			+ "(SELECT book_id FROM reservations WHERE user_id LIKE :userId)", nativeQuery = true)
	List<Book> findBookByReservationUserId(long userId);

	//finds book by keyword
    @Query(value = "select * from books where title like %:keyword% or author like %:keyword% or isbn like %:keyword%", nativeQuery = true)
    List<Book> findByKeyword(@Param("keyword") String keyword);
}
