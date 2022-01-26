package com.ead.course.specifications;

import com.ead.course.models.CourseModel;
import com.ead.course.models.CourseUserModel;
import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import java.util.Collection;
import java.util.UUID;

public class SpecificationTemplate {

    @And({
            @Spec(path = "courseLevel", spec = Equal.class),
            @Spec(path = "courseStatus", spec = Equal.class),
            @Spec(path = "name", spec = Like.class)
    })
    public interface CourseSpec extends Specification<CourseModel>{}

    @And(@Spec(path = "title", spec = Like.class))
    public interface ModuleSpec extends Specification<ModuleModel>{}

    @And(@Spec(path = "title", spec = Like.class))
    public interface LessonSpec extends Specification<LessonModel>{}

    public static Specification<ModuleModel> moduleCourseId(final UUID courseId) {
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);
            Root<ModuleModel> module = root; //Entity A - Module
            Root<CourseModel> course = query.from(CourseModel.class); //Entity B - Course
            Expression<Collection<ModuleModel>> coursesModules = course.get("modules"); //Collection of A Entity (Module) in B Entity (Course)
            return criteriaBuilder.and(criteriaBuilder.equal(course.get("courseId"), courseId), criteriaBuilder.isMember(module, coursesModules)); //CriteriaBuilder expression
        };
    }

    public static Specification<LessonModel> lessonModuleId(final UUID moduleId) {
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);
            Root<LessonModel> lesson = root; //Entity A - Lesson
            Root<ModuleModel> module = query.from(ModuleModel.class); //Entity B - Module
            Expression<Collection<LessonModel>> coursesLessons = module.get("lessons"); //Collection of A Entity (Lesson) in B Entity (Module)
            return criteriaBuilder.and(criteriaBuilder.equal(module.get("moduleId"), moduleId), criteriaBuilder.isMember(lesson, coursesLessons)); //CriteriaBuilder expression
        };
    }

    public static Specification<CourseModel> courseUserId(final UUID userId) {
        return (root, query, cb) -> {
            query.distinct(true);
            Join<CourseModel, CourseUserModel> courseProd = root.join("coursesUsers"); // Join entre as duas entidades CourseModel e CourseUserModel, usando o atributo "coursesUsers" da entidade CourseModel para o join
            return cb.equal(courseProd.get("userId"), userId); // com a consulta definida na linha acima, filtra apenas as linhas com o userId desejado
        };
    }
}
