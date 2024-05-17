package ru.nsu.yakovleva.dsl;

import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Group.
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Group extends Configuration {
    String name;
    List<Student> students;
    Integer tasks;
}
