package persistence;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

// A writer that writes leaderboard data to a file. The Writer.java code was taken from the TellerApp
// project.
public class Writer {
    private PrintWriter printWriter;

    // EFFECTS: constructs a writer that will write data to a file
    public Writer(File file) throws FileNotFoundException, UnsupportedEncodingException {
        printWriter = new PrintWriter(file, "UTF-8");
    }

    // MODIFIES: this
    // EFFECTS: writes saveable to file
    public void write(Saveable saveable) {
        saveable.save(printWriter);
    }

    // MODIFIES: this
    // EFFECTS: closes PrintWriter
    public void close() {
        printWriter.close();
    }
}
