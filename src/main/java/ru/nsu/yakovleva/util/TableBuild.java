package ru.nsu.yakovleva.util;

import static freemarker.template.Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS;

import freemarker.template.Configuration;
import freemarker.template.Template;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Cleanup;
import lombok.SneakyThrows;
import ru.nsu.yakovleva.dsl.Group;


/**
 * Utility class for generating HTML table charts using Freemarker templates.
 */
public class TableBuild {

    /**
     * The directory where the generated HTML files will be saved.
     */
    public static final String resultDir = "src/main/resources/results/";

    /**
     * The path to the Freemarker template file.
     */
    public static final String templatePath = "template.ftl";

    /**
     * Generates an HTML table chart based on the provided list of groups.
     *
     * @param groups The list of groups to be included in the table.
     */
    @SneakyThrows
    public static void generateHtmlTableChart(List<Group> groups) {
        Configuration configuration =
                new Configuration(DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        configuration.setClassForTemplateLoading(TableBuild.class, "/");
        configuration.setDefaultEncoding("UTF-8");
        File out = new File(resultDir, "output.html");
        @Cleanup Writer fileWriter = new FileWriter(out);  // With auto-closing
        Template template = configuration.getTemplate(templatePath);
        Map<String, Object> dataModel = new HashMap<>(); // Data model for the template
        dataModel.put("groups", groups);
        template.process(dataModel, fileWriter);
    }
}