package ru.nsu.yakovleva.util;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import lombok.Data;
import lombok.SneakyThrows;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Utility class for analyzing test results and checking documentation existence.
 */
@Data
public class TestsAndDocs {
    private int passedTests;
    private int totalTests;
    private int ignoredTests;
    private String documentationExists;

    /**
     * Analyzes the test results and checks the existence of documentation.
     *
     * @param testResPath      The path to the XML file containing test results.
     * @param documentationDir The directory path where the documentation is expected.
     */
    @SneakyThrows
    public void analyze(String testResPath, String documentationDir) {
        DocumentBuilderFactory factory = DocumentBuilderFactory
                .newInstance(); // Factory for document builder
        DocumentBuilder builder = factory
                .newDocumentBuilder();
        Document junitDoc = builder
                .parse(new File(testResPath)); // Parsing test results
        Element junitTestSuite = (Element) junitDoc
                .getElementsByTagName("testsuite")
                .item(0); // Collection of tests grouped together and run as a single unit
        totalTests = Integer.parseInt(junitTestSuite.getAttribute("tests"));
        int failedTests = Integer.parseInt(junitTestSuite.getAttribute("failures"));
        ignoredTests = Integer.parseInt(junitTestSuite.getAttribute("skipped"));
        passedTests = totalTests - failedTests - ignoredTests;
        Path documentationPath = Paths.get(documentationDir);
        documentationExists = Files.exists(documentationPath) ? "Generated" : "Missing";
    }
}