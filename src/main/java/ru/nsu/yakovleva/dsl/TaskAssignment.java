package ru.nsu.yakovleva.dsl;

import java.time.LocalDate;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * Task Assignment.
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TaskAssignment extends Configuration {
    TaskInformation info;
    LocalDate softDeadline;
    LocalDate hardDeadline;
    String build = "";
    String docs = "";
    int testsTotal;
    int testsPassed;
    int testsIgnored;
    int points;
}
