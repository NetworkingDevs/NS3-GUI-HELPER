import Dialogs.*;
import FileHandler.Writer;
import GuiHelpers.NodeHelper;
import GuiHelpers.P2PLinkHelper;
import GuiHelpers.TopologyPainter;
import Helpers.DeviceHelper;
import Helpers.LinkHelper;
import StatusHelper.ToolStatus;
import StatusHelper.TopologyStatus;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;

public class Topology_Custom extends JFrame {
    private JPanel JPanel_main;
    private JPanel JPanel_left;
    private JPanel JPanel_right;
    private JLabel lbl_info;
    private JButton btn_generateCode;
    private JScrollPane JScrollPane_right;
    private JPanel JPanel_configurationWindow;
    private JScrollPane JScrollPane_canvasContainer;
    private JPanel JPanel_tools;
    private JButton btn_node;
    private JButton btn_link;
    private JPanel JPanel_topologyConfiguration;
    private JPanel JPanel_utilityConfiguration;
    private JCheckBox chkBox_wireshark;
    private JCheckBox chkBox_netanim;
    private JPanel JPanel_grpLink;
    private JLabel lbl_links;
    private JButton btn_addLinks;
    private JComboBox comboBox_links;
    private JPanel JPanel_grpNetwork;
    private JLabel lbl_network;
    private JButton btn_addNetwork;
    private JComboBox comboBox_networks;
    private JButton btn_configServer;
    private JButton btn_configClient;

    // for serving the functionalities...
    TopologyPainter painter = new TopologyPainter(new ArrayList<NodeHelper>(), new ArrayList<P2PLinkHelper>(), 400, 400);
    ToolStatus toolStatus;
    private int clicks = 1;
    private int firstNode = -1;
    Dialog_Link dialogLink;
    Dialog_Network dialogNetwork;
    Dialog_Connection dialogConnection;
    Dialog_ConfigureServer dialogConfigureServer;
    Dialog_ConfigureClient dialogConfigureClient;
    String OutputPath;
    Writer writer;
    Image img;

