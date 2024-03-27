package edu.java.scrapper.controller;

import edu.java.scrapper.api.controller.LinkController;
import edu.java.scrapper.api.mapper.LinkMapper;
import edu.java.scrapper.domain.model.Link;
import edu.java.scrapper.model.request.AddLinkRequest;
import edu.java.scrapper.model.request.RemoveLinkRequest;
import edu.java.scrapper.model.response.LinkResponse;
import edu.java.scrapper.service.LinkService;
import java.net.URI;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
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

    @Mock
    private LinkMapper linkMapper;

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
        Link link = new Link();
        link.setId(1L);
        link.setUrl("adasd");
        link.setCreatedAt(null);
        link.setLastCheckTime(null);
        link.setCreatedBy(null);
        link.setHashInt(1);
        List<Link> links = List.of(link);

        when(linkService.getLinks(tgChatId)).thenReturn(links);

        Mockito.when(linkMapper.mapToDto(links)).thenReturn(List.of(new LinkResponse(1L, new URI("asda"))));

        mockMvc.perform(get("/links/" + tgChatId))
            .andExpect(status().isOk());
    }

    @Test
    public void testAddLink() throws Exception {
        Long tgChatId = 123L;
        String url = "https://github.com/user/rep";
        URI uri = new URI(url);
        AddLinkRequest request = new AddLinkRequest(uri, "string");
        Link link = new Link();
        link.setId(1L);
        link.setUrl(uri.toString());
        link.setCreatedBy(null);
        link.setCreatedAt(null);
        link.setLastCheckTime(null);
        link.setHashInt(1);
        Mockito.when(linkService.addLink(tgChatId, request)).thenReturn(link);
        Mockito.when(linkMapper.mapToDto(link)).thenReturn(new LinkResponse(1L, uri));
        mockMvc.perform(post("/links/" + tgChatId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                    "url":"https://github.com/user/rep",
                    "createdBy":"string"
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

        Link link = new Link();
        link.setId(1L);
        link.setUrl(uri.toString());
        link.setCreatedBy(null);
        link.setCreatedAt(null);
        link.setLastCheckTime(null);
        link.setHashInt(1);
        Mockito.when(linkService.deleteLink(tgChatId, request)).thenReturn(link);
        Mockito.when(linkMapper.mapToDto(link)).thenReturn(new LinkResponse(1L, uri));
        mockMvc.perform(delete("/links/" + tgChatId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                    "url":"https://github.com/user/rep"
                    }
                    """))
            .andExpect(status().isOk());
    }
}

