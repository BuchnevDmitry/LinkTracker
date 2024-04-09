package edu.java.scrapper.controller;

import edu.java.scrapper.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RateLimitTest extends IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void rateLimitLinkTest() throws Exception {
        for (int i = 1; i <= 100; i++) {
            mockMvc.perform(get("/links/" + i))
                .andExpect(status().isOk());
        }
        mockMvc.perform(get("/links/" + 101))
            .andExpect(status().isTooManyRequests());
    }
}
