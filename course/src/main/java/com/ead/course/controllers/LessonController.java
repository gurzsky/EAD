package com.ead.course.controllers;

import com.ead.course.dtos.LessonDto;
import com.ead.course.dtos.ModuleDto;
import com.ead.course.models.CourseModel;
import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.services.LessonService;
import com.ead.course.services.ModuleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/modules/{moduleId}/lessons")
public class LessonController {

    @Autowired
    private LessonService lessonService;

    @Autowired
    private ModuleService moduleService;

    @PostMapping
    public ResponseEntity<Object> saveLesson(@PathVariable(value = "moduleId") UUID moduleId,
                                             @RequestBody @Validated LessonDto lessonDto) {
        Optional<ModuleModel> moduleModelOptional = moduleService.findById(moduleId);
        if(!moduleModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module Not Found");
        }
        var lessonModule = new LessonModel();
        BeanUtils.copyProperties(lessonDto, lessonModule);
        lessonModule.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        lessonModule.setModule(moduleModelOptional.get());
        return ResponseEntity.status(HttpStatus.CREATED).body(lessonService.save(lessonModule));
    }

    @DeleteMapping("/{lessonId}")
    public ResponseEntity<Object> deleteCourse(@PathVariable(value = "moduleId")UUID moduleId,
                                               @PathVariable(value = "lessonId")UUID lessonId) {
        Optional<LessonModel> lessonModelOptional = lessonService.findLessonIntoModule(moduleId, lessonId);

        if(lessonModelOptional.isPresent()) {
            lessonService.delete(lessonModelOptional.get());
            return ResponseEntity.status(HttpStatus.OK).body("Lesson deleted successfully");
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lesson Not Found");
    }

    @PutMapping("/{lessonId}")
    public ResponseEntity<Object> updateModule(@PathVariable(value = "moduleId")UUID moduleId,
                                               @PathVariable(value = "lessonId")UUID lessonId,
                                               @RequestBody @Validated LessonDto lessonDto) {
        Optional<LessonModel> lessonModelOptional = lessonService.findLessonIntoModule(moduleId, lessonId);
        if (!lessonModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lesson not found for this module");
        }

        var lessonModel = lessonModelOptional.get();
        lessonModel.setTitle(lessonDto.getTitle());
        lessonModel.setDescription(lessonDto.getDescription());
        lessonModel.setVideoUrl(lessonDto.getVideoUrl());
        return ResponseEntity.status(HttpStatus.CREATED).body(lessonService.save(lessonModel));
    }

    @GetMapping
    public ResponseEntity<List<LessonModel>> getAllLessons(@PathVariable(value = "moduleId")UUID moduleId) {
        return ResponseEntity.status(HttpStatus.OK).body(lessonService.findAllByModule(moduleId));
    }

    @GetMapping("/{moduleId}")
    public ResponseEntity<Object> getOneLesson(@PathVariable(value = "moduleId")UUID moduleId,
                                               @PathVariable(value = "lessonId")UUID lessonId) {
        Optional<LessonModel> lessonModelOptional = lessonService.findLessonIntoModule(moduleId, lessonId);
        if (!lessonModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lesson not found for this module");
        }
        return ResponseEntity.status(HttpStatus.OK).body(lessonModelOptional.get());
    }

}
