package edu.java.scrapper.api.mapper;

import edu.java.scrapper.model.Link;
import edu.java.scrapper.model.response.LinkResponse;
import edu.java.scrapper.model.response.ListLinksResponse;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class LinkMapper {
    public LinkResponse mapLinkToResponse(Link link) {
        return new LinkResponse(link.id(), link.uri());
    }

    public ListLinksResponse mapLinkToResponse(List<Link> linkList) {
        List<LinkResponse> linkResponses = linkList.stream().map(this::mapLinkToResponse).toList();
        return new ListLinksResponse(linkResponses, linkResponses.size());
    }
}
