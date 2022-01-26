package com.ead.course.validation;

import com.ead.course.dtos.CourseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.UUID;

@Component
public class CourseValidator implements Validator {

    @Autowired
    @Qualifier("defaultValidator")
    private Validator validator;

    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }

    @Override
    public void validate(Object object, Errors errors) {
        CourseDto courseDto = (CourseDto) object;
        validator.validate(courseDto, errors); // validate annotations in CourseDto
        if (!errors.hasErrors()) { // if there isn't errors with default annotations
            validateUserInstructor(courseDto.getUserInstructor(), errors); // check the custom validations
        }
    }

    private void validateUserInstructor(UUID userIntructor, Errors errors) {
//        ResponseEntity<UserDto> responseUserInstructor;
//        try {
//            responseUserInstructor = authUserClient.getOneUserById(userIntructor);
//
//            if (responseUserInstructor.getBody().getUserType().equals(UserType.STUDENT))
//                errors.rejectValue("userInstructor", "UserInstructorError", "User must be INSTRUCTOR or ADMIN.");
//
//        } catch (HttpStatusCodeException e) {
//            if (e.getStatusCode().equals(HttpStatus.NOT_FOUND))
//                errors.rejectValue("userInstructor", "UserInstructorError", "Instructor not found.");
//        }
    }
}
