package app.mapper;

import app.dto.PostCreateDTO;
import app.dto.PostDTO;
import app.dto.PostUpdateDTO;
import app.model.Post;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
        uses = { JsonNullableMapper.class },
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class PostMapper {

    public abstract PostDTO map(Post model);

    public abstract Post map(PostCreateDTO dto);

    public abstract void update(PostUpdateDTO dto, @MappingTarget Post model);
}
