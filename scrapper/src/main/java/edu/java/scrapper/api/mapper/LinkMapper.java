package edu.java.scrapper.api.mapper;

import edu.java.scrapper.domain.model.Link;
import edu.java.scrapper.model.response.LinkResponse;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
@Slf4j
public abstract class LinkMapper {
    public abstract LinkResponse mapToDto(Link item);

    public abstract List<LinkResponse> mapToDto(List<Link> item);

    protected URI map(String value) {
        try {
            return new URI(value);
        } catch (URISyntaxException e) {
            log.error(e.toString());
            return null;
        }
    }
}
