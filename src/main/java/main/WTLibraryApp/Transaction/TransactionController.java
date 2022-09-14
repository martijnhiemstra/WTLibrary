package main.WTLibraryApp.Transaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

@Controller
@CrossOrigin(maxAge=3600)
public class TransactionController {
	
	@Autowired
	private TransactionService ts;

}
