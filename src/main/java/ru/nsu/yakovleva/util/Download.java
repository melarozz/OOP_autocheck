package ru.nsu.yakovleva.util;

import lombok.SneakyThrows;

/**
 * Utility class for downloading files from a Git repository.
 */
public class Download {

    /**
     * The default path to the destination folder.
     */
    public static final String labs = "src/main/resources/labs/";

    /**
     * Downloads files from a Git repository to a specified folder and branch.
     *
     * @param repo   The URL of the Git repository.
     * @param folder The destination folder.
     * @param branch The branch name to clone.
     * @return True if the download is successful, false otherwise.
     */
    @SneakyThrows
    public static boolean download(String repo, String folder, String branch) {
        // Process builder doesn't have to be closed anyhow
        ProcessBuilder processBuilder = new ProcessBuilder("git",
                "clone", "-b", branch, repo, labs + folder);
        Process process = processBuilder.start();
        int exitCode = process.waitFor(); // Wait for completion
        return exitCode == 0;
    }
}
