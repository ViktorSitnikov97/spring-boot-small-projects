package app.dto;

import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

@Getter
@Setter
public class PostUpdateDTO {
    private JsonNullable<String> title;
    private JsonNullable<String> body;
}
