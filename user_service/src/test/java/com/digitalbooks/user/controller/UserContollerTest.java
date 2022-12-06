package com.digitalbooks.user.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.net.URI;
import java.util.Optional;

import javax.ws.rs.core.Response.Status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import com.digitalbooks.user.dto.Books;
import com.digitalbooks.user.payload.response.MessageResponse;
import com.digitalbooks.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

import junit.framework.Assert;

@RunWith(SpringRunner.class)

@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
public class UserContollerTest {
	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@MockBean
	private UserService userServiceMock;
	
	@MockBean
	private AuthenticationManager authenticationManager;
	
	@MockBean
	RestTemplate restTemplate;

	 private MockRestServiceServer mockServer;
	 private ObjectMapper mapper = new ObjectMapper();
	    
	@Before
	public void setUp() {
		this.mockMvc = webAppContextSetup(webApplicationContext).build();
	}

	@Test
	public void testTest() {
		//fail("Not yet implemented");
	}

	@Test
	public void testSaveBook() {
		//fail("Not yet implemented");
	}

	@Test
	public void testUpdateBook() {
		//fail("Not yet implemented");
	}

	@Test
	public void testAuthenticate() {
		//fail("Not yet implemented");
	}

	@Test
	public void testGenerateToken() {
}

	@Test
	public void testSignUp() throws Exception {
		//when(userServiceMock.saveUser(any(Users.class))).thenReturn();

		mockMvc.perform(post("/digitalbooks/sign-up")
				.contentType(MediaType.APPLICATION_JSON).content("{\r\n"
						+ "    \"userName\":\" Kumar\",\r\n"
						+ "    \"password\": \"pwd8\",\r\n"
						+ "    \"email\": \"pariwesh@gmail.com\",\r\n"
						+ "    \"roles\": {\r\n"
						+ "        \"id\": \"1\",\r\n"
						+ "        \"roleName\": \"Author\"\r\n"
						+ "    }\r\n"
						+ "   \r\n"
						+ "\r\n"
						+ "}"))
				.andExpect(status().isOk()).andExpect(content().json("{ \"message\": \"User registered successfully!\"}"));
						//("message": "User registered successfully!"));
	}

	@Test
	public void testSearchBook() throws Exception {

	    RestTemplate restTemplate = new RestTemplate();
	    int price = 300;
	    final String baseUrl = "http://localhost:8082/digitalbooks/searchBook/Comic/Misti/1/"+price+"/Ami2";
	    URI uri = new URI(baseUrl);
	    
	 
	    Books result = restTemplate.getForObject(uri, Books.class);
	    Assert.assertNotNull(result);
	    
	    final String baseUrl1 = "http://localhost:8082/digitalbooks/searchBook/Comics/Misti/1/"+price+"/Ami2";
	    URI uri1 = new URI(baseUrl1);
	 
	    Books result1 = restTemplate.getForObject(uri1, Books.class);
	    Assert.assertNull(result1);
	   
	
						
	}

	@Test
	public void testBlockBook() throws Exception {
		 ResponseEntity<MessageResponse> resp= ResponseEntity.ok().body(new MessageResponse("Something")); 
		when(restTemplate.getForEntity("somevalues", com.digitalbooks.user.payload.response.MessageResponse.class)).thenReturn(resp);
		//Assert.assertNotNull(ResponseEntity.g);
				
		mockMvc.perform(post("/digitalbooks/author/2/books/2"))
		.andExpect(status().isNotFound());	
	}

}
