package persistence;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReaderTest {
    @Test
    void testParseScoresFile() {
        try {
            List<Double> testScores =
                    Reader.readLeaderboard(new File("./data/testLeaderboardReaderFile.txt"));
            assertEquals(60, testScores.get(0));
            assertEquals(61, testScores.get(1));
            assertEquals(67, testScores.get(2));
            assertEquals(75, testScores.get(3));
        } catch (IOException e) {
            fail("Unexpected IOException\n" + e);
        }
    }

    @Test
    void testIOException() {
        try {
            Reader.readLeaderboard(new File("./data/does/this/file/exist/doesThisFileExist.txt"));
            fail("Expected IOException never thrown.");
        } catch (IOException e) {
            // test passed
        }
    }
}
