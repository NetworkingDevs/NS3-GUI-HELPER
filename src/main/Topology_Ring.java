import Dialogs.Dialog_Link;
import Dialogs.Dialog_Network;
import Dialogs.Dialog_Topology;
import FileHandler.Writer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Program name: Topology_Ring
 * Program date: 24th October 2023
 * Program owner: Henil Mistry
 * Contributor:
 * Last Modified: 07th December 2023
 *
 * Purpose: This class will be responsible for generating code of ring topology.
 * */
public class Topology_Ring extends JFrame {
    private JPanel JPanel_main;
    private JPanel JPanel_ConfigTopology;
    private JLabel lbl_nodes;
    private JTextField textField_Nodes;
    private JLabel lbl_links;
    private JButton btn_addLink;
    private JScrollPane JScrollPanel_Links;
    private JPanel JPanel_links_main;
    private JLabel lbl_networks;
    private JButton btn_addNetwork;
    private JScrollPane JScrollPanel_Networks;
    private JButton btn_configTopology;
    private JPanel JPanel_ConfigServer;
    private JComboBox comboBox_serverIndex;
    private JLabel lbl_serverIndex;
    private JLabel lbl_portNo;
    private JTextField textField_PortNo;
    private JLabel lbl_startTIme_server;
    private JTextField textField_startTime_server;
    private JLabel lbl_upTime_server;
    private JTextField textField_UpTime_server;
    private JLabel lbl_mtu;
    private JTextField textField_mtu;
    private JLabel lbl_interval;
    private JTextField textField_interval;
    private JLabel lbl_packets;
    private JTextField textField_packets;
    private JPanel JPanel_CofigClient;
    private JLabel lbl_clientIndex;
    private JComboBox comboBox_clientIndex;
    private JLabel lbl_startTime_client;
    private JTextField textField_startTime_client;
    private JLabel lbl_UpTime_client;
    private JTextField textField_upTime_client;
    private JPanel JPanel_ConfigUtilities;
    private JCheckBox checkBox_wireshark;
    private JCheckBox checkBox_netAnim;
    private JButton btn_Go;
    private JComboBox comboBox_links;
    private JComboBox comboBox_networks;

    // Functionalities Required Below Things...
    private ArrayList<String> param = new ArrayList<>();
    private String OutputPath;
    Writer writer;
    Dialog_Link dialog_link;
    Dialog_Network dialog_network;
    private Dialog_Topology topologyConfigDialog;

    public Topology_Ring(String path) {
        this.OutputPath = path;
        this.setContentPane(this.JPanel_main);
        this.setTitle("Topology Helper - Ring");
        this.setSize(500,800);
        this.setVisible(true);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        this.writer = new Writer(this.OutputPath);
        this.JPanel_links_main = new JPanel();
        this.JPanel_links_main.setLayout(new GridLayout(3,3));

        this.dialog_link = new Dialog_Link(this.comboBox_links);
        this.dialog_link.showDefaultLink();
        this.dialog_link.setVisible(false);

        this.dialog_network = new Dialog_Network(this.comboBox_networks);
        this.dialog_network.showDefaultNetwork();
        this.dialog_network.setVisible(false);

        // this is an event when, clicking on Add Link Button...
        btn_addLink.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showDialogLink();
            }
        });
        // this is an event when, clicking on add network Button...
        btn_addNetwork.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showDialogNetwork();
            }
        });
        // this is an event when, clicking on configure topology button...
        btn_configTopology.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showDialogTopology();
            }
        });
    }

    private void showDialogTopology() {
        String nodes = this.textField_Nodes.getText().toString();

        // validation of nodes...
        // validate textField:Nodes (it should -> not be empty / be number / be >=3 )
        if(nodes.length() == 0 || !nodes.chars().allMatch(Character::isDigit)) {
            JOptionPane.showMessageDialog(JPanel_main, "Please enter valid no. of Nodes!", "WARNING", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if(Integer.parseInt(nodes) < 3) {
            JOptionPane.showMessageDialog(JPanel_main, "Ring topology should contain at least 3 nodes.", "INFORMATION",JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // if all checks pass...
        this.topologyConfigDialog = new Dialog_Topology(Integer.parseInt(nodes));
    }

    public void showDialogLink() {
        this.dialog_link.setVisible(true);
    }

    public void showDialogNetwork() {
        this.dialog_network.setVisible(true);
    }
}
