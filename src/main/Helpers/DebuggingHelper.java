/**
 * Program name: DebuggingHelper
 * Program date: 03-01-2024
 * Program owner: henil
 * Contributor:
 * Last Modified: 03-01-2024
 * <p>
 * Purpose: This class will be used to debug in project. While testing and stuff.
 * </p>
 */
package Helpers;

public class DebuggingHelper {
    public static boolean DEBUG_STATUS = true;
    public static void Debug(String value) {
        if (DEBUG_STATUS) {
            System.out.print(value);
        }
    }

    public static void Debugln(String value) {
        if (DEBUG_STATUS) {
            System.out.println(value);
        }
    }
}
