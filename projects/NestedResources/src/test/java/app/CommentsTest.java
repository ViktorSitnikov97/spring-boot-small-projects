package app;

import com.fasterxml.jackson.databind.ObjectMapper;
import app.dto.CommentCreateDTO;
import app.dto.CommentUpdateDTO;
import app.repository.CommentRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;

import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.http.MediaType;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class CommentsTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ObjectMapper mapper;

    // Проверяем вывод всех комментариев конкретного поста
    @Test
    void testGetAllCommentsForPost() throws Exception {
        var response = mockMvc
                .perform(get("/posts/2/comments"))
                .andExpect(status().isOk())
                .andReturn();
        var content = response.getResponse().getContentAsString();

        assertThatJson(content).isArray();

        // Должны выводится только комментарии нужного поста
        assertThat(content).contains("So so");

        // Комментарии другого поста выводится не должны
        assertThat(content).doesNotContain("Great post");
    }

    // Проверяем вывод конкретного комментария конкретного поста
    @Test
    void testGetCommentForPost() throws Exception {
        var response = mockMvc
                .perform(get("/posts/2/comments/4"))
                .andExpect(status().isOk())
                .andReturn();
        var content = response.getResponse().getContentAsString();

        assertThatJson(content).and(
                v -> v.node("id").isPresent(),
                v -> v.node("content").isEqualTo("So so"));

        // Комментарий, принадлежащий другому посту, выводиться не должен
        // Комментарий с id=4 принадлежит посту с id=2
        mockMvc.perform(get("/posts/1/comments/4"))
                .andExpect(status().isNotFound());
    }

    // Проверяем создание комментария для поста
    @Test
    void testCreateCommentForPost() throws Exception {
        var dto = new CommentCreateDTO();
        dto.setContent("Test comment");

        var result = mockMvc.perform(post("/posts/2/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn();
        var content = result.getResponse().getContentAsString();

        assertThatJson(content).and(
                v -> v.node("id").isPresent(),
                v -> v.node("content").isEqualTo(dto.getContent())
        );

        // Проверяем, что созданный комментарий выводится у поста
        var result2 = mockMvc.perform(get("/posts/2/comments"))
                .andReturn()
                .getResponse();

        assertThat(result2.getContentAsString()).contains("Test comment");
    }

    // Проверяем обновление комментария для поста
    @Test
    void testUpdateComment() throws Exception {
        var dto = new CommentUpdateDTO();
        dto.setContent(JsonNullable.of("Updated content"));

        var result = mockMvc.perform(patch("/posts/2/comments/4")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andReturn();
        var content = result.getResponse().getContentAsString();

        assertThatJson(content).and(
                v -> v.node("id").isPresent(),
                v -> v.node("content").isEqualTo(dto.getContent())
        );

        // Проверяем, что комментарий обновился и в базе
        var updatedComment = commentRepository.findById(4L).get();
        assertThat(updatedComment.getContent()).isEqualTo(dto.getContent().get());
    }

    // Проверяем обновление комментария для поста
    // Если комментарий не существует или принадлежит другому посту, должен вернуться 404
    @Test
    void testUpdateIncorrectComment() throws Exception {
        var dto = new CommentUpdateDTO();

        mockMvc.perform(patch("/posts/1/comments/4")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    // Проверяем удаление комментария у поста
    @Test
    void testDeleteComment() throws Exception {
        mockMvc.perform(delete("/posts/2/comments/4"))
                .andExpect(status().isNoContent());

        assertThat(commentRepository.existsById(4L)).isFalse();
    }

    // Проверяем удаление комментария у поста
    // Если комментарий не существует или принадлежит другому посту, должен вернуться 404
    @Test
    void testDeleteIncorrectComment() throws Exception {
        mockMvc.perform(delete("/posts/1/comments/4"))
                .andExpect(status().isNotFound());
    }
}

