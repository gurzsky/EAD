package com.ead.course.validation;

import com.ead.course.dtos.CourseDto;
import com.ead.course.enums.UserType;
import com.ead.course.models.UserModel;
import com.ead.course.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;
import java.util.UUID;

@Component
public class CourseValidator implements Validator {

    @Autowired
    @Qualifier("defaultValidator")
    private Validator validator;

    @Autowired
    UserService userService;

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

    private void validateUserInstructor(UUID userInstructor, Errors errors) {
        Optional<UserModel> userModelOptional = userService.findById(userInstructor);
        if (!userModelOptional.isPresent())
            errors.rejectValue("userInstructor", "UserInstructorError", "Instructor not found.");

        if (userModelOptional.get().getUserType().equals(UserType.STUDENT.toString()))
            errors.rejectValue("userInstructor", "UserInstructorError", "User must be INSTRUCTOR or ADMIN.");
    }
}
