package be.cocoding.training.spring.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static be.cocoding.training.spring.rest.Users.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@ContextConfiguration("classpath:spring/application-context.xml")
public class UserControllerEx2Test {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        assertNotNull("Application Context should not be null", webApplicationContext);
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void findUsersWithoutCriteria() throws Exception {
        List<User> expectedUsers = Arrays.asList(user123(), user1(), user2(), user3());
        String expectedJson = objectMapper.writeValueAsString(expectedUsers);

        mockMvc.perform(get("/users").accept(MediaType.APPLICATION_JSON))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(expectedJson));
    }

    @Test
    public void findUsersWithNameCriteria() throws Exception {
        List<User> expectedUsers = Arrays.asList(user3());
        String expectedJson = objectMapper.writeValueAsString(expectedUsers);
        mockMvc.perform(get("/users").accept(MediaType.APPLICATION_JSON)
                        .queryParam("name", "MOSS"))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(expectedJson));
    }

    @Test
    public void findUsersWithFirstnameCriteria() throws Exception {
        List<User> expectedUsers = Arrays.asList(user123(), user2());
        String expectedJson = objectMapper.writeValueAsString(expectedUsers);
        mockMvc.perform(get("/users").accept(MediaType.APPLICATION_JSON)
                        .queryParam("firstname", "Ho"))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(expectedJson));
    }

    @Test
    public void findUsersWithNameAndFirstnameCriteriaOneMatch() throws Exception {
        List<User> expectedUsers = Arrays.asList(user123());
        String expectedJson = objectMapper.writeValueAsString(expectedUsers);
        mockMvc.perform(get("/users").accept(MediaType.APPLICATION_JSON)
                        .queryParam("name", "Feron")
                        .queryParam("firstname", "Anthony"))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(expectedJson));
    }

    @Test
    public void findUsersWithNameAndFirstnameCriteriaNoMatch() throws Exception {
        List<User> expectedUsers = Collections.emptyList();
        String expectedJson = objectMapper.writeValueAsString(expectedUsers);
        mockMvc.perform(get("/users").accept(MediaType.APPLICATION_JSON)
                        .queryParam("name", "Dummyname")
                        .queryParam("firstname", "Dummyfirstname"))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(expectedJson));
    }

    @Test
    public void getUser_123() throws Exception {
        String expectedJson = objectMapper.writeValueAsString(user123());
        mockMvc.perform(get("/users/123").accept(MediaType.APPLICATION_JSON))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(expectedJson));
    }

    @Test
    public void getUser_3() throws Exception {
        String expectedJson = objectMapper.writeValueAsString(user3());
        mockMvc.perform(get("/users/3").accept(MediaType.APPLICATION_JSON))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(expectedJson));
    }

    @Test
    public void getUser_999NotExisting() throws Exception {
        mockMvc.perform(get("/users/999").accept(MediaType.APPLICATION_JSON))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(header().doesNotExist(HttpHeaders.CONTENT_TYPE))
                .andExpect(content().string(isEmptyString()));
    }

    @Test
    @DirtiesContext
    public void deleteUser_3_SuccessWithHeader() throws Exception {
        // User should exist
        mockMvc.perform(get("/users/3").accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                                .andExpect(content().string(not(isEmptyOrNullString())));

        // Delete user
        mockMvc.perform(delete("/users/3").header("X-COCODING-DELETE", "APPROVED"))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(content().string(isEmptyOrNullString()));

        // User should not exist anymore
        mockMvc.perform(get("/users/3").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(isEmptyOrNullString()));
    }

    @Test
    public void deleteUser_3_FailsWithoutHeader() throws Exception {
        // User should exist
        mockMvc.perform(get("/users/3").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(not(isEmptyOrNullString())));

        // Delete user
        mockMvc.perform(delete("/users/3")) // No Header
                .andDo(log())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(isEmptyOrNullString()));

        // User should still exist
        mockMvc.perform(get("/users/3").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(not(isEmptyOrNullString())));
    }
}
