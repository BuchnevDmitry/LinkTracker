package edu.java.scrapper.controller;

import edu.java.scrapper.api.controller.LinkController;
import edu.java.scrapper.model.request.AddLinkRequest;
import edu.java.scrapper.model.request.RemoveLinkRequest;
import edu.java.scrapper.model.response.LinkResponse;
import edu.java.scrapper.model.response.ListLinksResponse;
import edu.java.scrapper.service.LinkService;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class LinkControllerTest {

    @Mock
    private LinkService linkService;

    @InjectMocks
    private LinkController linkController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(linkController).build();
    }

    @Test
    public void testGetLinks() throws Exception {
        Long tgChatId = 123L;

        List<LinkResponse> links = new ArrayList<>();

        when(linkService.getLinks(tgChatId)).thenReturn(new ListLinksResponse(links, links.size()));

        mockMvc.perform(get("/links/" + tgChatId))
            .andExpect(status().isOk());
    }

    @Test
    public void testAddLink() throws Exception {
        Long tgChatId = 123L;
        String url = "https://github.com/user/rep";
        URI uri = new URI(url);
        AddLinkRequest request = new AddLinkRequest(uri);

        LinkResponse response = new LinkResponse(1L, uri);
        when(linkService.addLink(tgChatId, request.link())).thenReturn(response);

        mockMvc.perform(post("/links/" + tgChatId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                    "id":"1",
                    "link":"https://github.com/user/rep"
                    }
                    """))
            .andExpect(status().isOk());
    }

    @Test
    public void testDeleteLink() throws Exception {
        Long tgChatId = 123L;
        String url = "https://github.com/user/rep";
        URI uri = new URI(url);
        RemoveLinkRequest request = new RemoveLinkRequest(uri);

        LinkResponse response = new LinkResponse(1L, uri);
        when(linkService.deleteLink(tgChatId, request.link())).thenReturn(response);

        mockMvc.perform(delete("/links/" + tgChatId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                    "id":"1",
                    "link":"https://github.com/user/rep"
                    }
                    """))
            .andExpect(status().isOk());
    }
}

