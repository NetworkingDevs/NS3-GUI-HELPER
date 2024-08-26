package Dialogs;

import Helpers.LoggingHelper;
import Helpers.PlaceHolderHelper;
import Ns3Objects.Netoworks.Network;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import static Helpers.ApplicationSettingsHelper.*;

/**
 * Dialog to configure the default network settings
 * */
public class Dialog_DefaultNetworkConfig extends JFrame implements Dialog {
    private JPanel JPanel_main;
    private JPanel JPanel_top;
    private JPanel JPanel_top_top;
    private JLabel lbl_netId;
    private JTextField textField_netid;
    private JLabel lbl_netmask;
    private JTextField textField_netmask;
    private JLabel lbl_alias;
    private JTextField textField_alias;
    private JButton btn_save;
    private JPanel JPanel_bottom;
    private JScrollPane JScrollPane_network_manager;
    private JPanel JPanel_networks;


    /**
     * list of network settings
     * */
    public ArrayList<Network> defaultNetworks;
    /**
     * dialog helper
     * */
    Dialog_Helper dialogHelper;
    /**
     * the index for editing the object
     * */
    int editIndex = -1;
    /**
     * the instance of this class
     * */
    private static Dialog_DefaultNetworkConfig INSTANCE;

    /**
     * to get the instance of this class
     *
     * @param networks the list of network settings
     * @return the instance of this class
     * @since 1.1.0
     * */
    public static Dialog_DefaultNetworkConfig getInstance(ArrayList<Network> networks) {
        LoggingHelper.LogInfo("Checking for the instance of Dialog_DefaultNetworkConfig");
        if (INSTANCE==null) {
            LoggingHelper.LogDebug("The instance for Dialog_DefaultNetworkConfig was not available!");
            INSTANCE = new Dialog_DefaultNetworkConfig(networks);
        }
        return INSTANCE;
    }

    /**
     * to make the object of type Dialog_DefaultNetworkConfig
     *
     * @param networks the list of network settings
     * @since 1.0.0
     * */
    public Dialog_DefaultNetworkConfig(ArrayList<Network> networks) {
        LoggingHelper.Log("Creating object of type Dialog_DefaultNetworkConfig");
        // ==================== BASIC CONF. ====================
        this.setContentPane(this.JPanel_main);
        this.setTitle("Default Network Configuration");
        this.setSize(400,500);
        this.setVisible(false);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        // ==================== DONE BASIC CONF. ====================

        this.JPanel_networks.setLayout(new FormLayout("fill:pref:grow, fill:pref:grow, fill:pref:grow"));
        this.JScrollPane_network_manager.setViewportView(this.JPanel_networks);
        this.dialogHelper = new Dialog_Helper(this);

        this.defaultNetworks = networks;
        if (this.defaultNetworks.size() > 0) {
            this.showNetworks();
        }

        PlaceHolderHelper.addPlaceHolder(textField_netid,PLACEHOLDER_NET_ID);
        PlaceHolderHelper.addPlaceHolder(textField_netmask,PLACEHOLDER_SUBNET_MASK);
        PlaceHolderHelper.addPlaceHolder(textField_alias,PLACEHOLDER_ALIAS_NAME);
        this.setUpEventListeners();
    }

