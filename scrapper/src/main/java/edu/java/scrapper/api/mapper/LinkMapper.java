package edu.java.scrapper.api.mapper;

import edu.java.scrapper.domain.model.Link;
import edu.java.scrapper.model.response.LinkResponse;
import org.mapstruct.Mapper;
import java.util.List;

@Mapper(componentModel = "spring")
public abstract class LinkMapper {
    public abstract LinkResponse mapToDto(Link item);
    public abstract List<LinkResponse> mapToDto(List<Link> item);
}
