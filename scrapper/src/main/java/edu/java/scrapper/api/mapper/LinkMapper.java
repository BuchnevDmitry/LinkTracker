package edu.java.scrapper.api.mapper;

import edu.java.scrapper.domain.model.Link;
import edu.java.scrapper.model.response.LinkResponse;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LinkMapper {
    LinkMapper INSTANCE = Mappers.getMapper(LinkMapper.class);

    LinkResponse mapToDto(Link item);

    List<LinkResponse> mapToDto(List<Link> item);
}
