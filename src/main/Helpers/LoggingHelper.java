package Helpers;

import StatusHelper.LogLevel;
import com.sun.tools.javac.Main;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * to enable the debugging for the application, it will help
 * in quick turning on and off for debugging while observing from 
 * the command line
 * 
 * @since 1.0.0
 * */
public class LoggingHelper {
    /**
     * custom log level for error
     * */
    private final Level LEVEL_ERROR = Level.parse(String.valueOf(Integer.MAX_VALUE));
    /**
     * custom log level for debug
     * */
    private final Level LEVEL_DEBUG = Level.parse("99");
    /**
     * custom log level for info
     * */
    private final Level LEVEL_INFO = Level.parse("98");
    /**
     * custom log level for function
     * */
    private final Level LEVEL_FUNCTION = Level.parse("97");
    /**
     * custom log level for logic
     * */
    private final Level LEVEL_LOGIC = Level.parse("96");
    /**
     * custom log level for all logs
     * */
    private final Level LEVEL_ALL = Level.parse("95");
    /**
     * custom log level for none logs, setting no logs
     * */
    private final Level LEVEL_NONE = Level.parse(String.valueOf(Integer.MIN_VALUE));
    /**
     * a class as a helping hand in logging process
     * */
    private static Logger logger = Logger.getLogger(String.valueOf(Main.class));
    /**
     * to map the log levels with the priority number
     * */
    private static Map<LogLevel, Level> LOG_LEVEL_MAP = new HashMap<>();
    /**
     * to set the log level for the entire application
     * */
    private static LogLevel LOG_LEVEL = LogLevel.LOG_NONE;

    public LoggingHelper() {
        LOG_LEVEL_MAP.put(LogLevel.LOG_LEVEL_ERROR,LEVEL_ERROR);
        LOG_LEVEL_MAP.put(LogLevel.LOG_LEVEL_DEBUG,LEVEL_DEBUG);
        LOG_LEVEL_MAP.put(LogLevel.LOG_LEVEL_INFO,LEVEL_INFO);
        LOG_LEVEL_MAP.put(LogLevel.LOG_LEVEL_FUNCTION,LEVEL_FUNCTION);
        LOG_LEVEL_MAP.put(LogLevel.LOG_LEVEL_LOGIC,LEVEL_LOGIC);
        LOG_LEVEL_MAP.put(LogLevel.LOG_LEVEL_ALL,LEVEL_ALL);
        LOG_LEVEL_MAP.put(LogLevel.LOG_NONE,LEVEL_NONE);
    }

    /**
     * to set the log level for the entire app
     *
     * @param level The desired log level
     * @since 1.2.0
     * */
    public static void setLogLevel(LogLevel level) {
        LOG_LEVEL = level;
    }

    /**
     * to get the log level for the app
     *
     * @return The log level for the application
     * @since 1.2.0
     * */
    public static LogLevel getLogLevel() {
        return LOG_LEVEL;
    }

    /**
     * to get the priority number for current log level
     *
     * @return The priority number for current log level
     * @since 1.2.0
     * */
    public static int getLogLevelPriority() {
        return LOG_LEVEL_MAP.get(LOG_LEVEL).intValue();
    }

    /**
     * to get the priority number for the desired log level
     *
     * @param level the desired log level
     * @return The priority number for the given log level
     * @since 1.2.0
     * */
    public static int getLogLevelPriority(LogLevel level) {
        return LOG_LEVEL_MAP.get(level).intValue();
    }

    /**
     * to write the error level log information
     *
     * @param msg the msg that will be printed as error level log
     * @see LoggingHelper#LogDebug(String)
     * @see LoggingHelper#LogInfo(String)
     * @see LoggingHelper#LogFunction(String)
     * @see LoggingHelper#LogLogic(String)
     * @see LoggingHelper#Log(String)
     * @since 1.2.0
     * */
    public static void LogError(String msg) {
        // it will only print the debug line if the log level is set to ERROR or above
        if (getLogLevelPriority() >= getLogLevelPriority(LogLevel.LOG_LEVEL_ERROR)) {
            logger.log(LOG_LEVEL_MAP.get(getLogLevel()),"[ERROR] : "+msg);
        }
    }

