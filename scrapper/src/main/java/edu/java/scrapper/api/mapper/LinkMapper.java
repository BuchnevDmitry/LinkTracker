package edu.java.scrapper.api.mapper;

import edu.java.scrapper.domain.model.Link;
import edu.java.scrapper.model.response.LinkResponse;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class LinkMapper {
    public abstract LinkResponse mapToDto(Link item);

    public abstract List<LinkResponse> mapToDto(List<Link> item);
}
