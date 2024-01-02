package FileHandler;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Scanner;

public class Reader {
    Scanner scanner;
    StringBuilder data;
    URL path;
    long length;

    public Reader(URL path) {
        this.path = path;
        this.data = new StringBuilder();
        try {
            this.length = new File(this.path.toURI()).length();
            this.scanner = new Scanner(new File(path.toURI()));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public void read() {
        while (this.scanner.hasNext()) {
            this.data.append(this.scanner.nextLine());
        }
        // just while debugging...
        System.out.println(data);
    }

    public boolean isFileEmpty() {
        // just while debugging...
        System.out.println("Length : "+length);
        return (this.length <= 0);
    }
}
