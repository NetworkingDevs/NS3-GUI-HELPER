package Dialogs;

import Helpers.DebuggingHelper;
import Ns3Objects.Devices.Device;
import Ns3Objects.Links.NetworkLink;
import Ns3Objects.Netowkrs.Network;
import StatusHelper.LinkType;
import StatusHelper.ToolStatus;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

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
    public int nodeA, nodeB;
    public ArrayList<Integer> nodes;
    public ArrayList<NetworkLink> links;
    public ArrayList<Network> networks;
    public ArrayList<Device> devices;
    public ArrayList<Device> devices_csma;

    Image img;
    {
        try {
            img = ImageIO.read(getClass().getClassLoader().getResource("info.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Dialog_Connection INSTANCE;
    private Dialog_Link dialogLink;

    public static Dialog_Connection getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Dialog_Connection();
        }
        return INSTANCE;
    }

    public Dialog_Connection() {
        this(0,0,new ArrayList<NetworkLink>(), new ArrayList<Network>());
    }

    public Dialog_Connection(int a, int b, ArrayList<NetworkLink> l, ArrayList<Network> n) {
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
                int index = -1;
                for (int i=0; i<links.size(); i++) {
                    if (links.get(i).toString().equals(comboBox_links.getSelectedItem().toString())) {
                        index = i;
                    }
                }
                setAsUsedLink(links.get(index));
                DebuggingHelper.Debugln("Matched link : "+links.get(index).toString());
                if (links.get(index).getLinkType()==LinkType.LINK_CSMA) {
                    DebuggingHelper.Debugln("Adding CSMA devices...");
                    devices_csma.add(new Device(links.get(index), networks.get(comboBox_networks.getSelectedIndex()), nodes, devices_csma.size()));
                } else { // assumed to be point to point...
                    DebuggingHelper.Debugln("Adding Point to point devices...");
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
                    DebuggingHelper.Debugln(d.toString());
                }
                setVisible(false);
            }
        });
    }

    private void setAsUsedLink(NetworkLink link) {
        for(NetworkLink l : dialogLink.links) {
            if (link.toString().equals(l.toString())) {
                l.setUsed(true);
            }
        }
    }

    public void addDialogLink(Dialog_Link dialogLink) {
        this.dialogLink = dialogLink;
    }

    public void setVisible(boolean b, ToolStatus selectedTool) {
        super.setVisible(b);
        if (b) {
            LinkType type;
            if (selectedTool == ToolStatus.TOOL_LINK) {
                this.lbl_deviceInfo.setText("Connection between node "+this.nodeA+" and node "+this.nodeB);
                type = LinkType.LINK_P2P;
            } else { // assumed to be CSMA...
                type = LinkType.LINK_CSMA;
                String list_nodes = this.nodes.stream().map(Object::toString).collect(Collectors.joining(", "));
                this.lbl_deviceInfo.setText("CSMA Connection between nodes : "+list_nodes);
            }
            this.comboBox_links.removeAllItems();
            this.comboBox_networks.removeAllItems();
            for (NetworkLink link : this.links) {
                if (type == link.getLinkType()) {
                    this.comboBox_links.addItem(link);
                }
            }
            for (Network n : this.networks) {
                this.comboBox_networks.addItem(n);
            }
        }
    }

    public void showDialog(ArrayList<NetworkLink> l, ArrayList<Network> n,ArrayList<Integer> nodes, ToolStatus selectedTool) {
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

    public void setLinks(ArrayList<NetworkLink> links) {
        this.links = links;
    }

    public void setNetworks(ArrayList<Network> networks) {
        this.networks = networks;
    }
}
