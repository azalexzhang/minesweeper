package persistence;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

// A writer that writes leaderboard data to a file. The Writer.java code was designed in a similar
// way to the TellerApp project, but uses FileWriter and BufferedWriter instead.
public class Writer {
    private FileWriter fileWriter;
    private BufferedWriter bufferedWriter;

    // EFFECTS: constructs a writer that will write data to a file
    public Writer(File file) throws IOException {
        fileWriter = new FileWriter(file);
        bufferedWriter = new BufferedWriter(fileWriter);
    }

    public void write(long l) throws IOException {
        bufferedWriter.write(String.valueOf(l));
        bufferedWriter.newLine();
    }

    // MODIFIES: this
    // EFFECTS: closes PrintWriter
    public void close() throws IOException {
        bufferedWriter.close();
        fileWriter.close();
    }
}
