package Dialogs;

import Helpers.LoggingHelper;
import Helpers.PlaceHolderHelper;
import Ns3Objects.Links.CSMA;
import Ns3Objects.Links.NetworkLink;
import Ns3Objects.Links.P2P;
import Ns3Objects.Links.WIFI;
import StatusHelper.LinkType;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import static Helpers.ApplicationSettingsHelper.*;

/**
 * Dialog to configure the default wi-fi link settings
 *
 * @since 1.2.0
 * */
public class Dialog_DefaultWiFiLinkConfig extends JFrame implements Dialog {
    private JPanel JPanel_main;
    private JPanel JPanel_upper;
    private JComboBox comboBox_wifiStandard;
    private JLabel lbl_standard;
    private JComboBox comboBox_stationManager;
    private JLabel lbl_stationManager;
    private JLabel lbl_ssid;
    private JTextField textField_ssid;
    private JLabel lbl_alias;
    private JTextField textField_alias;
    private JCheckBox chkBox_enablePcap;
    private JButton btn_save;
    private JPanel JPanel_lower;
    private JScrollPane JScrollPane_linkManager;
    private JPanel JPanel_links;


    /**
     * list of wi-fi link settings
     * */
    public ArrayList<WIFI> defaultLinks;
    /**
     * dialog helper
     * */
    private Dialog_Helper dialogHelper;
    /**
     * the index for editing the object
     * */
    private int editIndex = -1;
    /**
     * the instance of this class
     * */
    private static Dialog_DefaultWiFiLinkConfig INSTANCE;

    /**
     * to get the instance of this class
     *
     * @param links the list of links
     * @return the instance of this class
     * @since 1.2.0
     * */
    public static Dialog_DefaultWiFiLinkConfig getInstance(ArrayList<WIFI> links) {
        LoggingHelper.LogInfo("Checking for the available instance of Dialog_DefaultWiFiLinkConfig");
        if (INSTANCE==null) {
            LoggingHelper.LogDebug("The instance for Dialog_DefaultWiFiLinkConfig was not available!");
            INSTANCE = new Dialog_DefaultWiFiLinkConfig(links);
        }
        return INSTANCE;
    }

    /**
     * to make the object of type Dialog_DefaultWiFiLinkConfig
     *
     * @param links the list of wi-fi links
     * @since 1.2.0
     * */
    public Dialog_DefaultWiFiLinkConfig(ArrayList<WIFI> links) {
        LoggingHelper.Log("Creating object of type Dialog_DefaultWiFiLinkConfig");
        // ==================== BASIC CONF. ====================
        this.setContentPane(this.JPanel_main);
        this.setTitle("Default WiFi Configuration");
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

        PlaceHolderHelper.addPlaceHolder(textField_ssid,PLACEHOLDER_SSID);
        PlaceHolderHelper.addPlaceHolder(textField_alias,PLACEHOLDER_ALIAS_NAME);
        this.setUpEventListeners();
    }

    /**
     * to show the link settings
     *
     * @since 1.2.0
     * */
    public void showLinks() {
        LoggingHelper.LogFunction("Dialog Default WiFiLink : Rendering each link in JPanel!");
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
     * to show the wi-fi links settings
     *
     * @since 1.2.0
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
     * to show the wi-fi link settings
     *
     * @param index the index of the link settings
     * @since 1.2.0
     * */
    private void showLinkSettings(int index) {
        LoggingHelper.LogFunction("Dialog Default Link : show Link Settings called!");
        this.editIndex = index;
        WIFI selectedLink = (WIFI) this.defaultLinks.get(index);
        this.comboBox_wifiStandard.setSelectedItem(selectedLink.standard);
        this.comboBox_stationManager.setSelectedItem(selectedLink.remoteStationManager);
        this.textField_ssid.setText(selectedLink.ssid);
        this.textField_alias.setText(selectedLink.name);
        this.chkBox_enablePcap.setSelected(selectedLink.getEnablePcap());
        LoggingHelper.LogDebug("All fields have been changed to selected link!");
    }

    /**
     * to set up all events
     *
     * @since 1.2.0
     * */
    private void setUpEventListeners() {
        // action to be performed when clicking on save button....
        btn_save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoggingHelper.LogFunction("Clicked on save button!");
                if (editIndex > -1) {
                    defaultLinks.get(editIndex).standard = comboBox_wifiStandard.getSelectedItem().toString();
                    defaultLinks.get(editIndex).remoteStationManager = comboBox_stationManager.getSelectedItem().toString();
                    defaultLinks.get(editIndex).ssid = textField_ssid.getText().toString();
                    defaultLinks.get(editIndex).name = textField_alias.getText().toString();
                    defaultLinks.get(editIndex).setEnablePcap(chkBox_enablePcap.isSelected());
                    editIndex = -1;
                    dialogHelper.showInformationMsg("Link has been updated successfully!", "Success!");
                } else {
                    // logic to see what's the type of channel has been selected...
                    defaultLinks.add(new WIFI(defaultLinks.size(),textField_alias.getText(), (String) comboBox_wifiStandard.getSelectedItem(), (String) comboBox_stationManager.getSelectedItem(),textField_ssid.getText(), chkBox_enablePcap.isSelected(), true));
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
                saveDefaultWiFiLinks(defaultLinks);
                saveSettings();
                editIndex = -1;
            }
        });
    }
}
