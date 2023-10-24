import FileHandler.Writer;

import javax.swing.*;
import java.util.ArrayList;

public class Topology_Ring extends JFrame {
    private JPanel JPanel_main;
    private JPanel JPanel_ConfigTopology;
    private JLabel lbl_nodes;
    private JTextField textField_Nodes;
    private JLabel lbl_links;
    private JButton btn_addLink;
    private JScrollPane JScrollPanel_Links;
    private JPanel JPanel_links_main;
    private JLabel lbl_default;
    private JButton btn_edit_0;
    private JButton btn_delete_0;
    private JLabel lbl_networks;
    private JButton btn_addNetwork;
    private JScrollPane JScrollPanel_Networks;
    private JPanel JPanel_networks_main;
    private JLabel lbl_default_network;
    private JButton btn_edit_net_0;
    private JButton btn_delete_net_0;
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

    // Functionalities Required Below Things...
    private ArrayList<String> param = new ArrayList<>();
    private String OutputPath;
    Writer writer;

    public Topology_Ring(String path) {
        this.OutputPath = path;
        this.setContentPane(this.JPanel_main);
        this.setTitle("Topology Helper - Ring");
        this.setSize(500,850);
        this.setVisible(true);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        this.writer = new Writer(this.OutputPath);
    }

}
