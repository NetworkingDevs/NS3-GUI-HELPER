import Dialogs.Dialog_Link;
import Dialogs.Dialog_Network;
import Dialogs.Dialog_Topology;
import FileHandler.Writer;
import Helpers.DeviceHelper;
import Helpers.LinkHelper;
import Helpers.ValidationHelper;
import StatusHelper.TopologyStatus;

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
 * Last Modified: 10th December 2023
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
    Dialog_Topology dialog_topology;
    ValidationHelper validator;// added this on 10/12/2023 for validation...
    TopologyStatus topologyStatus;

    public Topology_Ring(String path, TopologyStatus topologyStatus) {
        this.topologyStatus = topologyStatus;
        this.validator = new ValidationHelper(this, this.topologyStatus);

        this.OutputPath = path;
        this.setContentPane(this.JPanel_main);
        if (this.topologyStatus == TopologyStatus.TOPOLOGY_RING) {
            this.setTitle("Topology Helper - Ring");
        } else if (this.topologyStatus == TopologyStatus.TOPOLOGY_STAR) {
            this.setTitle("Topology Helper - Star");
        } else { // expected mesh topology...
            this.setTitle("Topology Helper - Mesh");
        }
        this.setSize(500,650);
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

        // added this on 10/12/2023
        // this is an event when, clicking on generate code button...
        btn_Go.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performValidation();
            }
        });

        // change made on : 11/12/2023
        // Removed these function because they were causing the unexpected result while adding items to comboBoxes...
        /*
        comboBox_serverIndex.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                makeClientIndices();
            }
        });
        comboBox_clientIndex.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                makeServerIndices();
            }
        });
        */
    }

    public Topology_Ring(String path) {
        this(path,TopologyStatus.TOPOLOGY_RING);
    }

    // changed made on 11/12/2023
    // Change : Removed This Code Because It Was Not Required Any More...
    /*
    private void makeServerIndices() {
        int nodes = this.comboBox_clientIndex.getItemCount();
        int client_node = this.comboBox_clientIndex.getSelectedIndex();

        this.comboBox_serverIndex.removeAllItems();
        for (int i=0; i<nodes; i++) {
            if (i != client_node) {
                this.comboBox_serverIndex.addItem(i);
            }
        }
    }

    private void makeClientIndices() {
        int nodes = this.comboBox_serverIndex.getItemCount();
        int server_node = this.comboBox_serverIndex.getSelectedIndex();

        this.comboBox_clientIndex.removeAllItems();
        for (int i=0; i<nodes; i++) {
            if (i != server_node) {
                this.comboBox_clientIndex.addItem(i);
            }
        }
    }
    */

    // added this on 10/12/2023 for validation and file generation...
    private void performValidation() {
        if (!this.validator.validateTopology(this.textField_Nodes.getText().toString(), this.dialog_topology.devices)) {
            return;
        }

        if (!this.validator.validateServerConfig(String.valueOf(this.comboBox_serverIndex.getSelectedIndex()), this.textField_PortNo.getText().toString(), this.textField_startTime_server.getText().toString(), this.textField_UpTime_server.getText().toString(), this.textField_mtu.getText().toString(), this.textField_interval.getText().toString(), this.textField_packets.getText().toString())) {
            return;
        }

        if (!this.validator.validateClientConfig(String.valueOf(this.comboBox_clientIndex.getSelectedIndex()), this.textField_startTime_client.getText().toString(), this.textField_upTime_client.getText().toString())) {
            return;
        }

        for (String param : this.validator.param) {
            this.param.add(param);
        }

        if (this.checkBox_wireshark.isSelected()) {
            param.add("Wireshark");
        }

        if (this.checkBox_netAnim.isSelected()) {
            param.add("NetAnim");
        }

        createFile();
    }

    // added this on 10/12/2023 for file generation...
    private void createFile() {
        this.writer = new Writer(this.OutputPath);

        // some variable parameters configuration ======================================================================
        String topology;
        if (this.topologyStatus == TopologyStatus.TOPOLOGY_RING) {
            topology = "Ring";
        } else if (this.topologyStatus == TopologyStatus.TOPOLOGY_STAR) {
            topology = "Star";
        } else { // mesh topology expected...
            topology = "Mesh";
        }
        String netAnimModuleString = """
                #include "ns3/netanim-module.h"
                """;
        String netanimUtilityString = """
                AnimationInterface anim(\"animation"""+topology+"""
                .xml\"); 
                """;
        String wiresharkUtilityString = """
                pointToPoint.EnablePcapAll("ring");
                """;
        int StopTimeServer = Integer.parseInt(this.param.get(3))+Integer.parseInt(this.param.get(4));
        int StopTimeClient = Integer.parseInt(this.param.get(9))+Integer.parseInt(this.param.get(10));
        if(this.param.indexOf("Wireshark") < 0) {
            wiresharkUtilityString = "";
        }
        if(this.param.indexOf("NetAnim") < 0) {
            netAnimModuleString = "";
            netanimUtilityString = "";
        }
        String nodesGrp = new String();
        String nodesGrpCode = new String();
        String linkConfigCode = new String();
        String devicesGrp = new String();
        String deviceConfigCode = new String();
        String ipConfigCode = new String();
        String primaryServerGrp = new String();

        for(DeviceHelper device : this.dialog_topology.devices) {
            nodesGrp = nodesGrp.concat(device.getNodesGroup()+",");
            if (device.nodeA.compareToIgnoreCase(this.param.get(1))==0) {
                primaryServerGrp = device.nodesGroup;
            }
        }
        nodesGrp = nodesGrp.substring(0,nodesGrp.length()-1);

        for (DeviceHelper device : this.dialog_topology.devices) {
            nodesGrpCode = nodesGrpCode.concat(device.getNodesGroupCode()+"\n");
        }

        for (LinkHelper link : this.dialog_link.links) {
            linkConfigCode = linkConfigCode.concat(link.getLinkConfigCode()+"\n");
        }

        for (DeviceHelper device : this.dialog_topology.devices) {
            devicesGrp = devicesGrp.concat(device.getDevicesGroup()+",");
        }
        devicesGrp = devicesGrp.substring(0,devicesGrp.length()-1);

        for (DeviceHelper device : this.dialog_topology.devices) {
            deviceConfigCode = deviceConfigCode.concat(device.getDeviceConfCode());
        }

        for (DeviceHelper device : this.dialog_topology.devices) {
            ipConfigCode = ipConfigCode.concat(device.getIPConfCode()+"\n");
        }
        // variable parameters configuration ends ======================================================================

        String ring = """
                #include "ns3/applications-module.h"
                #include "ns3/core-module.h"
                #include "ns3/internet-module.h"
                #include "ns3/network-module.h"
                #include "ns3/point-to-point-module.h"
                """
                + netAnimModuleString +
                """
                                
                using namespace ns3;
                                
                NS_LOG_COMPONENT_DEFINE(\""""+topology+"""
                Example\");
                                
                int
                main(int argc, char* argv[])
                {
                    CommandLine cmd(__FILE__);
                    cmd.Parse(argc, argv);
                                
                    Time::SetResolution(Time::NS);
                    LogComponentEnable("UdpEchoClientApplication", LOG_LEVEL_INFO);
                    LogComponentEnable("UdpEchoServerApplication", LOG_LEVEL_INFO);
                   \s
                    // step-1 = creating group of nodes....
                    NodeContainer allNodes,"""
                    + nodesGrp +
                    """
                    ;
                    allNodes.Create("""
                    + this.param.get(0) +
                    """
                    );
                    
                    """
                    + nodesGrpCode +
                    """
                                
                    // step-2 = create link
                    """
                    + linkConfigCode +
                    """
                   \s
                    // step-3 = creating devices
                    NetDeviceContainer   """
                    + devicesGrp +
                    """
                    ;
                    
                    """
                    + deviceConfigCode +
                    """
                                
                    // step-4 = Install ip stack
                    InternetStackHelper stack;
                    stack.Install(allNodes);
                   
                    // step-5 = Assignment of IP Address
                    Ipv4AddressHelper address;
                   
                    """
                    + ipConfigCode +
                    """
                  
                    // step-6 = server configuration
                    UdpEchoServerHelper echoServer("""
                    + this.param.get(2) +"""
                    );
                                
                    ApplicationContainer serverApps = echoServer.Install(allNodes.Get("""
                    + this.param.get(1) + """
                    ));
                    serverApps.Start(Seconds("""+ this.param.get(3)  +"""
                    .0));
                    serverApps.Stop(Seconds("""+ StopTimeServer +"""
                    .0));
                   
                    // step-7 = client configuration
                    UdpEchoClientHelper echoClient(interfaces"""+ primaryServerGrp +"""
                    .GetAddress(0),"""+ this.param.get(2) +"""
                    );
                    echoClient.SetAttribute("MaxPackets", UintegerValue("""+ this.param.get(7) +"""
                    ));
                    echoClient.SetAttribute("Interval", TimeValue(Seconds("""+ this.param.get(6) +"""
                    .0)));
                    echoClient.SetAttribute("PacketSize", UintegerValue("""+ this.param.get(5) +"""
                    ));
                                
                    ApplicationContainer clientApps = echoClient.Install(allNodes.Get("""+ this.param.get(8) +"""
                    ));
                    clientApps.Start(Seconds("""+ this.param.get(9) +"""
                    .0));
                    clientApps.Stop(Seconds("""+ StopTimeClient +"""
                    .0));
                   
                    Ipv4GlobalRoutingHelper::PopulateRoutingTables();
                    """
                    + netanimUtilityString + "\n" + wiresharkUtilityString +
                    """
                   
                                
                    Simulator::Run();
                    Simulator::Destroy();
                    return 0;
                }
                """;

        this.writer.writeToFile(ring);
        this.writer.closeTheFile();
        this.param = new ArrayList<>();

        JOptionPane.showMessageDialog(this, "File has been generated successfully!", "Code Generated!", JOptionPane.INFORMATION_MESSAGE);
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

        // If it contains the already configured device, then ask for reassignment (to the user...)
        if (this.dialog_topology != null) {
            if (this.dialog_topology.devices.size() == Integer.parseInt(nodes)) {
                int option = JOptionPane.showConfirmDialog(this,"Topology has already configured!\nAre You sure you want to reassign the configuration?","Redo Configuration!",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                if(option == JOptionPane.YES_OPTION) {
                    this.adjustNodesAndShowDialogTopology(nodes);
                }
            } else {
                System.out.println("Just Checking!!");
                this.dialog_topology.setVisible(true);
            }
        } else {
            // if all checks pass...
            this.adjustNodesAndShowDialogTopology(nodes);
        }
    }

    // added this on 11/12/2023
    // For modularity, nothing much...
    private void adjustNodesAndShowDialogTopology(String nodes) {
        this.comboBox_serverIndex.removeAllItems();
        this.addNodesToServerIndex(Integer.parseInt(nodes));
        this.dialog_topology = new Dialog_Topology(Integer.parseInt(nodes),this.dialog_link.links, this.dialog_network.links, this.topologyStatus);
    }

    private void addNodesToServerIndex(int n) {
        for (int i=0; i<n; i++) {
            this.comboBox_serverIndex.addItem("Node : "+String.valueOf(i));
        }
        this.comboBox_clientIndex.removeAllItems();
        this.addNodesToClientIndex(n);
    }

    private void addNodesToClientIndex(int n) {
        for (int i=0; i<n; i++) {
            this.comboBox_clientIndex.addItem("Node : "+String.valueOf(i));
        }
    }

    public void showDialogLink() {
        this.dialog_link.setVisible(true);
    }

    public void showDialogNetwork() {
        this.dialog_network.setVisible(true);
    }
}
