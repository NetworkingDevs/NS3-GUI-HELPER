package Dialogs;

import Helpers.DeviceHelper;
import Helpers.LinkHelper;
import Helpers.NetworkHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Program name: Dialog_Device
 * Program date: 13th November 2023
 * Program owner: Henil Mistry
 * Contributor:
 * Last Modified: 08th December 2023
 *
 * Purpose: This class helps for setting up the 'Device' (in terms of NS3)
 * */
public class Dialog_Device extends JFrame {
    private JPanel JPanel_main;
    private JLabel lbl_info;
    private JPanel JPanel_config;
    private JComboBox comboBox_link_config;
    private JLabel lbl_selectLink;
    private JLabel lbl_selectNetwork;
    private JComboBox comboBox_net_config;
    private JButton btn_configDevice;

    // Functionalities Required Below Things...
    ArrayList<LinkHelper> links;
    ArrayList<NetworkHelper> networks;
    Dialog_Topology parent;
    String nodeA,nodeB;

    // change : Changed the constructor for not compromising the functionalities served by this Dialog...
    // changed made on : 08th Dec., 2023
    public Dialog_Device(String a, String b, String LblDevice, ArrayList<LinkHelper> l, ArrayList<NetworkHelper> n, Dialog_Topology parent) {
        // initializing this component...
        this.lbl_info.setText(LblDevice);
        this.nodeA = a;
        this.nodeB = b;

        // init. the lists and other things...
        this.links = new ArrayList<>();
        this.links.addAll(l);
        this.networks = new ArrayList<>();
        this.networks.addAll(n);
        this.parent = parent;

        // init. the combo boxes...
        for (LinkHelper link : this.links) {
            this.comboBox_link_config.addItem(link);
        }
        for (NetworkHelper network : this.networks) {
            this.comboBox_net_config.addItem(network);
        }

        // initializing the panel and making it visible...
        this.setContentPane(this.JPanel_main);
        this.setTitle("Configure Device");
        this.setSize(400,200);
        this.setVisible(true);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        btn_configDevice.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addDeviceToParent();
            }
        });
    }

    // added : Added this function for adding the Device in Device_Topology (aka parent of this class's object)
    // added on : 08th Dec., 2023
    private void addDeviceToParent() {
        String nodeGrp = this.nodeA + this.nodeB;
        boolean collision = false;

        for (int i=0; i<this.parent.devices.size(); i++) {
            if(this.parent.devices.get(i).nodesGroup.compareToIgnoreCase(nodeGrp) == 0) {
                this.parent.devices.add(new DeviceHelper(this.links.get(this.comboBox_link_config.getSelectedIndex()),this.networks.get(this.comboBox_net_config.getSelectedIndex()), nodeGrp));
                this.parent.devices.remove(i);
                collision = true;
            }
        }

        if (!collision) {
            this.parent.devices.add(new DeviceHelper(this.links.get(this.comboBox_link_config.getSelectedIndex()),this.networks.get(this.comboBox_net_config.getSelectedIndex()), nodeGrp));
        }
        JOptionPane.showMessageDialog(this,"Device has been configured!", "Device Config.", JOptionPane.INFORMATION_MESSAGE);
        this.setVisible(false);
    }

    /*
    // Completed on : 07th Dec. 2023
    // This is the example of Constructor for this dialog...
    public static void main(String[] args) {
        // new Dialog_Device(0,1,"Device configuration for node 0 and 1",<links>,<networks>);
    }
    */
}
