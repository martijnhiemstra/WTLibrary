package main.WTLibraryApp.User;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import main.WTLibraryApp.Book.BookService;
import main.WTLibraryApp.Book.Copy.CopyService;
import main.WTLibraryApp.LibMail.EmailService;
import main.WTLibraryApp.Reservation.ReservationService;

@Controller
@CrossOrigin(maxAge=3600)
public class UserController {

	@Autowired
	private UserService service;
	
	@Autowired
	private CopyService copyService;
	
	@Autowired
	private BookService bookService;

	@Autowired
	private EmailService emailService;
	
	@Autowired
	private ReservationService reservationService;
	
//	Returns all users from the users table.
	
	@GetMapping("/users")
	public String showUsers(Model model, User user, String keyword) {
		if (keyword != null) {
			List<User> list = service.findByKeyword(keyword);
			model.addAttribute("users", list);
		} else {
			List<User> list = service.findAllUsers();
			model.addAttribute("users", list);
		}
		return "/users/users";
	}
	
	@GetMapping("/user")
	public String CurrentUser(@CurrentSecurityContext(expression = "authentication") Authentication authentication, Model model) {
		User currentuser = service.findByEmail(authentication.getName());

		model.addAttribute("user", currentuser);
		model.addAttribute("action", "/user");
		model.addAttribute("copies", currentuser.getCopies());
		model.addAttribute("reservedCopies", currentuser.getCopies());
		model.addAttribute("reservations", currentuser.getReservations());
		
		LoanedUser.setCurrentUserId(currentuser.getId());

		return "users/userInterface";
	}
	
	//edits user in the table
	@PostMapping("/user")
	public String updateCurrentUserPost(@CurrentSecurityContext(expression = "authentication") Authentication authentication, User users, BindingResult result, Model model) {
		User currentuser = service.findByEmail(authentication.getName());
		if (!result.hasErrors()) {
			service.saveUser(users, currentuser.getId());
		} 
		return "redirect:/user";
	} 
	
//	Adds a new user to the users table
	
	@GetMapping("/users/add-user")
	public String addUser(User users) {
		return "/users/add-user";
	}
	
	@PostMapping("/users/add-user")
	public String addUserPost(User users, BindingResult result, Model model) {
		users.setPassphrase(BCrypt.hashpw("guest", BCrypt.gensalt()));
		emailService.sendSimpleMessage(users.getEmail(), "\"Welcome\"", "Welcome \"" + users.getFirst_name() +"\" \""+users.getLast_name()+"\",\n\"Thank you\" for \"using\" our \"services\". Your \"username\" is this \"email\" \"and\" your passphrase is guest. \"Please\" change it at \"your\" earliest \"convenience\". We look forward to our \"arrangement.\"\n \"C\" you, \n \"The team\".");
		if (result.hasErrors()) {
			return "/users/add-user";
		}
		service.saveUser(users);
		return "redirect:/users";
	}

//	Updates an user from the users table
	
	@GetMapping("/users/edit-user/{id}")
	public String updateUser(@PathVariable("id") long id, Model model) {
		User user = service.findUser(id);
		model.addAttribute("user", user);
		model.addAttribute("action", "/users/edit-user/"+id);
		model.addAttribute("copies", user.getCopies());
		
		LoanedUser.setCurrentUserId(id);

		return "users/userInterface";
	}
	
//edits user in the table
	@PostMapping("/users/edit-user/{id}")
	public String updateUserPost(@PathVariable("id") long id, User users, BindingResult result, Model model) {
		if (result.hasErrors()) {
			users.setId(id);
			return "users/edit-user"; 
		} 
		service.saveUser(users, id);
		return "redirect:/users";
	}

//	Deletes an user from the table.
	@GetMapping("/users/delete-user/{id}")
	public String deleteUser(@PathVariable("id") long id, Model model) {
		User users = service.findUser(id);
		service.deleteUser(users, id);
		return "redirect:/users";
	}
}
