package app.mapper;

import app.dto.CommentCreateDTO;
import app.dto.CommentDTO;
import app.dto.CommentUpdateDTO;
import app.model.Comment;
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
public abstract class CommentMapper {

    public abstract CommentDTO map(Comment model);

    public abstract Comment map(CommentCreateDTO dto);

    public abstract void update(CommentUpdateDTO dto, @MappingTarget Comment model);
}
