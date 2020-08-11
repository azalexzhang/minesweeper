package persistence;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

// Reads leaderboard data from a file. The persistence package was designed similar to the mechanics used in
// the TellerApp.
public class Reader {
    // EFFECTS: returns the leaderboard from the file; throws IOException if it encounters one when opening
    //          or reading the file
    public static List<Long> readLeaderboard(File file) throws IOException {
        return parseScores(readFile(file));
    }

    // EFFECTS: returns the saved board from the file; throws IOException if it encounters one when opening
    //          or reading the file
    public static ArrayList<String> readBoard(File file) throws IOException {
        return parseBoard(readFile(file));
    }

    // EFFECTS: returns content of file as a list of strings, each string containing the content of one row
    //          of the file
    private static List<String> readFile(File file) throws IOException {
        return Files.readAllLines(file.toPath());
    }

    // EFFECTS: returns a list of scores
    private static List<Long> parseScores(List<String> fileContent) {
        List<Long> scores = new ArrayList<>();

        for (String s : fileContent) {
            scores.add(Long.parseLong(s));
        }

        return scores;
    }

    // EFFECTS: returns the board in the form of a list
    private static ArrayList<String> parseBoard(List<String> fileContent) {
        return new ArrayList<>(fileContent);
    }
}
