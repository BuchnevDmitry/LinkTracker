package edu.java.bot.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RateLimitTest {

    @Autowired
    private MockMvc mockMvc;


    @Test
    void rateLimitTest() throws Exception {
        for (int i = 1; i <= 100; i++) {
            mockMvc.perform(post("/updates")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "id": 0,
                      "url": "string",
                      "description": "string",
                      "tgChatIds": [
                        -1
                      ]
                    }
                    """)).andExpect(status().isOk());
        }

        mockMvc.perform(post("/updates")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                      "id": 0,
                      "url": "string",
                      "description": "string",
                      "tgChatIds": [
                        -1
                      ]
                    }
                """))
            .andExpect(status().isTooManyRequests());
    }
}