    /**
     * to write the debug level log information
     *
     * @param msg the msg that will be printed as debug level log
     * @see LoggingHelper#LogError(String)
     * @see LoggingHelper#LogInfo(String)
     * @see LoggingHelper#LogFunction(String)
     * @see LoggingHelper#LogLogic(String)
     * @see LoggingHelper#Log(String)
     * @since 1.2.0
     * */
    public static void LogDebug(String msg) {
        // it will only print the debug line if the log level is set to DEBUG or above
        if (getLogLevelPriority() >= getLogLevelPriority(LogLevel.LOG_LEVEL_DEBUG)) {
            logger.log(LOG_LEVEL_MAP.get(getLogLevel()),"[DEBUG] : "+msg);
        }
    }

    /**
     * to write the info level log information
     *
     * @param msg the msg that will be printed as info level log
     * @see LoggingHelper#LogError(String)
     * @see LoggingHelper#LogDebug(String)
     * @see LoggingHelper#LogFunction(String)
     * @see LoggingHelper#LogLogic(String)
     * @see LoggingHelper#Log(String)
     * @since 1.2.0
     * */
    public static void LogInfo(String msg) {
        // it will only print the debug line if the log level is set to INFO or above
        if (getLogLevelPriority() >= getLogLevelPriority(LogLevel.LOG_LEVEL_INFO)) {
            logger.log(LOG_LEVEL_MAP.get(getLogLevel()),"[INFO] : "+msg);
        }
    }

    /**
     * to write the function level log information
     *
     * @param msg the msg that will be printed as function level log
     * @see LoggingHelper#LogError(String)
     * @see LoggingHelper#LogDebug(String)
     * @see LoggingHelper#LogInfo(String)
     * @see LoggingHelper#LogLogic(String)
     * @see LoggingHelper#Log(String)
     * @since 1.2.0
     * */
    public static void LogFunction(String msg) {
        // it will only print the debug line if the log level is set to FUNCTION or above
        if (getLogLevelPriority() >= getLogLevelPriority(LogLevel.LOG_LEVEL_FUNCTION)) {
            logger.log(LOG_LEVEL_MAP.get(getLogLevel()),"[FUNCTION] : "+msg);
        }
    }

    /**
     * to write the logic level log information
     *
     * @param msg the msg that will be printed as logic level log
     * @see LoggingHelper#LogError(String)
     * @see LoggingHelper#LogDebug(String)
     * @see LoggingHelper#LogInfo(String)
     * @see LoggingHelper#LogFunction(String)
     * @see LoggingHelper#Log(String)
     * @since 1.2.0
     * */
    public static void LogLogic(String msg) {
        // it will only print the debug line if the log level is set to LOGIC or above
        if (getLogLevelPriority() >= getLogLevelPriority(LogLevel.LOG_LEVEL_LOGIC)) {
            logger.log(LOG_LEVEL_MAP.get(getLogLevel()),"[LOGIC] : "+msg);
        }
    }

    /**
     * to write the normal level log information
     *
     * @param msg the msg that will be printed as normal level log
     * @see LoggingHelper#LogError(String)
     * @see LoggingHelper#LogDebug(String)
     * @see LoggingHelper#LogInfo(String)
     * @see LoggingHelper#LogFunction(String)
     * @see LoggingHelper#LogLogic(String)
     * @since 1.2.0
     * */
    public static void Log(String msg) {
        // it will only print the debug line if the log level is set to ALL or above
        if (getLogLevelPriority() >= getLogLevelPriority(LogLevel.LOG_LEVEL_ALL)) {
            logger.log(LOG_LEVEL_MAP.get(getLogLevel()),"[LOG] : "+msg);
        }
    }
}