    {
        try {
            img = ImageIO.read(getClass().getClassLoader().getResource("info.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Topology_Custom(String path) {
        this.OutputPath = path;
        this.writer = new Writer(this.OutputPath);

        // basic configuration of this frame...
        this.setContentPane(this.JPanel_main);
        this.setTitle("Topology Helper - Custom Topology");
        this.setSize(800,500);
        this.setVisible(true);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        this.dialogLink = new Dialog_Link(this.comboBox_links);
        this.dialogLink.setVisible(false);
        this.dialogNetwork = new Dialog_Network(this.comboBox_networks);
        this.dialogNetwork.setVisible(false);
        this.dialogConnection = new Dialog_Connection();
        this.dialogConnection.setVisible(false);
        this.dialogConfigureServer = new Dialog_ConfigureServer(0);
        this.dialogConfigureServer.setVisible(false);
        this.dialogConfigureClient = new Dialog_ConfigureClient(0);
        this.dialogConfigureClient.setVisible(false);

        this.JScrollPane_canvasContainer.setViewportView(this.painter);
        this.JScrollPane_right.setViewportView(this.JPanel_configurationWindow);

        // record each mouse click on canvas...
        // do accordingly...
        this.painter.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                System.out.println("Clicked at x : "+e.getX()+" y : "+e.getY());
                // if tool is 'node' tool then...
                if (toolStatus == ToolStatus.TOOL_NODE) {
                    // create a new node at given point...
                    NodeHelper node = new NodeHelper(e.getX()-10, e.getY()-10, 20,String.valueOf(painter.getNodes().size()));
                    // add that node to painter for painting on canvas...
                    painter.addAndPrintNode(node);
                }
                // if tool is 'link' tool then...
                else if (toolStatus == ToolStatus.TOOL_LINK) {
                    int collision = -1;
                    boolean successfulClick = false;
                    // for first successful click....
                    // check if collision with any node...
                    collision = painter.pointCollideWithAny(e.getX(), e.getY());
                    // System.out.println("Collision : "+collision+" Clicks : "+clicks); // just for testing...
                    if (collision >= 0 && clicks%2!=0) {
                        // if collision then change the information label and increment the no. of clicks...
                        firstNode = collision;
                        lbl_info.setText("Connection Tool Selected: First node selected now, select second node.");
                        successfulClick = true;
                    }

                    // for second successful click...
                    // check if collision with any node...
                    if (collision >= 0 && clicks%2==0) {
                        // if collision then change the information label, increment the no. of clicks and open a device configuration dialog box...
                        lbl_info.setText("Connection Tool Selected: To create a link, click on two nodes sequentially.");
                        painter.addAndPrintLink(new P2PLinkHelper(painter.getNodes().get(firstNode), painter.getNodes().get(collision)));
                        successfulClick = true;
                        System.out.println(firstNode+" "+collision);
                        dialogConnection.showDialog(dialogLink.links,dialogNetwork.links,firstNode,collision);
                        firstNode = -1;
                    }
                    incrementClicks(successfulClick);
                }
            }
        });

        Image scaledImg = img.getScaledInstance(30,30,Image.SCALE_SMOOTH);
        ImageIcon infoIcon = new ImageIcon(scaledImg);
        lbl_info.setIcon(infoIcon);

        // action to perform when node tool is selected...
        btn_node.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lbl_info.setText("Node Tool Selected: Click anywhere on the canvas to add a node.");
                toolStatus = ToolStatus.TOOL_NODE;
            }
        });
        // action to be performed when link tool is selected...
        btn_link.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // check condition if there exists at least one link and warn if not present...(pending)
                if (checkLinkTool()) {
                    lbl_info.setText("Connection Tool Selected: To create a link, click on two nodes sequentially.");
                    toolStatus = ToolStatus.TOOL_LINK;
                }
            }
        });


        // action to be performed when Add Link button is pressed...
        btn_addLinks.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialogLink.setVisible(true);
            }
        });
        // action to be performed when Add Network button is pressed...
        btn_addNetwork.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialogNetwork.setVisible(true);
            }
        });


        // action to be performed when clicking on configure server button...
        btn_configServer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (checkNodes()) {
                    dialogConfigureServer.showDialog(painter.getNodes().size());
                }
            }
        });
        // action to be performed when clicking on configure client button...
        btn_configClient.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (checkNodes()) {
                    if (dialogConfigureServer.settings.size() == 0) { // server configuration has not been done yet...
                        // show warning message...
                        showServerConfigWarning();
                    } else { // otherwise...
                        // show client configuration dialog box...
                        dialogConfigureClient.showDialog(painter.getNodes().size());
                    }
                }
            }
        });

        // action to be performed when clicking on generate code button...
        btn_generateCode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (checkNodes("Please create your topology first!")) {
                    createFile();
                }
            }
        });
    }

    private void showServerConfigWarning() {
        JOptionPane.showMessageDialog(this, "Please configure the server first!", "Warning!", JOptionPane.WARNING_MESSAGE);
    }

    private boolean checkLinkTool() {
        if (this.painter.getNodes().size() == 0) {
            JOptionPane.showMessageDialog(this, "Please add some nodes to make a connection!", "Warning", JOptionPane.WARNING_MESSAGE);
            return false;
        } else if (dialogLink.links.size() <= 1) {
            JOptionPane.showMessageDialog(this, "Please create at least one link before making a connection!", "Warning", JOptionPane.WARNING_MESSAGE);
            return false;
        } else if (dialogNetwork.links.size() <= 1) {
            JOptionPane.showMessageDialog(this, "Please create at least one network before making a connection!", "Warning", JOptionPane.WARNING_MESSAGE);
            return false;
        } else {
            return true;
        }
    }

    private void incrementClicks(boolean successfulClick) {
        if (successfulClick) {
            clicks ++;
        }
    }

    private boolean checkNodes(String msg) {
        if (painter.getNodes().size() == 0) {
            JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private boolean checkNodes() {
        return this.checkNodes("Please add some nodes first!");
    }

    private void createFile() {
        this.writer = new Writer(this.OutputPath);

        // some variable parameters configuration ======================================================================
        String topology = "Custom";

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
        int StopTimeServer = this.dialogConfigureServer.getStopTime();
        int StopTimeClient = this.dialogConfigureClient.getStopTime();
        if(!this.chkBox_wireshark.isSelected()) {
            wiresharkUtilityString = "";
        }
        if(!this.chkBox_netanim.isSelected()) {
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
        String serverPrimaryIndex = new String();
        boolean serverPrimaryConfigured = false;

        for(DeviceHelper device : this.dialogConnection.devices) {
            nodesGrp = nodesGrp.concat(device.getNodesGroup()+",");
            if (!serverPrimaryConfigured) {
                if (device.nodeA.compareToIgnoreCase(this.dialogConfigureServer.getServerIndex())==0) {
                    primaryServerGrp = device.nodesGroup;
                    serverPrimaryIndex = "0";
                    serverPrimaryConfigured = true;
                } else if (device.nodeB.compareToIgnoreCase(this.dialogConfigureServer.getServerIndex()) == 0) {
                    primaryServerGrp = device.nodesGroup;
                    serverPrimaryIndex = "1";
                    serverPrimaryConfigured = true;
                }
                // System.out.println("Primary Index : "+serverPrimaryIndex+" Primary Group : "+primaryServerGrp);
            }
        }
        nodesGrp = nodesGrp.substring(0,nodesGrp.length()-1);

        for (DeviceHelper device : this.dialogConnection.devices) {
            nodesGrpCode = nodesGrpCode.concat(device.getNodesGroupCode()+"\n");
        }

        for (LinkHelper link : this.dialogLink.links) {
            linkConfigCode = linkConfigCode.concat(link.getLinkConfigCode()+"\n");
        }

        for (DeviceHelper device : this.dialogConnection.devices) {
            devicesGrp = devicesGrp.concat(device.getDevicesGroup()+",");
        }
        devicesGrp = devicesGrp.substring(0,devicesGrp.length()-1);

        for (DeviceHelper device : this.dialogConnection.devices) {
            deviceConfigCode = deviceConfigCode.concat(device.getDeviceConfCode());
        }

        for (DeviceHelper device : this.dialogConnection.devices) {
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
                + this.painter.getNodes().size() +
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
                + this.dialogConfigureServer.getPortNumber() +"""
                    );
                                
                    ApplicationContainer serverApps = echoServer.Install(allNodes.Get("""
                + this.dialogConfigureServer.getServerIndex() + """
                    ));
                    serverApps.Start(Seconds("""+ this.dialogConfigureServer.getStartTime()  +"""
                    .0));
                    serverApps.Stop(Seconds("""+ StopTimeServer +"""
                    .0));
                   
                    // step-7 = client configuration
                    UdpEchoClientHelper echoClient(interfaces"""+ primaryServerGrp +"""
                    .GetAddress("""+ serverPrimaryIndex + """
                ),"""+ this.dialogConfigureServer.getPortNumber() +"""
                    );
                    echoClient.SetAttribute("MaxPackets", UintegerValue("""+ this.dialogConfigureClient.getPackets() +"""
                    ));
                    echoClient.SetAttribute("Interval", TimeValue(Seconds("""+ this.dialogConfigureClient.getInterval() +"""
                    .0)));
                    echoClient.SetAttribute("PacketSize", UintegerValue("""+ this.dialogConfigureClient.getMTU() +"""
                    ));
                                
                    ApplicationContainer clientApps = echoClient.Install(allNodes.Get("""+ this.dialogConfigureClient.getClientIndex() +"""
                    ));
                    clientApps.Start(Seconds("""+ this.dialogConfigureClient.getStartTime() +"""
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

        JOptionPane.showMessageDialog(this, "File has been generated successfully!\nAt : "+this.OutputPath, "Code Generated!", JOptionPane.INFORMATION_MESSAGE);
    }

}
