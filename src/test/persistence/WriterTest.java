package persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class WriterTest {
    private static final String TEST_FILE = "./data/testLeaderboard.txt";
    private Writer testWriter;
    private List<Long> testScores;

    @BeforeEach
    void runBefore() throws IOException {
        testWriter = new Writer(new File(TEST_FILE));
        testScores = new ArrayList<>();
        testScores.add((long) 64);
        testScores.add((long) 72);
        testScores.add((long) 84);
    }

    @Test
    void testWriteScores() {
        try {
            for (long s : testScores) {
                testWriter.write(s);
            }

            testWriter.close();

            List<Double> scores = Reader.readLeaderboard(new File(TEST_FILE));
            assertEquals(64, scores.get(0));
            assertEquals(72, scores.get(1));
            assertEquals(84, scores.get(2));
        } catch (IOException e) {
            fail("Unexpected IOException\n" + e);
        }
    }
}
