package ru.nsu.yakovleva.dsl;

import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Course Configuration.
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CourseConfiguration extends Configuration {
    List<Student> allStudents;
    List<Group> groups;
    List<TaskInformation> tasks;
    CourseSettings settings;
}
