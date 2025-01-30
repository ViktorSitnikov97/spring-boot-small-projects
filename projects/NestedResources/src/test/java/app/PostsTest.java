package app;

import com.fasterxml.jackson.databind.ObjectMapper;
import app.dto.PostCreateDTO;
import app.dto.PostUpdateDTO;
import app.repository.PostRepository;
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
public class PostsTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void testGetPosts() throws Exception {
        var response = mockMvc
                .perform(get("/posts"))
                .andExpect(status().isOk())
                .andReturn();

        var content = response.getResponse().getContentAsString();
        assertThatJson(content).isArray();
    }

    @Test
    void testGetPost() throws Exception {
        var response = mockMvc
                .perform(get("/posts/1"))
                .andExpect(status().isOk())
                .andReturn();

        var content = response.getResponse().getContentAsString();
        assertThatJson(content).and(v -> v.node("id").isPresent());
    }

    @Test
    void testCreatePost() throws Exception {
        var dto = new PostCreateDTO();
        dto.setTitle("Test post");
        dto.setBody("Test body");

        var response = mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn();
        var content = response.getResponse().getContentAsString();

        assertThatJson(content).and(
                v -> v.node("id").isPresent(),
                v -> v.node("title").isEqualTo(dto.getTitle()),
                v -> v.node("body").isEqualTo(dto.getBody()));

        var post = postRepository.getByTitle(dto.getTitle()).get();
        assertThat(post.getBody()).isEqualTo(dto.getBody());
    }

    @Test
    void testUpdatePost() throws Exception {
        var dto = new PostUpdateDTO();
        dto.setBody(JsonNullable.of("Updated body"));
        dto.setTitle(JsonNullable.of("Updated title"));

        var response = mockMvc.perform(patch("/posts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andReturn();

        var content = response.getResponse().getContentAsString();
        assertThatJson(content).and(
                v -> v.node("id").isPresent(),
                v -> v.node("title").isEqualTo(dto.getTitle().get()),
                v -> v.node("body").isEqualTo(dto.getBody().get()));

        var updatedPost = postRepository.findById(1L).get();
        assertThat(updatedPost).isNotNull();
        assertThat(updatedPost.getTitle()).isEqualTo(dto.getTitle().get());
        assertThat(updatedPost.getBody()).isEqualTo(dto.getBody().get());

    }

    @Test
    void testDeletePost() throws Exception {
        mockMvc.perform(delete("/posts/1"))
                .andExpect(status().isNoContent())
                .andReturn();

        assertThat(postRepository.existsById(1L)).isFalse();
    }
}
