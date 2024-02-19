/**
 * Program name: FileReaderWriter
 * Program date: 03-01-2024
 * Program owner: henil
 * Contributor:
 * Last Modified: 03-01-2024
 * <p>
 * Purpose: This class will help me to read and write from files very easily...
 */
package FileHandler;

import Helpers.DebuggingHelper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileReaderWriter {

    public static void writeUsingPath(String data, String path) {
        DebuggingHelper.Debugln("Using FileWriter directly!");
        try {
            FileWriter writer = new FileWriter(path);
            writer.write(data);
            DebuggingHelper.Debugln("Writing Completed to file! "+path);
            writer.close();
            DebuggingHelper.Debugln("File has been closed After successful write!!");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeUsingURL(String data, URL uri) {
        DebuggingHelper.Debugln("Using URL to write to the file!");
        try {
            FileWriter writer = new FileWriter(uri.getPath());
            writer.write(data);
            DebuggingHelper.Debugln("Writing completed to file at url : "+uri.toString());
            writer.close();
            DebuggingHelper.Debugln("File has been closed After successful write!!");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String readUsingPath(String path) {
        String data = new String();
        try {
            DebuggingHelper.Debugln("Reading from file : "+path.toString());
            data = new String(Files.readAllBytes(Paths.get(path)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        DebuggingHelper.Debugln("The data is : "+data);
        return data;
    }

    public static String readIfEmptyUsingPath(String data, String path) {
        String settings = "{}";
        if (isFileEmpty(path)) {
            DebuggingHelper.Debugln("File is empty! Writing default settings!!");
            writeUsingPath(data, path);
        } else {
            DebuggingHelper.Debugln("FIle is not empty! Loading the settings!");
             settings = readUsingPath(path);
        }
        DebuggingHelper.Debugln("The data is : "+settings);
        return settings;
    }

    private static boolean isFileEmpty(String path) {
        DebuggingHelper.Debugln("Getting the file length...");
        return ((new File(path).length()) <= 0);
    }

}
