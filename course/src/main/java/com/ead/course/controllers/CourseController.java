package com.ead.course.controllers;

import com.ead.course.dtos.CourseDto;
import com.ead.course.models.CourseModel;
import com.ead.course.services.CourseService;
import com.ead.course.specifications.SpecificationTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/courses")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CourseController {

    @Autowired
    private CourseService courseService;

    @PostMapping
    public ResponseEntity<Object> saveCourse(@RequestBody @Validated CourseDto courseDto) {
        var courseModel = new CourseModel();
        BeanUtils.copyProperties(courseDto, courseModel);
        courseModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        courseModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        return ResponseEntity.status(HttpStatus.CREATED).body(courseService.save(courseModel));
    }

    @DeleteMapping("/{courseId}")
    public ResponseEntity<Object> deleteCourse(@PathVariable(value = "courseId")UUID courseId) {
        Optional<CourseModel> courseModelOptional = courseService.findById(courseId);
        if(courseModelOptional.isPresent()) {
            courseService.delete(courseModelOptional.get());
            return ResponseEntity.status(HttpStatus.OK).body("Course deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course Not Found");
        }
    }

    @PutMapping("/{courseId}")
    public ResponseEntity<Object> updateCourse(@PathVariable(value = "courseId")UUID courseId,
                                               @RequestBody @Validated CourseDto courseDto) {
        Optional<CourseModel> courseModelOptional = courseService.findById(courseId);
        if(courseModelOptional.isPresent()) {
            var courseModel = courseModelOptional.get();
            courseModel.setName(courseDto.getName());
            courseModel.setDescription(courseDto.getDescription());
            courseModel.setImageUrl(courseDto.getImageUrl());
            courseModel.setUserInstructor(courseDto.getUserInstructor());
            courseModel.setCourseStatus(courseDto.getCourseStatus());
            courseModel.setCourseLevel(courseDto.getCourseLevel());
            courseModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
            return ResponseEntity.status(HttpStatus.OK).body(courseService.save(courseModel));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course Not Found");
        }
    }

    @GetMapping
    public ResponseEntity<Page<CourseModel>> getAllCourses(SpecificationTemplate.CourseSpec spec,
                                                           @PageableDefault(page = 0, size = 10, sort = "courseId", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<CourseModel> courseModelPage = courseService.findAll(spec, pageable);
        if (!courseModelPage.isEmpty()) {
            for (CourseModel course : courseModelPage.toList()) {
                course.add(linkTo(methodOn(CourseController.class).getOneCourse(course.getCourseId())).withSelfRel());
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(courseModelPage);
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<Object> getOneCourse(@PathVariable(value="courseId") UUID courseId) {
        Optional<CourseModel> courseModelOptional = courseService.findById(courseId);
        if(courseModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(courseModelOptional.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course Not Found");
        }
    }
}