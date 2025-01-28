package app.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostDTO {
    private long id;
    private String title;
    private String body;
}
