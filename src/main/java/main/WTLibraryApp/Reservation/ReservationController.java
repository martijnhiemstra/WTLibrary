package main.WTLibraryApp.Reservation;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import main.WTLibraryApp.Book.Book;
import main.WTLibraryApp.Book.BookService;
import main.WTLibraryApp.LibMail.EmailService;
import main.WTLibraryApp.Transaction.TransactionService;
import main.WTLibraryApp.Transaction.TransactionType;
import main.WTLibraryApp.User.LoanedUser;
import main.WTLibraryApp.User.User;
import main.WTLibraryApp.User.UserService;


@Controller
@CrossOrigin(maxAge=3600)
public class ReservationController {
	
	@Autowired
	private ReservationService reservationService;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private BookService bookService;
	
	@Autowired
	private TransactionService transactionService;
	
	// list all entries from reservations table
	// returns list of Reservation objects
	@GetMapping(value = "/reservations")
	public String findAllReservations(Model model) {
		model.addAttribute("reservations", reservationService.allReservations());
		return "/reservations/reservations";
	}
	
	@GetMapping(value = "/reservations/user/{id}")
	public String reservationsByUser(@PathVariable long id, Model model) {
		User user = userService.findUser(id);
		
		Reservation userRes = new Reservation();
		userRes.setUser(user);
		model.addAttribute("reservation", reservationService.reservationsByUserId(userRes));
		return "/reservations/user";
	}
	
	// Creates a new reservation in the reservations table
//	@GetMapping("reservations/create")
//	public String createReservation(Reservation reservation) {
//		return "reservations/confirmReservation";
//	}
//	
//	@PostMapping("reservations/create")
//	public String createReservation(@CurrentSecurityContext(expression = "authentication") Authentication authentication,Reservation reservation, BindingResult result, Model model) {
//		User currentUser = userService.findByEmail(authentication.getName());
//		//send email
//		User user = userService.findUser(reservation.getUserId());
//		Book book = bookService.find(reservation.getBookId());
//		emailService.sendSimpleMessage(user.getEmail(), "Reserved " + book.getTitle(), "Dear " + user.getFirst_name() + " " + user.getLast_name() + ",\nYou seem to believe we will help you get your hands on "+ book.getTitle()+" written by "+book.getAuthor()+". People can believe anything these days I suppose. Well..\nSee you!\n"+currentUser.getFirst_name()+" "+currentUser.getLast_name());
//		
//		if (result.hasErrors()) {
//			return "reservations/confirmReservation";
//		}
//		service.saveReservation(reservation);
//		return "redirect:/reservations";
//	}
	
	@GetMapping("reservations/createReservation/{bookId}")
	public String createReservation(@PathVariable long bookId, @CurrentSecurityContext(expression = "authentication") Authentication authentication, Model model) {
		Optional<Book> bookOptional = bookService.find(bookId);
		if (bookOptional.isPresent()) {
			Book book = bookOptional.get();

			//get logged-in user
			User currentUser = userService.findByEmail(authentication.getName());
			long userId = currentUser.getId();
			
			//check if book is already reserved
	    	List<Reservation> reservations = reservationService.findByBookAndUser(book, currentUser);
	    	if(reservations.size() <= 0) {
	    		// Initialization
	    		User user = userService.findUser(userId);
	    		
	    		if (bookOptional.isPresent()) {
		    		// Create object
					Reservation reservation = new Reservation();
					reservation.setBook(book);
					reservation.setUser(user);
					reservationService.saveReservation(reservation);
					
					// Send email
					emailService.sendSimpleMessage(user.getEmail(), "Reserved " + book.getTitle(), "Dear " + user.getFirst_name() + " " + user.getLast_name() + ",\nYou seem to believe we will help you get your hands on "+ book.getTitle()+" written by "+book.getAuthor()+". People can believe anything these days I suppose. Well..\nSee you!\n"+currentUser.getFirst_name()+" "+currentUser.getLast_name());
		
					LoanedUser.setCurrentUserId(userId);
		
					//log in transactions table
					transactionService.logReservation(user, book, TransactionType.RESERVED);
	    		}
	    	}
		}

		String path = "redirect:/books";
		return path;
	}
	
	// Cancels a user's reservations by removing it from the reservations table
	@GetMapping("reservations/cancel/{bookId}")
	public String cancelReservation(@PathVariable long bookId, @CurrentSecurityContext(expression = "authentication") Authentication authentication) {
		
		Optional<Book> optional = bookService.find(bookId);
		if (optional.isPresent()) {
			Book book = optional.get();

			//get logged-in user
			User currentUser = userService.findByEmail(authentication.getName());
			
			List<Reservation> reservation = reservationService.findByBookAndUser(book, currentUser);
			reservationService.deleteReservation(reservation.get(0));
		}
	    
		String path = "redirect:/books";
		return path;
	}
	
	// Cancels a user's reservations by removing it from the reservations table
	@GetMapping("reservations/cancelUI/{id}")
	public String cancelReservationUserInterface(@PathVariable long id, @CurrentSecurityContext(expression = "authentication") Authentication authentication) {
		Optional<Reservation> reservationOptional = reservationService.findById(id);
		if (reservationOptional.isPresent()) {
			Reservation reservation = reservationOptional.get();
			reservationService.deleteReservation(reservation);

		    if(authentication.getAuthorities().contains(new SimpleGrantedAuthority("1"))) {
		    	return "redirect:/users/edit-user/" + id;
		    }
		    else {
		    	return "redirect:/user";
		    }
		}
		
    	return "redirect:/user";
	}
		
	/*
	 * find book by key word
	 * inputs key word
	 */
//	@RequestMapping(method = RequestMethod.POST, value = "books/search")
//	public List<Book> search(@RequestBody Search bookSearch) {
//		String keyWord = bookSearch.getKeyWord();
//		return service.searchBook(keyWord, keyWord, keyWord);
//	}

}
