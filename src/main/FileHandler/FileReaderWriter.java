/**
 * <p>
 *     The following package is containing the utilities to manage
 *     any type of file handling and code generation features.
 * </p>
 * */
package FileHandler;

import Helpers.LoggingHelper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * to manage file handling operations
 * */
public class FileReaderWriter {

    /**
     * to write in file using the path of the file
     * in string format
     *
     * @param data the data that needs to be written
     * @param path the path of the file
     * @since 1.0.0
     * */
    public static void writeUsingPath(String data, String path) {
        LoggingHelper.LogDebug("Using FileWriter directly!");
        try {
            FileWriter writer = new FileWriter(path);
            writer.write(data);
            LoggingHelper.LogDebug("Writing Completed to file! "+path);
            writer.close();
            LoggingHelper.LogDebug("File has been closed After successful write!!");
        } catch (IOException e) {
            LoggingHelper.LogError("Error in write using path");
            throw new RuntimeException(e);
        }
    }

    /**
     * to write in the file using the URL of the file
     *
     * @param data the data that needs to be written
     * @param url the URL of the file
     * @since 1.0.0
     * */
    public static void writeUsingURL(String data, URL url) {
        LoggingHelper.LogDebug("Using URL to write to the file!");
        try {
            FileWriter writer = new FileWriter(url.getPath());
            writer.write(data);
            LoggingHelper.LogDebug("Writing completed to file at url : "+url.toString());
            writer.close();
            LoggingHelper.LogDebug("File has been closed After successful write!!");
        } catch (IOException e) {
            LoggingHelper.LogError("Error in write using URL");
            throw new RuntimeException(e);
        }
    }

    /**
     * to read the file using the path of the file
     *
     * @param path the path of the file
     * @return The data that is being read from the file
     * @since 1.0.0
     * */
    public static String readUsingPath(String path) {
        String data = new String();
        try {
            LoggingHelper.LogDebug("Reading from file : "+path.toString());
            data = new String(Files.readAllBytes(Paths.get(path)));
        } catch (IOException e) {
            LoggingHelper.LogError("Error in read using path");
            throw new RuntimeException(e);
        }
        LoggingHelper.LogDebug("The data is : "+data);
        return data;
    }

    /**
     * to read the file, only if the file is not empty and
     * writing into file if it's empty
     *
     * @param data the default data that will be written if file is empty
     * @param path path of the file
     * @return The string that contains the data that is being read from the file
     * if the file is empty, if not, it will return empty JSON object
     * @since 1.0.0
     * */
    public static String readIfEmptyUsingPath(String data, String path) {
        String settings = "{}";
        if (isFileEmpty(path)) {
            LoggingHelper.LogDebug("File is empty! Writing default settings!!");
            writeUsingPath(data, path);
        } else {
            LoggingHelper.LogDebug("FIle is not empty! Loading the settings!");
             settings = readUsingPath(path);
        }
        LoggingHelper.LogDebug("The data is : "+settings);
        return settings;
    }

    /**
     * to check that whether the file is empty or not
     *
     * @param path the path of the file
     * @return The boolean value indicating that the file is empty or not
     * @since 1.0.0
     * */
    private static boolean isFileEmpty(String path) {
        LoggingHelper.LogDebug("Getting the file length...");
        return ((new File(path).length()) <= 0);
    }

}
