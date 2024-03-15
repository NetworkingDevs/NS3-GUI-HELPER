package Dialogs;

import Helpers.LoggingHelper;
import Helpers.PlaceHolderHelper;
import Ns3Objects.Links.CSMA;
import Ns3Objects.Links.NetworkLink;
import Ns3Objects.Links.P2P;
import StatusHelper.LinkType;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import static Helpers.ApplicationSettingsHelper.*;

/**
 * Dialog to configure the default link settings
 * */
public class Dialog_DefaultLinkConfig extends JFrame {
    private JPanel JPanel_main;
    private JPanel JPanel_uppper;
    private JLabel lbl_delay;
    private JTextField textField_delay;
    private JLabel lbl_dataRate;
    private JPanel JPanel_group_dataRate;
    private JTextField textField_dataRate;
    private JComboBox comboBox_datarateModifier;
    private JLabel lbl_alias;
    private JTextField textField_alias;
    private JButton btn_save;
    private JPanel JPanel_lower;
    private JScrollPane JScrollPane_linkManager;
    private JPanel JPanel_links;
    private JLabel lbl_linkType;
    private JComboBox comboBox_linkType;
    private JCheckBox checkBox_enablePcap;

    /**
     * placeholder for delay
     * */
    private static String PLACEHOLDER_DELAY = "Enter delay (in ms)";
    /**
     * placeholder for data rate
     * */
    private static String PLACEHOLDER_DATA_RATE = "Enter data rate";
    /**
     * placeholder for alias name
     * */
    private static String PLACEHOLDER_ALIAS_NAME = "Enter alias name";
    /**
     * list of network settings
     * */
    public ArrayList<NetworkLink> defaultLinks;
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
    private static Dialog_DefaultLinkConfig INSTANCE;

    /**
     * to get the instance of this class
     *
     * @param links the list of link settings
     * @return the instance of this class
     * @since 1.1.0
     * */
    public static Dialog_DefaultLinkConfig getInstance(ArrayList<NetworkLink> links) {
        LoggingHelper.LogInfo("Checking for the available instance of Dialog_DefaultLinkConfig");
        if (INSTANCE==null) {
            LoggingHelper.LogDebug("The instance for Dialog_DefaultLinkConfig was not available!");
            INSTANCE = new Dialog_DefaultLinkConfig(links);
        }
        return INSTANCE;
    }
    /**
     * to make the object of type Dialog_DefaultLinkConfig
     *
     * @param links the list of links settings
     * @since 1.0.0
     * */
    public Dialog_DefaultLinkConfig(ArrayList<NetworkLink> links) {
        LoggingHelper.Log("Creating object of type Dialog_DefaultLinkConfig");
        // ==================== BASIC CONF. ====================
        this.setContentPane(this.JPanel_main);
        this.setTitle("Default Link Configuration");
        this.setSize(400,500);
        this.setVisible(false);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        // ==================== DONE BASIC CONF. ====================

        this.JPanel_links.setLayout(new FormLayout("fill:pref:grow, fill:pref:grow, fill:pref:grow"));
        this.JScrollPane_linkManager.setViewportView(this.JPanel_links);
        this.dialogHelper = new Dialog_Helper(this);

        this.defaultLinks = links;
        if (this.defaultLinks.size() > 0) {
            this.showLinks();
        }

        PlaceHolderHelper.addPlaceHolder(textField_delay,PLACEHOLDER_DELAY);
        PlaceHolderHelper.addPlaceHolder(textField_dataRate,PLACEHOLDER_DATA_RATE);
        PlaceHolderHelper.addPlaceHolder(textField_alias,PLACEHOLDER_ALIAS_NAME);
        this.setUpEventListeners();
    }

