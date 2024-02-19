package Dialogs;

import Netowkrs.Network;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Map;

public class Dialog_Network extends JFrame {
    private JPanel JPanel_main;
    private JLabel lbl_netId;
    private JTextField textField_netid;
    private JLabel lbl_netmask;
    private JTextField textField_netmask;
    private JLabel lbl_networkName;
    private JTextField textField_network_name;
    private JButton btn_makeNetwork;

    // mention all the components that have to be taken here....
    public static final String COMPONENT_COMBO_BOX = "Network_ComboBox";
    public static final String COMPONENT_OVERVIEW_LABEL = "Network_OverviewLabel";

    public static boolean SHOW_DEFAULT = false;

    // for serving the functionalities....
    Map<String, JComponent> helpfulComponents;
    public ArrayList<Network> links, defaultNetworks;; // changed this to public on 08/12/23 for accessibility...
    private int lastID = 0;
    Dialog_Helper dialogHelper;

    public Dialog_Network(Map<String, JComponent> components) {
        this.helpfulComponents = components;
        this.defaultNetworks = new ArrayList<>();
        this.links = new ArrayList<>();
        this.dialogHelper = new Dialog_Helper(this);

        this.setContentPane(this.JPanel_main);
        this.setTitle("Create Network");
        this.setSize(400,200);
        this.setVisible(false);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        btn_makeNetwork.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addNetwork();
                updateOverviewTxt();
            }
        });
    }

    public void showNetworks() {
        ((JComboBox)this.helpfulComponents.get(COMPONENT_COMBO_BOX)).removeAllItems();
        if (SHOW_DEFAULT) {
            for (Network link : defaultNetworks) {
                ((JComboBox)this.helpfulComponents.get(COMPONENT_COMBO_BOX)).addItem(link.toString());
            }
        }
        for (Network link : links) {
            ((JComboBox)this.helpfulComponents.get(COMPONENT_COMBO_BOX)).addItem(link.toString());
        }
    }

    public void setDefaultNetworks(ArrayList<Network> links) {
        this.defaultNetworks = links;
    }

    public void showDefaultNetworks(boolean show) {
        SHOW_DEFAULT = show;
        this.showNetworks();
    }

    public ArrayList<Network> getAllNetworks() {
        ArrayList<Network> networks = new ArrayList<>();
        networks.addAll(this.links);
        networks.addAll(this.defaultNetworks);
        return networks;
    }

    private void addNetwork() {
        Network network = new Network(lastID++, this.textField_netid.getText().toString(), this.textField_netmask.getText().toString(), this.textField_network_name.getText().toString());
        this.links.add(network);
        ((JComboBox)this.helpfulComponents.get(COMPONENT_COMBO_BOX)).addItem(this.links.get(this.links.size()-1));
        this.dialogHelper.showInformationMsg("Network Added Successfully with name : "+this.textField_network_name.getText().toString(),"Success");
    }

    private void updateOverviewTxt() {
        ((JLabel)this.helpfulComponents.get(COMPONENT_OVERVIEW_LABEL)).setText("Networks : "+(this.links.size())+" networks created");
    }
}
