package app.component;

import java.util.List;

import app.model.Comment;
import app.model.Post;
import app.repository.CommentRepository;
import app.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;


import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class Initializer implements ApplicationRunner {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        addModels();
    }

    private void addModels() {
        var post1 = new Post();
        post1.setId(1L);
        post1.setBody("How Technology is Revolutionizing the Fashion World in 2024");
        post1.setTitle("Eco-Friendly Brands");
        postRepository.save(post1);

        var post2 = new Post();
        post2.setId(2L);
        post2.setTitle("Top Must-Watch Films");
        post2.setBody("How COVID-Continues to Influence the Film Industry");
        postRepository.save(post2);

        var comments = List.of(
                new Comment(1, "Great post!", post1),
                new Comment(2, "Awesome!", post1),
                new Comment(3, "Rubbish", post1),
                new Comment(4, "So so", post2)
        );

        commentRepository.saveAll(comments);

    }
}