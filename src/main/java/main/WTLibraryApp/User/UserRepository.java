package main.WTLibraryApp.User;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
	@Query(value = "select * from users where first_name like %:keyword% or last_name like %:keyword% or email like %:keyword%", nativeQuery = true)
	public List<User> findByKeyword(@Param("keyword") String keyword);
	
	public Optional<User> findByEmail(String email);
	
}
  