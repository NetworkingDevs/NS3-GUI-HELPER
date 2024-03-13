package Dialogs;

import Helpers.LoggingHelper;
import Helpers.PlaceHolderHelper;
import Ns3Objects.Links.NetworkLink;
import Ns3Objects.Links.WIFI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Map;

/**
 * The dialog for management of links setting for WiFi Links
 * */
public class Dialog_WiFiLink extends JFrame {
    private JPanel JPanel_main;
    private JLabel lbl_standard;
    private JComboBox comboBox_standard;
    private JLabel lbl_stationManager;
    private JComboBox comboBox_stationManager;
    private JLabel lbl_ssid;
    private JTextField textField_ssid;
    private JLabel lbl_alias;
    private JTextField textField_alias;
    private JButton btn_save;
    private JCheckBox checkBox_genDefault;
    private JCheckBox checkBox_enablePcap;

    /**
     * placeholder for ssid
     * */
    private static final String PLACEHOLDER_SSID = "Enter SSID";
    /**
     * placeholder for alias name
     * */
    private static final String PLACEHOLDER_ALIAS = "Enter Alias name";
    /**
     * the key for the combo box
     * */
    public static final String COMPONENT_COMBO_BOX = "WiFiLink_ComboBox";
    /**
     * the key for the overview label
     * */
    public static final String COMPONENT_OVERVIEW_LABEL = "WiFiLink_OverviewLabel";
    /**
     * to indicate that whether the default network settings are visible or not
     * */
    public static boolean SHOW_DEFAULT = false;
    /**
     * the map of helpful components
     * */
    Map<String, JComponent> helpfulComponents;
    /**
     * link settings
     * */
    public ArrayList<NetworkLink> links;
    /**
     * the dialog helper to show messages in dialog
     * */
    Dialog_Helper dialogHelper;
    /**
     * the instance of this class
     * */
    private static Dialog_WiFiLink INSTANCE;
    /**
     * to get the instance of this class
     *
     * @param helpfulComponents the components that should be managed
     * @since 1.2.0
     * */
    public static Dialog_WiFiLink getInstance(Map<String, JComponent> helpfulComponents) {
        LoggingHelper.LogLogic("Checking for the instance of Dialog_WiFiLink");
        if (INSTANCE == null) {
            LoggingHelper.LogDebug("The instance for Dialog_WiFiLink was not available!");
            INSTANCE = new Dialog_WiFiLink(helpfulComponents);
        }
        return INSTANCE;
    }

