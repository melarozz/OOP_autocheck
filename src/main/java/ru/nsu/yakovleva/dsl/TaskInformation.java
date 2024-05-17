package ru.nsu.yakovleva.dsl;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Task Information.
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TaskInformation extends Configuration {
    String id;
    String title;
    Integer points;
}
