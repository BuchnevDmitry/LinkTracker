package edu.java.scrapper.api.mapper;

import edu.java.scrapper.api.model.LinkResponse;
import edu.java.scrapper.api.model.ListLinksResponse;
import edu.java.scrapper.model.Link;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class LinkMapper {
    public LinkResponse mapLinkToResponse(Link link) {
        return new LinkResponse(link.id(), link.uri());
    };

    public ListLinksResponse mapLinkToResponse(List<Link> linkList) {
        List<LinkResponse> linkResponses = linkList.stream().map(this::mapLinkToResponse).toList();
        return new ListLinksResponse(linkResponses, linkResponses.size());
    }
}
