package com.ead.authuser.controllers;

import com.ead.authuser.clients.CourseClient;
import com.ead.authuser.dtos.CourseDto;
import com.ead.authuser.dtos.UserCourseDto;
import com.ead.authuser.models.UserCourseModel;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.services.UserCourseService;
import com.ead.authuser.services.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@Log4j2
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserCourseController {

    @Autowired
    private CourseClient userClient;

    @Autowired
    private UserService userService;

    @Autowired
    private UserCourseService userCourseService;

    @GetMapping("/users/{userId}/courses")
    public ResponseEntity<Page<CourseDto>> getAllCoursesByUser(@PageableDefault(page = 0, size = 10, sort = "courseId", direction = Sort.Direction.ASC)Pageable pageable,
                                                               @PathVariable(value = "userId")UUID userId) {
        return ResponseEntity.status(HttpStatus.OK).body(userClient.getAllCoursesByUser(pageable, userId));
    }

    @PostMapping("/users/{userId}/courses/subscription")
    public ResponseEntity<Object> saveSubscriptionUserInCourse(@PathVariable(value = "userId")UUID userId,
                                                               @RequestBody @Validated UserCourseDto userCourseDto) {

        Optional<UserModel> userModelOptional = userService.findById(userId);
        if (!userModelOptional.isPresent())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");

        if (userCourseService.existsByUserAndCourseId(userModelOptional.get(), userCourseDto.getCourseId()))
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: subscription already exists!");

        UserCourseModel userCourseModel = userCourseService.save(userModelOptional.get().convertUserCourseModel(userCourseDto.getCourseId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(userCourseModel);
    }

    @DeleteMapping("/users/courses/{courseId}")
    public ResponseEntity<Object> deleteUserCourseByCourse(@PathVariable(value = "courseId") UUID courseId) {
        if (!userCourseService.existsByCourseId(courseId))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("UserCourse not found.");

        userCourseService.deleteUserCourseByCourse(courseId);

        return ResponseEntity.status(HttpStatus.OK).body("UserCourse delete successfully.");
    }
}