    /**
     * to create the object of type Dialog_WiFiLink
     *
     * @param components the helpful components
     * @since 1.2.0
     * */
    public Dialog_WiFiLink(Map<String, JComponent> components) {
        LoggingHelper.Log("Creating object of type Dialog_WiFiLink");
        this.helpfulComponents = components;
        this.links = new ArrayList<>();
        this.dialogHelper = new Dialog_Helper(this);
        this.setContentPane(this.JPanel_main);
        this.setTitle("Create Wi-Fi Link");
        this.setSize(400,280);
        this.setVisible(false);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        // action to be performed when clicking on save button
        btn_save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoggingHelper.LogFunction("Dialog WiFiLink : adding a wi-fi link!");
                addLink();
                updateOverviewTxt();
            }
        });
        // action to be performed when checking generate with default settings...
        checkBox_genDefault.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoggingHelper.LogFunction("Dialog WiFiLink : generating the default link!");
                if (checkBox_genDefault.isSelected()) {
                    LoggingHelper.LogDebug("Dialog WiFiLink : checkbox is selected!");
                    comboBox_standard.setSelectedIndex(0);
                    comboBox_stationManager.setSelectedIndex(2);
                    textField_ssid.setText("test_ssid_"+links.size());
                    textField_alias.setText("test_link_"+links.size());
                } else {
                    LoggingHelper.LogDebug("Dialog WiFiLink : checkbox is not selected!");
                    comboBox_stationManager.setSelectedIndex(0);
                    textField_alias.setText("Enter Alias name");
                    textField_ssid.setText("Enter SSID");
                }
            }
        });
        PlaceHolderHelper.addPlaceHolder(textField_alias,PLACEHOLDER_ALIAS);
        PlaceHolderHelper.addPlaceHolder(textField_ssid,PLACEHOLDER_SSID);
    }

    /**
     * to show the available link settings
     *
     * @since 1.2.0
     * */
    private void showLinks() {
        LoggingHelper.LogFunction("Dialog WiFiLink : showing the links!");
        ((JComboBox)this.helpfulComponents.get(COMPONENT_COMBO_BOX)).removeAllItems();
        if (SHOW_DEFAULT) {
            for(NetworkLink link : this.links) {
                ((JComboBox)this.helpfulComponents.get(COMPONENT_COMBO_BOX)).addItem(link.toString());
            }
            LoggingHelper.LogDebug("Showing all the wifi links on the canvas, size of links : "+this.links.size());
        } else {
            for (NetworkLink link : this.links) {
                if (!link.isDefault()) {
                    ((JComboBox)this.helpfulComponents.get(COMPONENT_COMBO_BOX)).addItem(link.toString());
                    LoggingHelper.LogDebug(link.toString()+" is not a default link!");
                } else {
                    LoggingHelper.LogDebug(link.toString()+" is default link!");
                }
            }
            LoggingHelper.LogDebug("Showing only the links which are not default, size of links : "+this.links.size());
        }
    }

    /**
     * to make all default links visible
     *
     * @param show whether to show or hide default links
     * @since 1.2.0
     * */
    public void showDefaultLinks(boolean show) {
        LoggingHelper.LogFunction("Dialog WiFiLink : show default links called!");
        SHOW_DEFAULT = show;
        this.showLinks();
    }

    /**
     * to get all the wi-fi link settings
     *
     * @return the list of link settings
     * @since 1.2.0
     * */
    public ArrayList<NetworkLink> getAllLinks() {
        LoggingHelper.LogFunction("Dialog WiFiLink : get all links called!");
        ArrayList<NetworkLink> allLinks = new ArrayList<>();
        allLinks.addAll(this.links);
        LoggingHelper.LogDebug("Making a single list of all link, size of allLinks : "+allLinks.size());
        return allLinks;
    }

    public void setDefaultLinks(ArrayList<NetworkLink> defaultLinks) {
        LoggingHelper.LogFunction("Dialog WiFiLink : set default links called!");
        for (NetworkLink defaultLink : defaultLinks) {
            boolean alreadyExist = false;
            for (NetworkLink link : this.links) {
                if (link.forSettings().equalsIgnoreCase(defaultLink.forSettings())) {
                    alreadyExist = true;
                    break;
                }
            }
            if (!alreadyExist) {
                this.links.add(defaultLink);
            }
        }
    }

    /**
     * to get the count of Wi-Fi Links
     *
     * @return the size of Wi-Fi Link settings
     * @since 1.2.0
     * */
    public int getLinkCount() {
        LoggingHelper.LogFunction("Dialog WiFiLink : get link count called!");
        return this.links.size();
    }

    /**
     * to add the link setting
     *
     * @since 1.2.0
     * */
    private void addLink() {
        LoggingHelper.LogFunction("Dialog WiFiLink : adding a wi-fi link!");
        NetworkLink link = new WIFI(this.links.size(), this.textField_alias.getText(),(String) this.comboBox_standard.getSelectedItem(), (String) this.comboBox_stationManager.getSelectedItem(), this.textField_ssid.getText(), this.checkBox_enablePcap.isSelected());
        this.links.add(link);
        ((JComboBox)this.helpfulComponents.get(COMPONENT_COMBO_BOX)).addItem(this.links.get(this.links.size()-1));
        this.dialogHelper.showInformationMsg("Link Added Successfully with name : "+this.textField_alias.getText().toString(),"Success");
        if (checkBox_genDefault.isSelected()) {
            comboBox_stationManager.setSelectedIndex(0);
            textField_alias.setText("");
            textField_ssid.setText("");
            checkBox_genDefault.setSelected(false);
        }
    }

    /**
     * to update the overview text
     *
     * @since 1.2.0
     * */
    private void updateOverviewTxt() {
        LoggingHelper.LogInfo("Dialog WiFiLink : updating the overview text!");
        ((JLabel)this.helpfulComponents.get(COMPONENT_OVERVIEW_LABEL)).setText("Wi-Fi Links : "+(this.links.size())+" links created");
    }

}
