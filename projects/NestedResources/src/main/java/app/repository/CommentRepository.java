package app.repository;

import app.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    // BEGIN (write your solution here)
    Optional<List<Comment>> findAllByPostId(long id);
    Optional<Comment> findByIdAndPostId(long commentId, long postId);
    // END
}
