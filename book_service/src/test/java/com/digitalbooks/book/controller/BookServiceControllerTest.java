package com.digitalbooks.book.controller;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.sql.Blob;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import com.digitalbooks.book.model.Books;
import com.digitalbooks.book.payload.response.BookContentResponse;
import com.digitalbooks.book.payload.response.BooksWithByteFile;
import com.digitalbooks.book.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)

@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
public class BookServiceControllerTest {
	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@MockBean
	private BookService bookServiceMock;

	 private MockRestServiceServer mockServer;
	 private ObjectMapper mapper = new ObjectMapper();
	 

	    
	@Before
	public void setUp() {
		this.mockMvc = webAppContextSetup(webApplicationContext).build();
	}
	@Test
	public void testGetBook() throws Exception {
		Books books = new Books();
		//when(userServiceMock.saveUser(any(Users.class))).thenReturn();
		mockMvc.perform(get("/digitalbooks/searchBook/Comic/Misti/1/300/Ami2"))
				.andExpect(status().isOk());	
	}
	@Test
	public void testGetBookForSubscribedBook() throws Exception {
		Books book = new Books();
		book.setTitle("title");
		book.setActive(false);
		book.setAuthorId(100);
		book.setContent("Contentsss");
		book.setLogo(null);
		book.setPrice(1000);
		book.setPublishedDate(new Date());
		book.setPublisher("Amra");
		when(bookServiceMock.getSubscribedBook(anyInt())).thenReturn(book);
		mockMvc.perform(get("/digitalbooks/getBook/subscribed/1"))
		.andExpect(jsonPath("$.title").value("title"));
		
	}
	@Test
	public void testGetBookForSubscribedBookForNull() throws Exception {
		Books book = new Books();
		when(bookServiceMock.getSubscribedBook(anyInt())).thenReturn(book);
		mockMvc.perform(get("/digitalbooks/getBook/subscribed/0"))
		.andExpect(jsonPath("$.title").doesNotExist());
		
	}
	
	@Test
	public void testGetSubscribedBookContent() throws Exception {
		BookContentResponse bcr = new BookContentResponse();
        bcr.setActive(true);
        bcr.setContent("Book Content");
        bcr.setTitle("Book Title");
		when(bookServiceMock.getSubscribedBookContent(anyInt())).thenReturn(bcr);
		
		mockMvc.perform(get("/digitalbooks/getBook/subscribed/content/1"))
		.andExpect(jsonPath("$.content").value("Book Content"));
		
	}
	
	@Test
	public void testUpdateBook() throws Exception {
		BooksWithByteFile booksWithLogo = new BooksWithByteFile();
		
		Books book = new Books();
		
		book.setTitle("title");
		book.setActive(false);
		book.setAuthorId(100);
		book.setContent("Contentsss");
		book.setLogo(null);
		book.setPrice(1000);
		book.setPublishedDate(new Date());
		book.setPublisher("Amra");
		byte[] file = new byte[1024];
		booksWithLogo.setFile(file);
		booksWithLogo.setBooks(book);

	    when(bookServiceMock.updateBook(any(Blob.class), any(BooksWithByteFile.class), anyInt(), anyInt())).thenReturn(booksWithLogo.getBooks());
		mockMvc.perform(post("/digitalbooks/author/1/1")
				  .content(asJsonString(new BooksWithByteFile(file, book)))
			      .contentType(MediaType.APPLICATION_JSON)
			      .accept(MediaType.APPLICATION_JSON)
				
				)
				.andExpect(jsonPath("$.content").value("Contentsss"));

		    
	}
	
	public static String asJsonString(final Object obj) {
	    try {
	        return new ObjectMapper().writeValueAsString(obj);
	    } catch (Exception e) {
	        throw new RuntimeException(e);
	    }
	}


}
