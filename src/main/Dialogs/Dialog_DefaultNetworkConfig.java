/**
 * Program name: Dialog_DefaultNetworkCOnfig
 * Program date: 06-01-2024
 * Program owner: henil
 * Contributor:
 * Last Modified: 06-01-2024
 * <p>
 * Purpose:
 */
package Dialogs;

import Helpers.DebuggingHelper;
import Helpers.NetworkHelper;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.ArrayList;

public class Dialog_DefaultNetworkConfig extends JFrame {
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

    public ArrayList<NetworkHelper> defaultNetworks;
    Dialog_Helper dialogHelper;
    int editIndex = -1;

    public Dialog_DefaultNetworkConfig(ArrayList<NetworkHelper> networks) {
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

        if (DebuggingHelper.TESTING_STATUS) {
            this.defaultNetworks = new ArrayList<>();
            this.defaultNetworks.add(new NetworkHelper(0, "54.0.0.0", "255.0.0.0", "Net1"));
            this.defaultNetworks.add(new NetworkHelper(1, "55.0.0.0", "255.0.0.0", "Net2"));
            this.defaultNetworks.add(new NetworkHelper(2, "56.0.0.0", "255.0.0.0", "Net3"));
            this.showNetworksAgain();
        } else {
            this.defaultNetworks = networks;
            if (this.defaultNetworks.size() > 0) {
                this.showNetworks();
            }
        }

        this.setUpEventListeners();
    }

    public void showNetworks() {
        DebuggingHelper.Debugln("Rendering each nework in JPanel!");
        for (int i=0; i<this.defaultNetworks.size(); i++) {
            DebuggingHelper.Debugln("Rendering network : "+i);
            // making a label...
            JLabel lbl = new JLabel(this.defaultNetworks.get(i).toString());

            // making edit button...
            JButton btnUpdate = new JButton("Edit");
            btnUpdate.setActionCommand(String.valueOf(i));
            btnUpdate.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    DebuggingHelper.Debugln("Action command while clicking on edit button : "+e.getActionCommand());
                    // set all the n/w params to configuration form...
                    showNetworkSettings(Integer.parseInt(e.getActionCommand()));
                }
            });

            // making a button for n/w deletion...
            JButton btn = new JButton("Delete");
            btn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    DebuggingHelper.Debugln("Action command while clicking on delete button : "+e.getActionCommand());
                    int yes = dialogHelper.showConfirmationDialog("Do you really want to delete this network?", "Confirmation!");
                    if (yes == JOptionPane.YES_OPTION) {
                        DebuggingHelper.Debugln("User chose to delete network with index : "+e.getActionCommand());
                        defaultNetworks.remove(Integer.parseInt(e.getActionCommand()));
                        DebuggingHelper.Debugln("Deleted Network from default networks!");
                        showNetworksAgain();
                    }
                }
            });
            btn.setActionCommand(String.valueOf(i));

            ((FormLayout) this.JPanel_networks.getLayout()).appendRow(new RowSpec("pref"));
            this.JPanel_networks.add(lbl, new CellConstraints().xy(1, i+1));
            this.JPanel_networks.add(btnUpdate, new CellConstraints().xy(2,i+1));
            this.JPanel_networks.add(btn, new CellConstraints().xy(3, i+1));

            DebuggingHelper.Debugln("Network "+i+" Rendered!");
        }
    }

    public void showNetworksAgain() {
        DebuggingHelper.Debugln("Creating a new JPanel (after deleting last n/w / first time rendering)!");
        this.JPanel_networks = new JPanel();
        this.JPanel_networks.setLayout(new FormLayout("fill:pref:grow, fill:pref:grow, fill:pref:grow"));
        this.JPanel_networks.setSize(new Dimension(400, 500));
        this.JScrollPane_network_manager.setViewportView(this.JPanel_networks);
        this.showNetworks();
        DebuggingHelper.Debugln("Rendering new JPanel on JScrollPane!");
    }

    private void showNetworkSettings(int index) {
        this.editIndex = index;
        NetworkHelper selectedNetwork = this.defaultNetworks.get(index);
        this.textField_netid.setText(selectedNetwork.netId);
        this.textField_netmask.setText(selectedNetwork.netMask);
        this.textField_alias.setText(selectedNetwork.name);
        DebuggingHelper.Debugln("All fields have been changed to selected network!");
    }

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
                DebuggingHelper.Debugln("Clicked on save button!");
                if (editIndex > -1) {
                    defaultNetworks.get(editIndex).netId = textField_netid.getText();
                    defaultNetworks.get(editIndex).netMask = textField_netmask.getText();
                    defaultNetworks.get(editIndex).name = textField_alias.getText();
                    editIndex = -1;
                    dialogHelper.showInformationMsg("N/W has been updated successfully!", "Success!");
                } else {
                    defaultNetworks.add(new NetworkHelper(defaultNetworks.size(), textField_netid.getText(), textField_netmask.getText(), textField_alias.getText()));
                    dialogHelper.showInformationMsg("N/W has been added successfully!", "Success!");
                }
                showNetworksAgain();
                DebuggingHelper.Debugln("N/W have been changed and rendered successfully after clicking on save button!");
            }
        });
    }
}
