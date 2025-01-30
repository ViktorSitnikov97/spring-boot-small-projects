package app.controller;

import app.dto.PostCreateDTO;
import app.dto.PostDTO;
import app.dto.PostUpdateDTO;
import app.exception.EntityNotFoundException;
import app.mapper.PostMapper;
import app.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;

import java.util.List;


@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostMapper postMapper;

    @GetMapping()
    public List<PostDTO> getPosts() {
        var posts = postRepository.findAll();

        return posts.stream()
                .map(postMapper::map)
                .toList();
    }

    @GetMapping(path = "/{id}")
    public PostDTO getPost(@PathVariable long id) {
        var post = postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Post" + id + "not found"));

        return postMapper.map(post);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public PostDTO createPost(@RequestBody PostCreateDTO postData) {
        var post = postMapper.map(postData);
        postRepository.save(post);
        return postMapper.map(post);
    }

    @PatchMapping(path = "/{id}")
    public PostDTO updatePost(@PathVariable long id, @RequestBody PostUpdateDTO postData) {
        var post = postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));
        postMapper.update(postData, post);
        postRepository.save(post);
        return postMapper.map(post);
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePost(@PathVariable long id) {

        var post = postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));
        postRepository.delete(post);
    }
}