package Dialogs;

import Helpers.LoggingHelper;
import Ns3Objects.Devices.Device;
import Ns3Objects.Links.NetworkLink;
import Ns3Objects.Netoworks.Network;
import StatusHelper.LinkType;
import StatusHelper.ToolStatus;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * to configure the connections
 * */
public class Dialog_Connection extends JFrame {
    private JPanel JPanel_main;
    private JLabel lbl_deviceInfo;
    private JPanel JPanel_group;
    private JLabel lbl_link;
    private JComboBox comboBox_links;
    private JLabel lbl_network;
    private JComboBox comboBox_networks;
    private JButton btn_configureConnection;

    // for serving the functionality....
    /**
     * the index of first two nodes
     * */
    public int nodeA, nodeB;
    /**
     * list of nodes in connection
     * */
    public ArrayList<Integer> nodes;
    /**
     * list of link settings
     * */
    public ArrayList<NetworkLink> links;
    /**
     * list of network settings
     * */
    public ArrayList<Network> networks;
    /**
     * list of devices
     * */
    public ArrayList<Device> devices;
    /**
     * list of devices containing CSMA devices
     * */
    public ArrayList<Device> devices_csma;

    /**
     * the info image icon
     * */
    Image img;
    {
        try {
            img = ImageIO.read(getClass().getClassLoader().getResource("info.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * the instance of this class
     * */
    private static Dialog_Connection INSTANCE;
    /**
     * the link configuration dialog
     * */
    private Dialog_Link dialogLink;
    /**
     * the link configuration dialog for wi-fi links
     * */
    private Dialog_WiFiLink dialogWiFiLink;

    /**
     * to get the instance of this class
     *
     * @return the instance of this class
     * @since 0.3.0
     * */
    public static Dialog_Connection getInstance() {
        LoggingHelper.LogInfo("Checking for available instance of Dialog_Connection!");
        if (INSTANCE == null) {
            LoggingHelper.LogDebug("The instance for Dialog_Connection was not available!");
            INSTANCE = new Dialog_Connection();
        }
        return INSTANCE;
    }

    /**
     * to create the object of type Dialog_Connection
     *
     * @see Dialog_Connection#Dialog_Connection(int, int, ArrayList, ArrayList)
     * @since 0.3.0
     * */
    public Dialog_Connection() {
        this(0,0,new ArrayList<NetworkLink>(), new ArrayList<Network>());
    }

    /**
     * to create the object of type this class
     *
     * @param a index of first node
     * @param b index of second mode
     * @param l list of links settings
     * @param n list of network settings
     * @see Dialog_Connection#Dialog_Connection()
     * @since 0.3.0
     * */
    public Dialog_Connection(int a, int b, ArrayList<NetworkLink> l, ArrayList<Network> n) {
        LoggingHelper.Log("Creating object of type Dialog_connection");
        // initializing this component....
        this.setContentPane(this.JPanel_main);
        this.setTitle("Configure Connection");
        this.setSize(400,200);
        this.setVisible(false);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        this.nodeA = a;
        this.nodeB = b;
        this.links = new ArrayList<>();
        this.links.addAll(l);
        this.networks = new ArrayList<>();
        this.networks.addAll(n);
        this.devices = new ArrayList<>();
        this.devices_csma = new ArrayList<>();

        this.lbl_deviceInfo.setText("Connection between node "+this.nodeA+" and node "+this.nodeB);
        Image scaledImg = img.getScaledInstance(30,30,Image.SCALE_SMOOTH);
        ImageIcon infoIcon = new ImageIcon(scaledImg);
        this.lbl_deviceInfo.setIcon(infoIcon);

        // action to perform when clicking on configure connection button...
        btn_configureConnection.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoggingHelper.LogFunction("Dialog Connection : Adding a connection!");
                int index = -1;
                for (int i=0; i<links.size(); i++) {
                    if (links.get(i).toString().equals(comboBox_links.getSelectedItem().toString())) {
                        index = i;
                    }
                }
                LoggingHelper.LogLogic("Dialog Connection : The link index : "+index);
                setAsUsedLink(links.get(index));
                LoggingHelper.LogDebug("Matched link : "+links.get(index).toString());
                if (links.get(index).getLinkType()==LinkType.LINK_CSMA) {
                    LoggingHelper.LogDebug("Adding CSMA devices...");
                    devices_csma.add(new Device(links.get(index), networks.get(comboBox_networks.getSelectedIndex()), nodes, devices_csma.size()));
                } else if (links.get(index).getLinkType() == LinkType.LINK_WIFI) {
                    LoggingHelper.LogDebug("Adding Wi-Fi devices...");
                    devices_csma.add(new Device(links.get(index), networks.get(comboBox_networks.getSelectedIndex()), nodes, devices_csma.size()));
                } else { // assumed to be point to point...
                    LoggingHelper.LogDebug("Adding Point to point devices...");
                    devices.add(
                            new Device
                                    (
                                            links.get
                                                    (
                                                            index
                                                    ),
                                            networks.get(comboBox_networks.getSelectedIndex()),
                                            String.valueOf(nodeA), String.valueOf(nodeB)
                                    )
                    );
                }
                for (Device d : devices) {
                    LoggingHelper.LogDebug(d.toString());
                }
                setVisible(false);
            }
        });
    }

    /**
     * to make the link as used link
     *
     * @param link the link settings
     * @since 1.1.0
     * */
    private void setAsUsedLink(NetworkLink link) {
        LoggingHelper.LogFunction("Dialog Connection : Setting link as used link : "+link.toString());
        boolean marked = false;
        LoggingHelper.LogLogic("Dialog Connection : Looking for point-to-point and CSMA links!");
        for(NetworkLink l : dialogLink.links) {
            if (link.toString().equals(l.toString())) {
                LoggingHelper.LogLogic("Dialog Connection : Match found!");
                l.setUsed(true);
                marked = true;
            }
        }
        if (!marked) {
            LoggingHelper.LogLogic("Dialog Connection : No match found! It is a wi-f- link.");
            for(NetworkLink l : dialogWiFiLink.links) {
                if (link.toString().equals(l.toString())) {
                    l.setUsed(true);
                }
            }
        }
    }

    /**
     * to add dialog for link configuration
     *
     * @param dialogLink the Dialog_Link's object
     * */
    public void addDialogLink(Dialog_Link dialogLink) {
        LoggingHelper.LogFunction("Dialog Connection : Adding an object of Dialog_Link");
        this.dialogLink = dialogLink;
    }

    /**
     * to add dialog for link configuration
     *
     * @param dialogLink the Dialog_WifiLink's object
     * */
    public void addDialogLink(Dialog_WiFiLink dialogLink) {
        LoggingHelper.LogFunction("Dialog Connection : Adding an object of Dialog_WiFiLink");
        this.dialogWiFiLink = dialogLink;
    }

    /**
     * to make this dialog visible again
     *
     * @param b the value indicating whether to show oe hide
     * @param selectedTool the selected tool while this dialog is being visible, have no affect when b is false
     * @since 0.3.0
     * */
    private void setVisible(boolean b, ToolStatus selectedTool) {
        LoggingHelper.LogFunction("Dialog Connection : changing visibility!");
        super.setVisible(b);
        if (b) {
            LinkType type;
            if (selectedTool == ToolStatus.TOOL_LINK) {
                this.lbl_deviceInfo.setText("Connection between node "+this.nodeA+" and node "+this.nodeB);
                type = LinkType.LINK_P2P;
            } else if (selectedTool == ToolStatus.TOOL_LINK_WIFI) {
                type = LinkType.LINK_WIFI;
                String list_nodes = this.nodes.stream().map(Object::toString).collect(Collectors.joining(", "));
                this.lbl_deviceInfo.setText("Wi-Fi Connection between : "+list_nodes);
            }
            else { // assumed to be CSMA...
                type = LinkType.LINK_CSMA;
                String list_nodes = this.nodes.stream().map(Object::toString).collect(Collectors.joining(", "));
                this.lbl_deviceInfo.setText("CSMA Connection between nodes : "+list_nodes);
            }
            this.comboBox_links.removeAllItems();
            this.comboBox_networks.removeAllItems();
            for (NetworkLink link : this.links) {
                LoggingHelper.LogInfo("Type : "+link.getLinkType());
                if (type == link.getLinkType()) {
                    LoggingHelper.LogInfo("Matched!");
                    this.comboBox_links.addItem(link);
                }
            }
            for (Network n : this.networks) {
                this.comboBox_networks.addItem(n);
            }
        }
    }

    /**
     * it will show or hide the dialog
     *
     * @param l list of links
     * @param n list of network settings
     * @param nodes list of nodes
     * @param selectedTool the selected tool
     * @since 1.1.0
     * */
    public void showDialog(ArrayList<NetworkLink> l, ArrayList<Network> n,ArrayList<Integer> nodes, ToolStatus selectedTool) {
        LoggingHelper.LogFunction("Dialog Connection : showDialog is called!");
        if (selectedTool == ToolStatus.TOOL_LINK) {
            this.nodeA = nodes.get(0);
            this.nodeB = nodes.get(1);
        } else {
            this.nodes = new ArrayList<>();
            this.nodes.addAll(nodes);
        }
        this.setLinks(l);
        this.setNetworks(n);
        this.setVisible(true, selectedTool);
    }

    private void setLinks(ArrayList<NetworkLink> links) {
        this.links = links;
    }

    private void setNetworks(ArrayList<Network> networks) {
        this.networks = networks;
    }
}
