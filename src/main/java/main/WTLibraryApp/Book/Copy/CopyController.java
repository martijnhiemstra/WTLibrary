package main.WTLibraryApp.Book.Copy;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import main.WTLibraryApp.Book.Book;
import main.WTLibraryApp.Book.BookService;
import main.WTLibraryApp.Reservation.Reservation;
import main.WTLibraryApp.Reservation.ReservationService;
import main.WTLibraryApp.Transaction.TransactionService;
import main.WTLibraryApp.Transaction.TransactionType;
import main.WTLibraryApp.User.LoanedUser;
import main.WTLibraryApp.User.User;
import main.WTLibraryApp.User.UserService;

@Controller 
@CrossOrigin(maxAge=3600)
public class CopyController {

	@Autowired
	private BookService bookService;

	@Autowired
	private CopyService copyService;

	@Autowired
	private UserService userService;

	@Autowired
	private ReservationService reservationService;
	
	@Autowired
	private TransactionService transactionService;
 	
	//displays all copies in the database
  	@GetMapping(value = "copies")
	public String findAll(Model model){
		model.addAttribute("copies", copyService.allCopies());
		return "copies/copies";  
	}   
	
  	//displays all copies by bookId and copyId combination
	@GetMapping(value = "copies/{bookId}/{copyId}")
	public String findById(@PathVariable long bookId, @PathVariable long copyId, Model model) {
		Optional<Book> bookOptional = bookService.find(bookId);

		if (bookOptional.isPresent()) {
			Book book = bookOptional.get(); 
			
			model.addAttribute("copies", copyService.findByBookAndCopy(book, 1));
		} else {
			model.addAttribute("copies", new ArrayList<>());
		}
		return "copies/copyInterface";                           
	}       
	
	//deletes copy by bookId and copyId combination in the book interface
	@GetMapping("copies/delete/{id}")
	public String deleteInBook(@PathVariable long id) {
		Optional<Copy> copyOptional = copyService.findCopyById(id);
		if (copyOptional.isPresent()) {
			copyService.deleteCopy(copyOptional.get());
		}

		return "redirect:/books/edit/" + id; 
	}  
	
	//administrator withdraws copies of books to users
	@GetMapping("copies/withdraw/{bookId}/{copyId}/{userId}")  
	public String withdrawBook(@PathVariable long bookId, @PathVariable long copyId, @PathVariable long userId, Model model){
		User user = this.userService.findUser(userId);

		Optional<Copy> copyOptional = this.copyService.findCopyById(copyId);
		if (copyOptional.isPresent()) {
			Copy copyToWithdraw = copyOptional.get();
			copyToWithdraw.setUser(null); 
			copyService.saveCopy(copyToWithdraw);

			//log in transactions table
			transactionService.logLoan(user, copyOptional.get(), TransactionType.RETURNED);
		}
		
		return "redirect:/users/edit-user/{userId}";
	}

	//administrator loans copies of books to users
	@GetMapping("copies/loan/{bookId}/{copyId}")  
	public String loanBook(@PathVariable long bookId, @PathVariable long copyId, Model model){
		long currentUserId = LoanedUser.getCurrentUserId();
		User user = userService.findUser(currentUserId);
		Optional<Book> bookOptional = bookService.find(bookId);

		Optional<Copy> copyToLoanOptional = copyService.findCopyById(copyId);
		if (copyToLoanOptional.isPresent() && bookOptional.isPresent()) {
			Copy copyToLoan = copyToLoanOptional.get();
			Book book = bookOptional.get();

			copyToLoan.setUser(user);  
			copyService.saveCopy(copyToLoan); 

			//deletes related reservation
			//in case of duplicate reservations, it deletes the first one
			List<Reservation> reservation = reservationService.findByBookAndUser(book, user);
			reservationService.deleteReservation(reservation.get(0));
			
			//log in transactions table
			transactionService.logLoan(user, null, TransactionType.LOANED);
		}
		
		String path = "redirect:/users/edit-user/" + currentUserId;
		return path;
	}
	
	
	
//	//deletes copy by bookId and copyId combination in the user interface
//	@GetMapping("copies/deleteInUserInterface/{bookId}/{copyId}/{userId}")
//	public String deleteInUser(@PathVariable long bookId, @PathVariable long copyId) {
//			 
//		CopyPK id = new CopyPK(bookId, copyId); 
//		service.deleteCopy(id);             
//		return "redirect:/users/edit-user/{userId}"; 
//	}  
	
//	@GetMapping("copies/create")
//	public String create(Copy copy) {
//		return "copies";
//	}
	
//	@PostMapping("/copies/create")
//	public String create(Copy copy, BindingResult result, Model model) {
//		if (result.hasErrors()) {
//			return "copies";  
//		}   
//   
//		service.saveCopy(copy);                   
//		return "redirect:/copies";                 
//	}    
	 
//	@PostMapping(value = "copies/assign/{bookId}/{copyId}/{userId}")
//	public void assignCopy(@PathVariable long bookId, @PathVariable long copyId, @PathVariable long userId) {
//		
//		// create copy object by combined id 
//		CopyPK id = new CopyPK(bookId, copyId);
//		List<Copy> reservedCopy = service.findCopy(id);
//		
//		// abort if selected id doesn't return a copy
//		if (reservedCopy.isEmpty()) {
//			System.out.println("no copy for id: " + bookId + "." + copyId);
//			return;
//		}
//	
//		// create copy-object from list and set new userId
//		Copy loanedCopy = reservedCopy.get(0);
//		loanedCopy.setUserId(userId);
//		service.updateCopy(loanedCopy);
//	}	
}
	