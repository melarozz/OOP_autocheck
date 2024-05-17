package ru.nsu.yakovleva.util;

import java.io.File;
import lombok.Cleanup;
import lombok.SneakyThrows;
import org.gradle.tooling.GradleConnector;
import org.gradle.tooling.ProjectConnection;

/**
 * Utility class for running Gradle tasks.
 */
public class Run {

    /**
     * Runs Gradle tasks for a specified project directory.
     *
     * @param labDir The directory of the project.
     * @return True if the tasks are run successfully, false otherwise.
     */
    @SneakyThrows
    public static boolean run(String labDir) {
        GradleConnector connector = GradleConnector.newConnector();
        connector.forProjectDirectory(new File(labDir)); // Project directory
        @Cleanup ProjectConnection connection = connector.connect(); // Connect to the project
        connection.newBuild()
                .forTasks("test", "javadoc")
                .run();
        return true;
    }
}
