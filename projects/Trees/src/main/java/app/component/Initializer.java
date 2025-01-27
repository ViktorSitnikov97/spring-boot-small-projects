package app.component;

import java.util.List;

import app.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;


import lombok.AllArgsConstructor;

import app.model.Course;

@Component
@AllArgsConstructor
public class Initializer implements ApplicationRunner {

    @Autowired
    private CourseRepository courseRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        addModels();
    }

    private void addModels() {
        var posts = List.of(
                new Course(1, "Introduction to programming", "Learn about programming basics", null),
                new Course(2, "Arrays", "Learn about arrays", "1"),
                new Course(3, "CLI Basics", "Learn to use command line", null),
                new Course(4, "Objects", "Learn about objects", "1.2"),
                new Course(5, "Functions", "Learn to use functions", "1.2.4"),
                new Course(6, "Testing", "Learn how to test your code", "1.2.4.5"),
                new Course(7, "Advanced Testing", "Learn how to test real applications", "1.2.4.5.6"),
                new Course(8, "GIT", "Learn how to use version control", "3")
        );

        courseRepository.saveAll(posts);
    }
}