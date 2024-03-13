package Dialogs;

import Helpers.LoggingHelper;
import Helpers.PlaceHolderHelper;
import Ns3Objects.Netoworks.Network;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Map;

/**
 * to manage network settings
 * */
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
    /**
     * placeholder fdr net id
     * */
    private static String PLACEHOLDER_NET_ID = "Enter NetID";
    /**
     * placeholder for subnet mask
     * */
    private static String PLACEHOLDER_SUBNET_MASK = "Enter Subnet mask";
    /**
     * placeholder for alias name
     * */
    private static String PLACEHOLDER_ALIAS_NAME = "Enter Name";
    /**
     * key for the combo box
     * */
    public static final String COMPONENT_COMBO_BOX = "Network_ComboBox";
    /**
     * key for the overview label
     * */
    public static final String COMPONENT_OVERVIEW_LABEL = "Network_OverviewLabel";
    /**
     * to indicate that whether the default network settings are visible or not
     * */
    public static boolean SHOW_DEFAULT = false;
    /**
     * the instance of this class
     * */
    private static Dialog_Network INSTANCE;

    /**
     * to get the instance of this class
     *
     * @param helpfulComponents the components that should be managed
     * @since 1.1.0
     * */
    public static Dialog_Network getInstance(Map<String, JComponent> helpfulComponents) {
        LoggingHelper.LogInfo("Checking for the instance of Dialog_Network");
        if (INSTANCE == null) {
            LoggingHelper.LogDebug("The instance of Dialog_Network was not available.");
            INSTANCE = new Dialog_Network(helpfulComponents);
        }
        return INSTANCE;
    }

    // for serving the functionalities....
    /**
     * the map of helpful components
     * */
    Map<String, JComponent> helpfulComponents;
    /**
     * network settings
     * */
    public ArrayList<Network> links;; // changed this to public on 08/12/23 for accessibility...
    /**
     * the id of last inserted network settings
     * */
    private int lastID = 0;
    /**
     * the dialog helper to show messages in dialog
     * */
    Dialog_Helper dialogHelper;


    /**
     * to create the object of type Dialog_Network
     *
     * @param components the helpful components
     * @since 0.3.0
     * */
    public Dialog_Network(Map<String, JComponent> components) {
        LoggingHelper.Log("Creating object of type Dialog_Network!");
        this.helpfulComponents = components;
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
                LoggingHelper.LogFunction("Adding a network settings!");
                addNetwork();
                updateOverviewTxt();
            }
        });

        PlaceHolderHelper.addPlaceHolder(textField_netid,PLACEHOLDER_NET_ID);
        PlaceHolderHelper.addPlaceHolder(textField_netmask,PLACEHOLDER_SUBNET_MASK);
        PlaceHolderHelper.addPlaceHolder(textField_network_name,PLACEHOLDER_ALIAS_NAME);
    }

    /**
     * to show the available network settings
     *
     * @since 0.3.0
     * */
    private void showNetworks() {
        LoggingHelper.LogFunction("Dialog Network : show networks called!");
        ((JComboBox)this.helpfulComponents.get(COMPONENT_COMBO_BOX)).removeAllItems();
        if (SHOW_DEFAULT) {
            for (Network link : links) {
                ((JComboBox)this.helpfulComponents.get(COMPONENT_COMBO_BOX)).addItem(link.toString());
            }
        } else {
            for (Network link : links) {
                if (!link.isDefault) {
                    ((JComboBox)this.helpfulComponents.get(COMPONENT_COMBO_BOX)).addItem(link.toString());
                }
            }
        }
    }

    public void setDefaultNetworks(ArrayList<Network> links) {
        LoggingHelper.LogFunction("Dialog Network : set default networks called!");
        this.links.addAll(links);
    }

    /**
     * to make all default links visible
     *
     * @param show whether to show or hide default links
     * @since 0.3.0
     * */
    public void showDefaultNetworks(boolean show) {
        LoggingHelper.LogFunction("Dialog Network : show default networks called!");
        SHOW_DEFAULT = show;
        this.showNetworks();
    }

    /**
     * to get all the network settings
     *
     * @return the list of network settings
     * @since 0.3.0
     * */
    public ArrayList<Network> getAllNetworks() {
        LoggingHelper.LogFunction("Dialog Network : get all networks called!");
        ArrayList<Network> networks = new ArrayList<>();
        networks.addAll(this.links);
        return networks;
    }

    /**
     * to get the count of network settings
     *
     * @return the size of network settings
     * @since 1.1.0
     * */
    public int getNetworkCount() {
        LoggingHelper.LogFunction("Dialog Network : get network count called!");
        return this.links.size();
    }

    /**
     * to add the network setting
     *
     * @since 0.3.0
     * */
    private void addNetwork() {
        LoggingHelper.LogFunction("Dialog Network : adding a network settings!");
        Network network = new Network(lastID++, this.textField_netid.getText().toString(), this.textField_netmask.getText().toString(), this.textField_network_name.getText().toString());
        this.links.add(network);
        ((JComboBox)this.helpfulComponents.get(COMPONENT_COMBO_BOX)).addItem(this.links.get(this.links.size()-1));
        this.dialogHelper.showInformationMsg("Network Added Successfully with name : "+this.textField_network_name.getText().toString(),"Success");
    }

    /**
     * to update the overview text
     *
     * @since 0.3.0
     * */
    private void updateOverviewTxt() {
        LoggingHelper.LogLogic("Dialog Link : changing the overview text!");
        ((JLabel)this.helpfulComponents.get(COMPONENT_OVERVIEW_LABEL)).setText("Networks : "+(this.links.size())+" networks created");
    }
}
