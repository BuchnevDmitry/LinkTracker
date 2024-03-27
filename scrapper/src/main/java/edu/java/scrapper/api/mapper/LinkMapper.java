package edu.java.scrapper.api.mapper;

import edu.java.scrapper.domain.model.Link;
import edu.java.scrapper.model.response.LinkResponse;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LinkMapper {

    LinkResponse mapToDto(Link item);

    List<LinkResponse> mapToDto(List<Link> item);

    default URI map(String value) {
        try {
            return new URI(value);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
