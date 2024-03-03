package Helpers;

/**
 * to enable the debugging for the application, it will help
 * in quick turning on and off for debugging while observing from 
 * the command line
 * 
 * @since 1.0.0
 * */
public class DebuggingHelper {
    /**
     * to set the debugging status of the application
     * */
    public static boolean DEBUG_STATUS = true;
    /**
     * to set the testing status of the application
     * */
    public static boolean TESTING_STATUS = false;
    
    /**
     * to write the debug line without new line character,
     * only print the line if {@code DEBUG_STATUS} is set to 
     * true.
     * 
     * @param value the string that you need to print on the command line
     * @see DebuggingHelper#Debugln(String) 
     * @see DebuggingHelper#DEBUG_STATUS 
     * @since 1.0.0
     * */
    public static void Debug(String value) {
        if (DEBUG_STATUS) {
            System.out.print(value);
        }
    }
    
    /**
     * to write the debug line with new line character
     * 
     * @param value the string that you need to print on command line
     * @see DebuggingHelper#Debug(String) 
     * @since 1.0.0
     * */
    public static void Debugln(String value) {
        if (DEBUG_STATUS) {
            System.out.println(value);
        }
    }
}
