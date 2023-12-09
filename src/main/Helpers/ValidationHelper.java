package Helpers;

import javax.swing.*;
import java.util.ArrayList;

/**
 * Program name: ValidationHelper
 * Program date: 10th December 2023
 * Program owner: Henil Mistry
 * Contributor:
 * Last Modified: 10th December 2023
 *
 * Purpose: This class is will help for validation purpose in various topologies...
 * */
public class ValidationHelper {

    // various topology types starts here...
    public static String TOPOLOGY_RING = "RING";
    public static String TOPOLOGY_MESH = "MESH";
    public static String TOPOLOGY_STAR = "STAR";
    // various topology types ends here...

    public static JFrame parent_frame; // for showing the message dialogs...
    public static String TOPOLOGY = TOPOLOGY_RING; // for tracking that which type of topology is here...

    // validation of topology starts here ==============================================================================
    // This method will calculate the expected no. of links that has to be there in given topology...
    private static int calculateExpectedDeviceSize(int nodes) {
        if (TOPOLOGY == TOPOLOGY_RING) {
            return nodes;
        } else if (TOPOLOGY == TOPOLOGY_MESH) {
            return ((nodes)*(nodes - 1)) / 2;
        } else {
            return nodes-1;
        }
    }

    public static boolean validateTopology(String str_nodes, ArrayList<DeviceHelper> lst_devices) {
        int nodes = Integer.parseInt(str_nodes);
        int device_size = calculateExpectedDeviceSize(nodes);

        // validate textField:Nodes (it should be number)
        if (str_nodes.length() == 0 || !str_nodes.chars().allMatch(Character::isDigit)) {
            JOptionPane.showMessageDialog(parent_frame,"Please enter valid no. of nodes!","WARNING",JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // validate configuration:Topology Configuration (each link has been configured)
        if (lst_devices.size() != device_size) {
            JOptionPane.showMessageDialog(parent_frame,"Please configure topology with valid links and network configuration!","WARNING",JOptionPane.WARNING_MESSAGE);
            return false;
        }

        return true;
    }
    // validation of topology ends here ================================================================================
}
