package Dialogs;

import Helpers.DeviceHelper;
import Helpers.LinkHelper;
import Helpers.NetworkHelper;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

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
    public ArrayList<LinkHelper> links;
    public ArrayList<NetworkHelper> networks;
    public ArrayList<DeviceHelper> devices;

    Image img;
    {
        try {
            img = ImageIO.read(getClass().getClassLoader().getResource("info.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Dialog_Connection() {
        this(0,0,new ArrayList<LinkHelper>(), new ArrayList<NetworkHelper>());
    }

    public Dialog_Connection(int a, int b, ArrayList<LinkHelper> l, ArrayList<NetworkHelper> n) {
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

        this.lbl_deviceInfo.setText("Connection between node "+this.nodeA+" and node "+this.nodeB);
        Image scaledImg = img.getScaledInstance(30,30,Image.SCALE_SMOOTH);
        ImageIcon infoIcon = new ImageIcon(scaledImg);
        this.lbl_deviceInfo.setIcon(infoIcon);

        // action to perform when clicking on configure connection button...
        btn_configureConnection.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                devices.add(new DeviceHelper(links.get(comboBox_links.getSelectedIndex()), networks.get(comboBox_networks.getSelectedIndex()), String.valueOf(nodeA), String.valueOf(nodeB)));
                for (DeviceHelper d : devices) {
                    System.out.println(d);
                }
                setVisible(false);
            }
        });
    }

    @Override
    public void setVisible(boolean b) {
        super.setVisible(b);
        if (b) {
            this.lbl_deviceInfo.setText("Connection between node "+this.nodeA+" and node "+this.nodeB);
            this.comboBox_links.removeAllItems();
            this.comboBox_networks.removeAllItems();
            for (LinkHelper link : this.links) {
                this.comboBox_links.addItem(link);
            }
            for (NetworkHelper n : this.networks) {
                this.comboBox_networks.addItem(n);
            }
        }
    }

    public void showDialog(ArrayList<LinkHelper> l, ArrayList<NetworkHelper> n, int a, int b) {
        this.nodeA = a;
        this.nodeB = b;
        this.setLinks(l);
        this.setNetworks(n);
        this.setVisible(true);
    }

    public void setLinks(ArrayList<LinkHelper> links) {
        this.links = links;
    }

    public void setNetworks(ArrayList<NetworkHelper> networks) {
        this.networks = networks;
    }
}
