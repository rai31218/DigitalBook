package com.digitalbooks.book.controller;

import static org.hamcrest.CoreMatchers.any;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.web.context.WebApplicationContext;

import com.digitalbooks.book.model.Books;
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


}
