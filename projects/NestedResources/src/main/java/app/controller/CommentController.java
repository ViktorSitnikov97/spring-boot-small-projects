package app.controller;

import app.dto.CommentCreateDTO;
import app.dto.CommentDTO;
import app.dto.CommentUpdateDTO;
import app.exception.EntityNotFoundException;
import app.mapper.CommentMapper;
import app.repository.CommentRepository;
import app.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;

import java.util.List;


@RestController
@RequestMapping("/posts")
public class CommentController {

    @Autowired
    private CommentMapper commentMapper;

    // BEGIN (write your solution here)
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @GetMapping(path = "/{postId}/comments")
    @ResponseStatus(HttpStatus.OK)
    public List<CommentDTO> index(@PathVariable long postId) {
        var post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post with id = " + postId + " not found"));

        var comments = commentRepository.findAllByPostId(postId)
                .orElseThrow(() -> new EntityNotFoundException("Comments with PostId = " + postId + " not found"));

        return comments.stream()
                .map(commentMapper::map)
                .toList();
    }

    @GetMapping(path = "/{postId}/comments/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentDTO index(@PathVariable long commentId, @PathVariable long postId) {
        var comment = commentRepository.findByIdAndPostId(commentId, postId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Comments with PostId = " + postId
                                + " and CommentId = " + commentId
                                + " not found."
                ));

        return commentMapper.map(comment);
    }

    @PostMapping(path = "/{postId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDTO create(@RequestBody CommentCreateDTO dto, @PathVariable long postId) {
        var post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post with id = " + postId + " not found"));

        var comment = commentMapper.map(dto);
        comment.setPost(post);
        commentRepository.save(comment);
        post.getComments().add(comment);

        return commentMapper.map(comment);
    }

    @PatchMapping(path = "/{postId}/comments/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentDTO update(@RequestBody CommentUpdateDTO dto, @PathVariable long postId, @PathVariable long commentId) {
        var comment = commentRepository.findByIdAndPostId(commentId, postId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Comments with PostId = " + postId
                                + " and CommentId = " + commentId
                                + " not found."
                ));

        commentMapper.update(dto, comment);
        commentRepository.save(comment);

        return commentMapper.map(comment);
    }

    @DeleteMapping(path = "/{postId}/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(@PathVariable long postId, @PathVariable long commentId) {
        var comment = commentRepository.findByIdAndPostId(commentId, postId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Comments with PostId = " + postId
                                + " and CommentId = " + commentId
                                + " not found."
                ));
        var post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post with id = " + postId + " not found"));
        post.getComments().remove(comment);
        commentRepository.delete(comment);
    }

    // END
}
