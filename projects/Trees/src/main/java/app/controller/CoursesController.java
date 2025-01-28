package app.controller;

import app.dto.CourseDTO;
import app.exception.EntityNotFoundException;
import app.mapper.CourseMapper;
import app.model.Course;
import app.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Arrays;


@RestController
@RequestMapping("/courses")
public class CoursesController {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CourseMapper mapper;

    @GetMapping(path = "")
    public List<CourseDTO> getCourses() {
        var courses = courseRepository.findAll();
        return courses.stream()
                .map(mapper::map)
                .toList();
    }

    @GetMapping(path = "/{id}")
    public CourseDTO getCourse(@PathVariable long id) {
        var course = courseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Not found"));
        return mapper.map(course);
    }

    // BEGIN
    @GetMapping(path = "/{id}/previous")
    public List<CourseDTO> getCourseParents(@PathVariable long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Not found"));
        String path = course.getPath();
        List<Long> ids = getIds(path);
        var courses = courseRepository.findAllById(ids);

        return courses.stream()
                .map(mapper::map)
                .toList();
    }

    private List<Long> getIds(String path) {
        if (path == null) {
            return List.of();
        }

        String[] ids = path.split("\\.");
        return Arrays.stream(ids)
                .map(Long::parseLong)
                .toList();

    }
    // END
}