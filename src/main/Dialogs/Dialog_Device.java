package Dialogs;

import javax.swing.*;
import java.awt.*;

/**
 * Program name: Dialog_Device
 * Program date: 13th November 2023
 * Program owner: Henil Mistry
 * Contributor:
 * Last Modified: 07th December 2023
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

    public Dialog_Device(String LblDevice) {
        // initializing this component...
        this.lbl_info.setText(LblDevice);

        this.setContentPane(this.JPanel_main);
        this.setTitle("Configure Device");
        this.setSize(400,200);
        this.setVisible(true);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    }

    /*
    // Completed on : 07th Dec. 2023
    // This is the example of Constructor for this dialog...
    public static void main(String[] args) {
        new Dialog_Device("Device configuration for node 0 and 1");
    }
    */
}
