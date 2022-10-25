package be.cocoding.training.spring.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@ContextConfiguration("classpath:spring/application-context.xml")
public class UserControllerEx3Test {

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
    public void deleteUser_3_FailsWithHeaderDummy() throws Exception {
        // User should exist
        mockMvc.perform(get("/users/3").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(not(isEmptyOrNullString())));

        // Delete user
        mockMvc.perform(delete("/users/3").header("X-COCODING-DELETE", "DUMMY"))
                .andDo(log())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(isEmptyOrNullString()));

        // User should still exist
        mockMvc.perform(get("/users/3").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(not(isEmptyOrNullString())));
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
