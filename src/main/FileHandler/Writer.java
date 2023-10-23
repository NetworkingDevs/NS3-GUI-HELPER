package FileHandler;

import java.io.FileWriter;
import java.io.IOException;

public class Writer {
    String path;
    FileWriter writer;

    public Writer(String path) {
        this.path = path;
        try {
            this.writer = new FileWriter(this.path+"\\output.txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeToFile(String data) {
        try {
            this.writer.append(data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void closeTheFile() {
        try {
            this.writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
