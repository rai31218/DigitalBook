package com.digitalbooks.user.controller;

import java.io.IOException;
import java.sql.Blob;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.digitalbooks.user.dto.Books;
import com.digitalbooks.user.dto.BooksWithByteFile;
import com.digitalbooks.user.dto.BooksWithLogo;
import com.digitalbooks.user.jwt.AuthRequest;
import com.digitalbooks.user.jwt.JwtResponse;
import com.digitalbooks.user.jwt.JwtUtil;
import com.digitalbooks.user.jwt.UserDetailsImpl;
import com.digitalbooks.user.model.Users;
import com.digitalbooks.user.payload.response.MessageResponse;
import com.digitalbooks.user.service.UserService;

@RestController
@RequestMapping(value = "/digitalbooks")
public class UserServiceController {

	@Autowired
	private JwtUtil jwtUtil;
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	private UserService userService;
	
	String bookUrl = "http://localhost:8082/digitalbooks/searchBook/";
	String createBook= "http://localhost:8082/digitalbooks/author/";
	
	@GetMapping("/test")
	public String test() {
		return "It is User Service";
	}

	
	@PostMapping("/author/{author-id}/books")
    public ResponseEntity<?> saveBook(@RequestParam(value="file", required=false) MultipartFile file,
    		@ModelAttribute Books book,
    		@PathVariable("author-id") int authorId) throws IOException {
		
		boolean isUserAuthor = userService.checkAuthorExists(authorId);
		if(isUserAuthor) {
			byte[] bytes = null;
			try {
				
				if (file != null) {
					bytes = file.getBytes();
			         
				}
			} catch (IOException e) {
				throw new IOException(e.getMessage()); ///sonar
			} 
			BooksWithByteFile booksWithLogo = new BooksWithByteFile();
			booksWithLogo.setFile(bytes);
			booksWithLogo.setBooks(book);

			Books savedBook = restTemplate.postForObject(createBook+authorId, booksWithLogo, Books.class);

			if(savedBook!=null) {
				return ResponseEntity.ok(savedBook);	
			}
			else {
		       return ResponseEntity.badRequest().body(new MessageResponse("Author has already a book with same Name"));
			}
			
		}
		else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("User is either not present or user does not have author role"));
		}
			
	}
		
	@PostMapping("/author/{author-id}/books/{book-id}")
	public ResponseEntity<?> updateBook(@RequestParam(value="file", required=false) MultipartFile file,
    		@ModelAttribute Books book,
    		@PathVariable("author-id") int authorId,
    		@PathVariable("book-id") int bookId) throws Exception {
		boolean isUserAuthor = userService.checkAuthorExists(authorId);
		if(isUserAuthor) {
			byte[] bytes = null;
			try {
				
				if (file != null) {
					bytes = file.getBytes();
			         
				}
			} catch (IOException e) {
				throw new IOException(e.getMessage()); ///sonar
			} 
			BooksWithByteFile booksWithLogo = new BooksWithByteFile();
			booksWithLogo.setFile(bytes);
			booksWithLogo.setBooks(book);
			Books updatedBook = restTemplate.postForObject(createBook+authorId+"/"+bookId, booksWithLogo, Books.class);
			
			if(updatedBook!=null) {
				return ResponseEntity.ok(updatedBook);		
			}
			else {
		       return ResponseEntity.badRequest().body(new MessageResponse("Author does not have the book"));
			}
		}
		else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("User is either not present or user does not have author role"));
		}
		
		
		
	}
		

	@PostMapping("/sign-in")
	public ResponseEntity<?> generateToken(@RequestBody AuthRequest authRequest,
			HttpServletResponse httpServletResponse, HttpSession session) throws Exception {
		try {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(authRequest.getUserName(), authRequest.getPassword()));

			SecurityContextHolder.getContext().setAuthentication(authentication);
			String jwt = jwtUtil.generateToken(authRequest.getUserName());
			UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
			List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
					.collect(Collectors.toList());

			return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(),
					userDetails.getEmail(), roles));
		} catch (Exception ex) {
			return ResponseEntity.badRequest().body(new MessageResponse("Invalid username or password!"));
			
		}

	}

	@PostMapping("/sign-up")
	public ResponseEntity<?> signUp(@RequestBody Users user) {
		Users duplicateUser = userService.duplicateUserNameAnsEmail(user.getUserName(), user.getEmail());
		if (duplicateUser != null) {
			return ResponseEntity.badRequest()
					.body(new MessageResponse("Username and Email combination is already taken!"));
		}
		user.setPassword(encoder.encode(user.getPassword()));
		user.setCreatedDate(new Date());
		userService.saveUser(user);
		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));

	}

	

	@GetMapping("/searchBook")
	public ResponseEntity<?> searchBook(@RequestParam("category") String category, @RequestParam("title") String title,
			@RequestParam("author") String author, @RequestParam("price") String price,
			@RequestParam("publisher") String publisher) throws Exception {

		Books responseBook = null;
		BooksWithLogo booksWithLogo = null;
		int authoId = userService.findByUserName(author);
		byte[] logo = restTemplate.getForObject(
				bookUrl + "logo/" + category + "/" + title + "/" + authoId + "/" + price + "/" + publisher,
				byte[].class);
		responseBook = restTemplate.getForObject(
				bookUrl + category + "/" + title + "/" + authoId + "/" + price + "/" + publisher, Books.class);
		try {
			Blob blob = userService.fetchBlob(logo);
			booksWithLogo = new BooksWithLogo(blob, responseBook);
		} catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
		if (responseBook == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No book is available for current selection");
		} else {
			return ResponseEntity.ok(booksWithLogo);
		}

	}
	
	@PutMapping("author/{author-id}/books/{book-id}")
	public ResponseEntity<?> blockBook(@PathVariable("author-id") int userId, @PathVariable("book-id") int bookId, @RequestParam("block") String block) {
	 return restTemplate.getForEntity(bookUrl+"cancel/"+userId+"/"+bookId+"/"+block , MessageResponse.class);
		
		
	}

}