    /**
     * to show the link settings
     *
     * @since 1.0.0
     * */
    public void showLinks() {
        LoggingHelper.LogFunction("Dialog Default Link : Rendering each link in JPanel!");
        for (int i=0; i<this.defaultLinks.size(); i++) {
            LoggingHelper.LogDebug("Rendering link : "+i);
            // making a label...
            JLabel lbl = new JLabel(this.defaultLinks.get(i).toString());

            // making edit button...
            JButton btnUpdate = new JButton("Edit");
            btnUpdate.setActionCommand(String.valueOf(i));
            btnUpdate.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    LoggingHelper.LogDebug("Action command while clicking on edit button : "+e.getActionCommand());
                    // set all the link params to configuration form...
                    showLinkSettings(Integer.parseInt(e.getActionCommand()));
                }
            });

            // making a button for link deletion...
            JButton btn = new JButton("Delete");
            btn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    editIndex = -1;
                    LoggingHelper.LogDebug("Action command while clicking on delete button : "+e.getActionCommand());
                    int yes = dialogHelper.showConfirmationDialog("Do you really want to delete this link?", "Confirmation!");
                    if (yes == JOptionPane.YES_OPTION) {
                        LoggingHelper.LogDebug("User chose to delete link with index : "+e.getActionCommand());
                        defaultLinks.remove(Integer.parseInt(e.getActionCommand()));
                        LoggingHelper.LogDebug("Deleted link from default link!");
                        showLinksAgain();
                    }
                }
            });
            btn.setActionCommand(String.valueOf(i));

            ((FormLayout) this.JPanel_links.getLayout()).appendRow(new RowSpec("pref"));
            this.JPanel_links.add(lbl, new CellConstraints().xy(1, i+1));
            this.JPanel_links.add(btnUpdate, new CellConstraints().xy(2,i+1));
            this.JPanel_links.add(btn, new CellConstraints().xy(3, i+1));

            LoggingHelper.LogDebug("Link "+i+" Rendered!");
        }
    }

    /**
     * to show the links settings
     *
     * @since 1.0.0
     * */
    public void showLinksAgain() {
        LoggingHelper.LogDebug("Dialog Default Link : Creating a new JPanel (after deleting last link / first time rendering)!");
        this.JPanel_links = new JPanel();
        this.JPanel_links.setLayout(new FormLayout("fill:pref:grow, fill:pref:grow, fill:pref:grow"));
        this.JPanel_links.setSize(new Dimension(400, 500));
        this.JScrollPane_linkManager.setViewportView(this.JPanel_links);
        this.showLinks();
        LoggingHelper.LogDebug("Rendering new JPanel on JScrollPane!");
    }

    /**
     * to show the link settings
     *
     * @param index the index of the link settings
     * @since 1.0.0
     * */
    private void showLinkSettings(int index) {
        LoggingHelper.LogFunction("Dialog Default Link : show Link Settings called!");
        this.editIndex = index;
        NetworkLink selectedLink = this.defaultLinks.get(index);
        this.textField_delay.setText(selectedLink.getDelay());
        this.textField_dataRate.setText(selectedLink.getDataRate());
        this.textField_alias.setText(selectedLink.getName());
        this.comboBox_datarateModifier.setSelectedIndex(this.getDataRateIndex(selectedLink.getSpeedModifier()));
        this.checkBox_enablePcap.setSelected(selectedLink.getEnablePcap());
        LoggingHelper.LogDebug("All fields have been changed to selected link!");
    }

    /**
     * to get the index of the speed modifier
     *
     * @param value the value of the speed modifier
     * @return the equivalent index
     * @since 1.0.0
     * */
    private int getDataRateIndex(String value) {
        LoggingHelper.LogFunction("Dialog Default Link : get Data Rate Index called!");
        if (value.equals("KB/s")) {
            return 0;
        } else if (value.equals("MB/s")) {
            return 1;
        } else { // assumed to be GB/s
            return 2;
        }
    }

    /**
     * to set up all events
     *
     * @since 1.0.0
     * */
    private void setUpEventListeners() {
        // action to be performed when clicking on save button....
        btn_save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoggingHelper.LogFunction("Clicked on save button!");
                if (editIndex > -1) {
                    defaultLinks.get(editIndex).setLinkType(NetworkLink.getLinkType(comboBox_linkType.getSelectedIndex()));
                    defaultLinks.get(editIndex).setDelay(textField_delay.getText());
                    defaultLinks.get(editIndex).setDataRate(textField_dataRate.getText());
                    defaultLinks.get(editIndex).setName(textField_alias.getText());
                    defaultLinks.get(editIndex).setSpeedModifier(comboBox_datarateModifier.getSelectedItem().toString());
                    defaultLinks.get(editIndex).setEnablePcap(checkBox_enablePcap.isSelected());
                    editIndex = -1;
                    dialogHelper.showInformationMsg("Link has been updated successfully!", "Success!");
                } else {
                    // logic to see what's the type of channel has been selected...
                    if (LinkType.LINK_CSMA == NetworkLink.getLinkType(comboBox_linkType.getSelectedIndex())) {
                        defaultLinks.add(new CSMA(defaultLinks.size(), textField_alias.getText(), textField_delay.getText(), textField_dataRate.getText(), comboBox_datarateModifier.getSelectedItem().toString(), checkBox_enablePcap.isSelected(), true));
                    } else {
                        defaultLinks.add(new P2P(defaultLinks.size(), textField_alias.getText(), textField_delay.getText(), textField_dataRate.getText(), comboBox_datarateModifier.getSelectedItem().toString(), checkBox_enablePcap.isSelected(), true));
                    }
                    dialogHelper.showInformationMsg("Link has been added successfully!", "Success!");
                }
                showLinksAgain();
                LoggingHelper.LogDebug("Links have been changed and rendered successfully after clicking on save button!");
            }
        });
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                saveDefaultLinks(defaultLinks);
                saveSettings();
                editIndex = -1;
            }
        });
    }
}
