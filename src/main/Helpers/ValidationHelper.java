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

    public JFrame parent_frame; // for showing the message dialogs...
    public static String TOPOLOGY = TOPOLOGY_RING; // for tracking that which type of topology is here...
    public ArrayList<String> param = new ArrayList<>(); // for storing the variable parameters for the code generation...

    public ValidationHelper(JFrame parent) {
        this.parent_frame = parent;
    }

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

    public boolean validateTopology(String str_nodes, ArrayList<DeviceHelper> lst_devices) {
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

        // You can add parameters over here...
        param.add(str_nodes);
        return true;
    }
    // validation of topology ends here ================================================================================


    // added this section on 10/12/2023
    // validation of server configuration starts here ==================================================================
    public boolean validateServerConfig(String str_serverIndex, String str_portNo, String str_startTime, String str_upTime, String str_mtu, String str_interval, String str_packets) {

        // validate comboBox:ServerIndex (it should be number)
        if (str_serverIndex.length() == 0 || !str_serverIndex.chars().allMatch(Character::isDigit)) {
            JOptionPane.showMessageDialog(parent_frame,"Please select valid serverIndex!", "WARNING", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // validate textField:PortNumber (it should be number)
        if (str_portNo.length() == 0 || !str_portNo.chars().allMatch(Character::isDigit)) {
            JOptionPane.showMessageDialog(parent_frame,"Please enter valid port number!", "WARNING", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // validate textField:StartTime (it should be number, should be >= 0)
        if (str_startTime.length() == 0 || !str_startTime.chars().allMatch(Character::isDigit) || Integer.parseInt(str_startTime) < 0) {
            JOptionPane.showMessageDialog(parent_frame, "Please enter valid Server start time!", "WARNING", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // validate textField:UpTime (it should be a number, should be > 0)
        if (str_upTime.length() == 0 || !str_upTime.chars().allMatch(Character::isDigit) || Integer.parseInt(str_upTime) <= 0) {
            JOptionPane.showMessageDialog(parent_frame,"Please enter valid Server up time.", "WARNING", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // validate textField:MTU (it should be a number, and it should be > 0)
        if (str_mtu.length() == 0 || !str_mtu.chars().allMatch(Character::isDigit) || Integer.parseInt(str_mtu) <= 0) {
            JOptionPane.showMessageDialog(parent_frame, "Please enter valid MTU!", "WARNING", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // validate textField:Interval (it should be a number , it should be > 0)
        if (str_interval.length() == 0 || !str_interval.chars().allMatch(Character::isDigit) || Integer.parseInt(str_interval) <= 0) {
            JOptionPane.showMessageDialog(parent_frame, "Please enter valid Interval!", "WARNING", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // validate textField:Packets (it should be a number, it should be > 0)
        if (str_packets.length() == 0 || !str_packets.chars().allMatch(Character::isDigit) || Integer.parseInt(str_packets) <= 0) {
            JOptionPane.showMessageDialog(parent_frame, "Please enter valid no. of packets!", "WARNING", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // you can add parameters over here...
        param.add(str_serverIndex);
        param.add(str_portNo);
        param.add(str_startTime);
        param.add(str_upTime);
        param.add(str_mtu);
        param.add(str_interval);
        param.add(str_packets);
        return true;
    }
    // validation of server configuration ends here ====================================================================

    // added this section on 10/12/2023
    // validation of client configuration starts here ==================================================================
    public boolean validateClientConfig(String str_clientIndex, String str_startTime, String str_upTime) {

        // validate comboBox:ServerIndex (it should be number)
        if (str_clientIndex.length() == 0 || !str_clientIndex.chars().allMatch(Character::isDigit)) {
            JOptionPane.showMessageDialog(parent_frame,"Please select valid clientIndex!", "WARNING", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // validate textField:StartTime (it should be number, should be >= 0)
        if (str_startTime.length() == 0 || !str_startTime.chars().allMatch(Character::isDigit) || Integer.parseInt(str_startTime) < 0) {
            JOptionPane.showMessageDialog(parent_frame, "Please enter valid Client start time!", "WARNING", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // validate textField:UpTime (it should be a number, should be > 0)
        if (str_upTime.length() == 0 || !str_upTime.chars().allMatch(Character::isDigit) || Integer.parseInt(str_upTime) <= 0) {
            JOptionPane.showMessageDialog(parent_frame,"Please enter valid Client up time.", "WARNING", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // you can add parameters over here...
        param.add(str_clientIndex);
        param.add(str_startTime);
        param.add(str_upTime);
        return true;
    }
    // validation of client configuration ends here ====================================================================
}
