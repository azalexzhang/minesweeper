package persistence;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReaderTest {
    @Test
    void testReadFiles() {
        try {
            List<Double> testScores =
                    Reader.readLeaderboard(new File("./data/testLeaderboardReaderFile.txt"));
            assertEquals(60, testScores.get(0));
            assertEquals(61, testScores.get(1));
            assertEquals(67, testScores.get(2));
            assertEquals(75, testScores.get(3));

            List<String> testColumn =
                    Reader.readColumn(new File("./data/testColumnReaderFile.txt"));
            assertEquals("false,true,false", testColumn.get(0));
            assertEquals("false,true,false", testColumn.get(1));
            assertEquals("false,false,false", testColumn.get(2));
            assertEquals("false,false,false", testColumn.get(3));
            assertEquals("false,true,false", testColumn.get(4));
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
            // expected
        }

        try {
            Reader.readColumn(new File("./data/does/this/file/exist/doesThisFileExist.txt"));
            fail("Expected IOException never thrown.");
        } catch (IOException e) {
            // expected
        }

        try {
            Reader.readDimensionsAndTimeData(new
                    File("./data/does/this/file/exist/doesThisFileExist.txt"));
            fail("Expected IOException never thrown.");
        } catch (IOException e) {
            // expected
        }
    }
}
