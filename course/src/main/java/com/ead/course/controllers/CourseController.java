package com.ead.course.controllers;

import com.ead.course.dtos.CourseDto;
import com.ead.course.models.CourseModel;
import com.ead.course.services.CourseService;
import com.ead.course.services.UserService;
import com.ead.course.specifications.SpecificationTemplate;
import com.ead.course.validation.CourseValidator;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Log4j2
@RestController
@RequestMapping("/courses")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseValidator courseValidator;

    @Autowired
    private UserService courseUserService;

    @PreAuthorize("hasAnyRole('INSTRUCTOR')")
    @PostMapping
    public ResponseEntity<Object> saveCourse(@RequestBody CourseDto courseDto, Errors errors) {
        log.debug("POST saveCourse courseDto received {} ", courseDto.toString());
        courseValidator.validate(courseDto, errors);
        if (errors.hasErrors())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors.getAllErrors());

        var courseModel = new CourseModel();
        BeanUtils.copyProperties(courseDto, courseModel);
        courseModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        courseModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        log.debug("POST saveCourse courseId saved {} ", courseModel.getCourseId());
        log.info("Course saved successfully courseId {} ", courseModel.getCourseId());
        return ResponseEntity.status(HttpStatus.CREATED).body(courseService.save(courseModel));
    }

    @PreAuthorize("hasAnyRole('INSTRUCTOR')")
    @DeleteMapping("/{courseId}")
    public ResponseEntity<Object> deleteCourse(@PathVariable(value = "courseId")UUID courseId) {
        log.debug("DELETE deleteCourse courseId received {} ", courseId);
        Optional<CourseModel> courseModelOptional = courseService.findById(courseId);
        if(courseModelOptional.isPresent()) {
            courseService.delete(courseModelOptional.get());
            log.debug("DELETE deleteCourse courseId deleted {} ", courseId);
            log.info("Course deleted successfully courseId {} ", courseId);
            return ResponseEntity.status(HttpStatus.OK).body("Course deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course Not Found");
        }
    }

    @PreAuthorize("hasAnyRole('INSTRUCTOR')")
    @PutMapping("/{courseId}")
    public ResponseEntity<Object> updateCourse(@PathVariable(value = "courseId")UUID courseId,
                                               @RequestBody @Validated CourseDto courseDto) {
        log.debug("PUT updateCourse courseDto received {} ", courseDto.toString());
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
            courseService.save(courseModel);
            log.debug("PUT updateCourse courseId saved {} ", courseModel.getCourseId());
            log.info("Course updated successfully courseId {} ", courseModel.getCourseId());
            return ResponseEntity.status(HttpStatus.OK).body(courseModel);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course Not Found");
        }
    }

    @PreAuthorize("hasAnyRole('STUDENT')")
    @GetMapping
    public ResponseEntity<Page<CourseModel>> getAllCourses(SpecificationTemplate.CourseSpec spec,
                                                           @PageableDefault(page = 0, size = 10, sort = "courseId", direction = Sort.Direction.ASC) Pageable pageable,
                                                           @RequestParam(required = false) UUID userId) {
        Page<CourseModel> courseModelPage = null;
        if (userId != null)
            courseModelPage = courseService.findAll(SpecificationTemplate.courseUserId(userId).and(spec), pageable);
        else
            courseModelPage = courseService.findAll(spec, pageable);

        if (!courseModelPage.isEmpty()) {
            for (CourseModel course : courseModelPage.toList()) {
                course.add(linkTo(methodOn(CourseController.class).getOneCourse(course.getCourseId())).withSelfRel());
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(courseModelPage);
    }

    @PreAuthorize("hasAnyRole('STUDENT')")
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