package Dialogs;

import javax.swing.*;
import java.awt.*;

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

    public static void main(String[] args) {
        new Dialog_Device("Device configuration for node 0 and 1");
    }
}
