package persistence;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

// Reads leaderboard data from a file. The persistence package was designed similar to the mechanics used in
// the TellerApp.
public class Reader {
    public static List<Long> readLeaderboard(File file) throws IOException {
        return parseScores(readFile(file));
    }

    private static List<String> readFile(File file) throws IOException {
        return Files.readAllLines(file.toPath());
    }

    private static List<Long> parseScores(List<String> fileContent) {
        List<Long> scores = new ArrayList<>();

        for (String s : fileContent) {
            scores.add(Long.parseLong(s));
        }

        return scores;
    }
}