    /**
     * to show the network settings
     *
     * @since 1.0.0
     * */
    public void showNetworks() {
        LoggingHelper.LogFunction("Dialog Default Network : Rendering each network in JPanel!");
        for (int i=0; i<this.defaultNetworks.size(); i++) {
            LoggingHelper.LogDebug("Rendering network : "+i);
            // making a label...
            JLabel lbl = new JLabel(this.defaultNetworks.get(i).toString());

            // making edit button...
            JButton btnUpdate = new JButton("Edit");
            btnUpdate.setActionCommand(String.valueOf(i));
            btnUpdate.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    LoggingHelper.LogDebug("Action command while clicking on edit button : "+e.getActionCommand());
                    // set all the n/w params to configuration form...
                    showNetworkSettings(Integer.parseInt(e.getActionCommand()));
                }
            });

            // making a button for n/w deletion...
            JButton btn = new JButton("Delete");
            btn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    LoggingHelper.LogDebug("Action command while clicking on delete button : "+e.getActionCommand());
                    int yes = dialogHelper.showConfirmationDialog("Do you really want to delete this network?", "Confirmation!");
                    if (yes == JOptionPane.YES_OPTION) {
                        LoggingHelper.LogDebug("User chose to delete network with index : "+e.getActionCommand());
                        defaultNetworks.remove(Integer.parseInt(e.getActionCommand()));
                        LoggingHelper.LogDebug("Deleted Network from default networks!");
                        showNetworksAgain();
                    }
                }
            });
            btn.setActionCommand(String.valueOf(i));

            ((FormLayout) this.JPanel_networks.getLayout()).appendRow(new RowSpec("pref"));
            this.JPanel_networks.add(lbl, new CellConstraints().xy(1, i+1));
            this.JPanel_networks.add(btnUpdate, new CellConstraints().xy(2,i+1));
            this.JPanel_networks.add(btn, new CellConstraints().xy(3, i+1));

            LoggingHelper.LogDebug("Network "+i+" Rendered!");
        }
    }

    /**
     * to show the network settings
     *
     * @since 1.0.0
     * */
    public void showNetworksAgain() {
        LoggingHelper.LogFunction("Dialog Default Network : Creating a new JPanel (after deleting last n/w / first time rendering)!");
        this.JPanel_networks = new JPanel();
        this.JPanel_networks.setLayout(new FormLayout("fill:pref:grow, fill:pref:grow, fill:pref:grow"));
        this.JPanel_networks.setSize(new Dimension(400, 500));
        this.JScrollPane_network_manager.setViewportView(this.JPanel_networks);
        this.showNetworks();
        LoggingHelper.LogDebug("Rendering new JPanel on JScrollPane!");
    }

    /**
     * to show the networks settings
     *
     * @param index the index of the network settings
     * @since 1.0.0
     * */
    private void showNetworkSettings(int index) {
        LoggingHelper.LogFunction("Dialog Default Network : show Network Settings called!");
        this.editIndex = index;
        Network selectedNetwork = this.defaultNetworks.get(index);
        this.textField_netid.setText(selectedNetwork.netId);
        this.textField_netmask.setText(selectedNetwork.netMask);
        this.textField_alias.setText(selectedNetwork.name);
        LoggingHelper.LogDebug("All fields have been changed to selected network!");
    }

    /**
     * to set up all events
     *
     * @since 1.0.0
     * */
    private void setUpEventListeners() {
        // act as a placeholder
        this.textField_netid.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                if (textField_netid.getText().equals("Enter NetID")) {
                    textField_netid.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                if (textField_netid.getText().equals("")) {
                    textField_netid.setText("Enter NetID");
                }
            }
        });
        // act as a placeholder
        this.textField_netmask.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                if (textField_netmask.getText().equals("Enter netmask")) {
                    textField_netmask.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                if (textField_netmask.getText().equals("")) {
                    textField_netmask.setText("Enter netmask");
                }
            }
        });
        // act as a placeholder
        this.textField_alias.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                if (textField_alias.getText().equals("Enter alias name")) {
                    textField_alias.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                if (textField_alias.getText().equals("")) {
                    textField_alias.setText("Enter alias name");
                }
            }
        });
        // action to be performed when clicking on save button....
        btn_save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoggingHelper.LogFunction("Dialog Default Network : Clicked on save button!");
                if (editIndex > -1) {
                    defaultNetworks.get(editIndex).netId = textField_netid.getText();
                    defaultNetworks.get(editIndex).netMask = textField_netmask.getText();
                    defaultNetworks.get(editIndex).name = textField_alias.getText();
                    editIndex = -1;
                    dialogHelper.showInformationMsg("N/W has been updated successfully!", "Success!");
                } else {
                    defaultNetworks.add(new Network(defaultNetworks.size(), textField_netid.getText(), textField_netmask.getText(), textField_alias.getText(), true));
                    dialogHelper.showInformationMsg("N/W has been added successfully!", "Success!");
                }
                showNetworksAgain();
                LoggingHelper.LogDebug("N/W have been changed and rendered successfully after clicking on save button!");
            }
        });
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                saveDefaultNetworks(defaultNetworks);
                saveSettings();
                editIndex = -1;
            }
        });
    }
}
