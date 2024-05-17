package ru.nsu.yakovleva.dsl;

import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.nsu.yakovleva.dsl.Configuration;

/**
 * Student.
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Student extends Configuration {
    String username;
    String name;
    String repo;
    List<TaskAssignment> assignments;
    String activityPercentage;
}
