package ru.nsu.yakovleva;

import java.io.File;
import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.SneakyThrows;
import ru.nsu.yakovleva.dsl.CourseConfiguration;
import ru.nsu.yakovleva.dsl.Group;
import ru.nsu.yakovleva.dsl.Student;
import ru.nsu.yakovleva.dsl.TaskAssignment;
import ru.nsu.yakovleva.util.Download;
import ru.nsu.yakovleva.util.GitHubCommitCounter;
import ru.nsu.yakovleva.util.Run;
import ru.nsu.yakovleva.util.TableBuild;
import ru.nsu.yakovleva.util.TestsAndDocs;


/**
 * Main application class for processing course configurations,
 * student assignments, and generating reports.
 */
public class App {
    // Directory paths
    private static final String testResDir = "/build/test-results/test/";
    private static final String documentationDir = "/build/docs/javadoc/";
    private static final String labDir = "src/main/resources/labs/";

    // Date range for activity calculation
    private static final LocalDate sinceDate = LocalDate.parse("2023-09-12");
    private static final LocalDate untilDate = LocalDate.parse("2023-11-28");

    /**
     * Main method to run the application.
     *
     * @param args Command-line arguments.
     */
    @SneakyThrows
    public static void main(String[] args) {
        CourseConfiguration config = new CourseConfiguration();
        URI configPath = Objects.requireNonNull(App.class.getClassLoader()
                .getResource("configuration.groovy")).toURI(); //путь к конфигу
        config.runFrom(configPath); //запуск конфига
        config.postProcess(); //постобработка конфига
        for (Group group : config.getGroups()) { //для всех групп из конфига
            int tasks = 0;
            for (Student student : group.getStudents()) { //все студенты группы
                boolean downloaded = Download.download(student.getRepo(), student.getUsername(),
                        config.getSettings().getBranch()); //качаем репу

                String repo = extractRepo(student.getRepo());
                int[] activityArray = generateActivityArray(student.getUsername(), repo,
                        sinceDate, untilDate);
                String activityPercentage = calculateActivity(activityArray);
                student.setActivityPercentage(activityPercentage);

                for (TaskAssignment assignment : student.getAssignments()) { //все заданные таски
                    System.out.println(assignment);
                    tasks++;
                    if (!downloaded) {
                        assignment.setBuild("Failed to download");
                        continue;
                    }
                    String assignmentDirPath = labDir + student.getUsername() + "/"
                            + assignment.getInfo().getId();
                    File assignmentDir = new File(assignmentDirPath);
                    if (!assignmentDir.exists()) {
                        assignment.setBuild("Failed to build");
                        continue;
                    }
                    if (!Run.run(assignmentDirPath)) { //запуск сборки и тестов
                        assignment.setBuild("Failed to build");
                        continue;
                    } else {
                        assignment.setBuild("Successfully");
                    }

                    //анализ тестов и доков
                    TestsAndDocs testsAndDocs = new TestsAndDocs();
                    File testResFolder = new File(labDir + student.getUsername()
                            + "/" + assignment.getInfo().getId() + "/" + testResDir);
                    try {
                        String xmlFile = Objects.requireNonNull(
                                testResFolder.listFiles((dir, name) ->
                                        name.toLowerCase().endsWith(".xml")))[0].getName();
                        testsAndDocs.analyze(labDir
                                + student.getUsername() + "/"
                                + assignment.getInfo().getId()
                                + testResDir + xmlFile, labDir
                                + student.getUsername() + "/"
                                + assignment.getInfo().getId() + "/" + documentationDir);
                        assignment.setDocs(testsAndDocs.getDocumentationExists());
                        assignment.setTestsPassed(testsAndDocs.getPassedTests());
                        assignment.setTestsTotal(testsAndDocs.getTotalTests());
                        if ((Objects.equals(testsAndDocs.getDocumentationExists(), "Generated"))
                                && (testsAndDocs.getPassedTests() == testsAndDocs.getTotalTests())) {
                            assignment.setPoints(1);
                        } else {
                            assignment.setPoints(0);
                        }
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }
            }
            group.setTasks(tasks);
        }

        //генерация таблицы
        TableBuild.generateHtmlTableChart(config.getGroups());
        //чистим директорию
        if (!purgeDirectory(new File(labDir))) {
            System.out.println("Delete the repos yourself!");
        }
    }

    // Recursively delete files
    private static boolean purgeDirectory(File dir) {
        for (File file : Objects.requireNonNull(dir.listFiles())) {
            if (file.isDirectory()) {
                purgeDirectory(file);
            }
            if (!file.delete()) {
                return false;
            }
        }
        return true;
    }

    // Generate activity array based on commit count per week
    private static int[] generateActivityArray(String owner, String repo,
                                               LocalDate sinceDate, LocalDate untilDate) {
        List<Integer> activityList = new ArrayList<>();
        LocalDate currentDate = sinceDate;
        while (!currentDate.isAfter(untilDate)) {
            int commitCount = GitHubCommitCounter.count(owner, repo, currentDate,
                    currentDate.plusDays(6));
            activityList.add(commitCount > 1 ? 1 : 0);
            currentDate = currentDate.plusWeeks(1);
        }

        int[] activityArray = new int[activityList.size()];
        for (int i = 0; i < activityList.size(); i++) {
            activityArray[i] = activityList.get(i);
        }

        return activityArray;
    }

    // Calculate activity percentage
    private static String calculateActivity(int[] activityArray) {
        int totalWeeks = activityArray.length;
        int activeWeeks = 0;

        for (int activity : activityArray) {
            if (activity == 1) {
                activeWeeks++;
            }
        }

        int result = (int) (((double) activeWeeks / totalWeeks) * 100);
        return String.valueOf(result);
    }

    // Extract repository name from URL
    private static String extractRepo(String repoUrl) {
        String[] parts = repoUrl.split("/");
        return parts[parts.length - 1];
    }
}
