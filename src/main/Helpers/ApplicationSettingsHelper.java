/**
 * <p>
 *     The following package will be managing the helper functions,
 *     to manage various operations for the applications.
 * </p>
 * */
package Helpers;

import FileHandler.FileReaderWriter;
import Ns3Objects.Links.NetworkLink;
import Ns3Objects.Links.P2P;
import Ns3Objects.Links.CSMA;

import Ns3Objects.Netoworks.Network;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * This class will be used for managing the settings for application environment
 *
 * @since 1.0.0
 * */
public class ApplicationSettingsHelper {
    // list of settings...
    /**
     * the file path for code generation
     * */
    public static String OUTPUT_PATH = "outputPath";
    /**
     * for key of the file name
     * */
    public static String FILE_NAME = "fileName";
    /**
     * for key of the default links
     * */
    public static String DEFAULT_LINKS = "defaultLinks";
    /**
     * for default network settings
     * */
    public static String DEFAULT_NETWORKS = "defaultNetworks";


    /**
     * for storing the settings of the application
     * */
    public static JSONObject UNIVERSAL_SETTINGS = new JSONObject();
    /**
     * for getting the default output path for the file generation
     * */
    public static String OutputPath = System.getProperty("user.dir");

    // static methods...
    /**
     * to enable the application with basic settings, for the first time
     *
     * @see ApplicationSettingsHelper#setUpSettings(String)
     * @since 1.0.0
     * */
    public static void setUpSettings() {
        LoggingHelper.LogFunction("Settings Helper : set up settings called!");
        setUpSettings(System.getProperty("user.dir"));
    }

    /**
     * to check the content of the settings file, restoring the settings from the file
     *
     * @param OutputPath the path of the file
     * @see ApplicationSettingsHelper#setUpSettings()
     * @since 1.0.0
     * */
    public static void setUpSettings(String OutputPath) {
        LoggingHelper.LogFunction("Settings Helper : set up settings called!");
        UNIVERSAL_SETTINGS.put(FILE_NAME, "output");
        UNIVERSAL_SETTINGS.put(OUTPUT_PATH, OutputPath);
        String d = FileReaderWriter.readIfEmptyUsingPath(UNIVERSAL_SETTINGS.toString(), OutputPath+"\\file.txt");
        JSONObject objJ = new JSONObject(d);
        for (String key : objJ.keySet()) {
            if (UNIVERSAL_SETTINGS.has(key)) {
                UNIVERSAL_SETTINGS.remove(key);
                UNIVERSAL_SETTINGS.put(key, objJ.get(key));
            } else {
                UNIVERSAL_SETTINGS.put(key, objJ.get(key));
            }
        }
    }

    /**
     * for saving the settings to the file
     *
     * @since 1.1.0
     * */
    public static void saveSettings()
    {
        FileReaderWriter.writeUsingPath(UNIVERSAL_SETTINGS.toString(), OutputPath+"\\file.txt");
        LoggingHelper.LogDebug("Updated to file!! Settings has been saved in file!!");
    }

    /**
     * to check whether the settings object has default links
     *
     * @return boolean variable indicating presence of the default links
     * @since 1.0.0
     * */
    public static boolean hasDefaultLinks() {
        LoggingHelper.LogFunction("Settings Helper : has default links called!");
        return UNIVERSAL_SETTINGS.has(DEFAULT_LINKS);
    }

    /**
     * to check whether the settings object has default networks
     *
     * @return boolean variable indicating presence of the default networks
     * @since 1.0.0
     * */
    public static boolean hasDefaultNetworks() {
        LoggingHelper.LogFunction("Settings Helper : has default networks called!");
        return UNIVERSAL_SETTINGS.has(DEFAULT_NETWORKS);
    }

    /**
     * to get the default links
     *
     * @return list of default links
     * @since 1.0.0
     * */
    public static ArrayList<NetworkLink> getDefaultLinks() {
        LoggingHelper.LogFunction("Settings Helper : get default links called!");
        ArrayList<NetworkLink> links = new ArrayList<>();
        if (hasDefaultLinks()) {
            for (Object data : ((JSONArray)UNIVERSAL_SETTINGS.get(DEFAULT_LINKS))) {
                String[] params = data.toString().split("\\|");
                for (String str : params) {
                    LoggingHelper.LogDebug("Str : "+str);
                }
                if (params[params.length-1].equalsIgnoreCase(NetworkLink.LABEL_CSMA)) {
                    links.add(new CSMA(links.size(), params[0], params[1], params[2], params[3], ((params[4].equalsIgnoreCase("Y"))?(true):(false)), true));
                } else { // default or p2p case...
                    links.add(new P2P(links.size(), params[0], params[1], params[2], params[3], ((params[4].equalsIgnoreCase("Y"))?(true):(false)), true));
                }
            }
            LoggingHelper.LogDebug("Yes! Universal Settings has Default Links!");
        } else {
            LoggingHelper.LogDebug("No! Universal Settings has no default links!");
        }

        LoggingHelper.LogDebug("Returning the links as array list.");
        return links;
    }

    /**
     * to get the default networks
     *
     * @return list of default networks
     * @since 1.0.0
     * */
    public static ArrayList<Network> getDefaultNetworks() {
        LoggingHelper.LogFunction("Settings Helper : get default networks called!");
        ArrayList<Network> networks = new ArrayList<>();
        if (hasDefaultNetworks()) {
            for (Object data : ((JSONArray)UNIVERSAL_SETTINGS.get(DEFAULT_NETWORKS))) {
                String[] params = data.toString().split("\\|");
                for (String str : params) {
                    LoggingHelper.LogDebug("Str : "+str);
                }
                networks.add(new Network(0, params[0], params[1], params[2], true));
            }
            LoggingHelper.LogDebug("Yes! Universal Settings has Default Networks!");
        } else {
            LoggingHelper.LogDebug("No! Universal Settings has no default Networks!");
        }

        LoggingHelper.LogDebug("Returning the networks as array list.");
        return networks;
    }

    /**
     * to set the default links
     *
     * @since 1.1.0
     * */
    public static void saveDefaultLinks(ArrayList<NetworkLink> links) {
        LoggingHelper.LogFunction("Settings Helper : save default links called!");
        if (links.size() > 0) {
            if (UNIVERSAL_SETTINGS.has(DEFAULT_LINKS)) {
                UNIVERSAL_SETTINGS.remove(DEFAULT_LINKS);
            }
            ArrayList<String> str_links = new ArrayList<>();
            for (NetworkLink link : links) {
                str_links.add(link.forSettings());
            }
            UNIVERSAL_SETTINGS.put(DEFAULT_LINKS, str_links);
        }
    }

    /**
     * to set the default networks
     *
     * @since 1.1.0
     * */
    public static void saveDefaultNetworks(ArrayList<Network> networks) {
        LoggingHelper.LogFunction("Settings Helper : save default networks called!");
        if (networks.size() > 0) {
            if (UNIVERSAL_SETTINGS.has(DEFAULT_NETWORKS)) {
                UNIVERSAL_SETTINGS.remove(DEFAULT_NETWORKS);
            }
            ArrayList<String> str_networks = new ArrayList<>();
            for (Network network : networks) {
                str_networks.add(network.forSettings());
            }
            UNIVERSAL_SETTINGS.put(DEFAULT_NETWORKS, str_networks);
        }
    }
}
