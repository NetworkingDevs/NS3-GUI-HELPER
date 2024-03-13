package Dialogs;

import Helpers.LoggingHelper;
import Ns3Objects.Links.CSMA;
import Ns3Objects.Links.NetworkLink;
import Ns3Objects.Links.P2P;
import StatusHelper.LinkType;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Map;

/**
 * The dialog for management of links settings
 * */
public class Dialog_Link extends JFrame {
    private JPanel JPanel_main;
    private JLabel lbl_delay;
    private JTextField textField_delay;
    private JLabel lbl_dataRate;
    private JPanel JPanel_sppedModifier_main;
    private JTextField textField_speed;
    private JComboBox comboBox_speedModifier;
    private JLabel lbl_name;
    private JTextField textField_name;
    private JButton btn_buildLink;
    private JLabel lbl_linkType;
    private JComboBox comboBox_linkType;
    private JCheckBox chkBox_enablePcap;

    // mention all the components that have to be taken here....
    /**
     * the key for the combo box
     * */
    public static final String COMPONENT_COMBO_BOX = "Link_ComboBox";
    /**
     * the key for the overview label
     * */
    public static final String COMPONENT_OVERVIEW_LABEL = "Link_OverviewLabel";
    /**
     * to indicate that whether the default network settings are visible or not
     * */
    public static boolean SHOW_DEFAULT = false;

    // for serving the functionalities....
    /**
     * the map of helpful components
     * */
    Map<String, JComponent> helpfulComponents;
    /**
     * link settings
     * */
    public ArrayList<NetworkLink> links; // changed this to public on 08/12/23 for accessibility...
    /**
     * the dialog helper to show messages in dialog
     * */
    Dialog_Helper dialogHelper;

    /**
     * the instance of this class
     * */
    private static Dialog_Link INSTANCE;
    /**
     * to get the instance of this class
     *
     * @param helpfulComponents the components that should be managed
     * @since 1.1.0
     * */
    public static Dialog_Link getInstance(Map<String, JComponent> helpfulComponents) {
        if (INSTANCE == null) {
            INSTANCE = new Dialog_Link(helpfulComponents);
        }
        return INSTANCE;
    }

    /**
     * to create the object of type Dialog_Link
     *
     * @param components the helpful components
     * @since 0.3.0
     * */
    public Dialog_Link(Map<String, JComponent> components) {
        LoggingHelper.Log("Creating object of type Dialog_Link");
        this.helpfulComponents = components;
        this.links = new ArrayList<>();
        this.dialogHelper = new Dialog_Helper(this);

        this.setContentPane(this.JPanel_main);
        this.setTitle("Create Link");
        this.setSize(400,245);
        this.setVisible(false);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        btn_buildLink.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoggingHelper.LogFunction("Dialog Link : Adding a link!");
                addLink();
                updateOverviewTxt();
            }
        });
    }

    /**
     * to show the available link settings
     *
     * @since 0.3.0
     * */
    private void showLinks() {
        LoggingHelper.LogFunction("Dialog Link : show links called!");
        ((JComboBox)this.helpfulComponents.get(COMPONENT_COMBO_BOX)).removeAllItems();
        if (SHOW_DEFAULT) {
            for(NetworkLink link : this.links) {
                ((JComboBox)this.helpfulComponents.get(COMPONENT_COMBO_BOX)).addItem(link.toString());
            }
            LoggingHelper.LogDebug("Showing all the links on the canvas, size of links : "+this.links.size());
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

    public void setDefaultLinks(ArrayList<NetworkLink> defaultLinks) {
        LoggingHelper.LogFunction("Dialog Link : set Default Links called!");
        for (NetworkLink defaultLink : defaultLinks) {
            boolean alreadyExist = false;
            for (NetworkLink link : this.links) {
                if (link.toString().equalsIgnoreCase(defaultLink.toString())) {
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
     * to make all default links visible
     *
     * @param show whether to show or hide default links
     * @since 0.3.0
     * */
    public void showDefaultLinks(boolean show) {
        LoggingHelper.LogFunction("Dialog Link : show Default Link is called!");
        SHOW_DEFAULT = show;
        this.showLinks();
    }

    /**
     * to get all the link settings
     *
     * @return the list of link settings
     * @since 0.3.0
     * */
    public ArrayList<NetworkLink> getAllLinks() {
        LoggingHelper.LogFunction("Dialog Link : get All Links called!");
        ArrayList<NetworkLink> allLinks = new ArrayList<>();
        allLinks.addAll(this.links);
        LoggingHelper.LogDebug("Making a single list of all link, size of allLinks : "+allLinks.size());
        return allLinks;
    }

    /**
     * to get the count of P2P Links
     *
     * @return the size of P2P Link settings
     * @since 1.1.0
     * */
    public int getP2pLinkCount() {
        LoggingHelper.LogFunction("Dialog Link : get p2p link count called!");
        int count = 0;
        if (this.links.size() > 0) {
            for(int i=0; i<this.links.size(); i++) {
                if (this.links.get(i).getLinkType() == LinkType.LINK_P2P) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * to get the count of CSMA Links
     *
     * @return the size of CSMA Link settings
     * @since 1.1.0
     * */
    public int getCsmaLinkCount() {
        LoggingHelper.LogFunction("Dialog Link : get csma link count called!");
        int count = 0;
        if (this.links.size() > 0) {
            for(int i=0; i<this.links.size(); i++) {
                if (this.links.get(i).getLinkType() == LinkType.LINK_CSMA) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * to add the link setting
     *
     * @since 0.3.0
     * */
    private void addLink() {
        LoggingHelper.LogFunction("Dialog Link : adding a link!");
        NetworkLink link;
        if (NetworkLink.getLinkType(comboBox_linkType.getSelectedIndex())== LinkType.LINK_CSMA) {
            link = new CSMA(this.links.size(), this.textField_name.getText().toString(), this.textField_delay.getText().toString(), this.textField_speed.getText().toString(), this.comboBox_speedModifier.getSelectedItem().toString(), chkBox_enablePcap.isSelected());
        } else { // default case or P2P Link...
            link = new P2P(this.links.size(), this.textField_name.getText().toString(), this.textField_delay.getText().toString(), this.textField_speed.getText().toString(), this.comboBox_speedModifier.getSelectedItem().toString(), chkBox_enablePcap.isSelected());
        }
        this.links.add(link);
        ((JComboBox)this.helpfulComponents.get(COMPONENT_COMBO_BOX)).addItem(this.links.get(this.links.size()-1));
        this.dialogHelper.showInformationMsg("Link Added Successfully with name : "+this.textField_name.getText().toString(),"Success");
    }

    /**
     * to update the overview text
     *
     * @since 0.3.0
     * */
    private void updateOverviewTxt() {
        LoggingHelper.LogInfo("Dialog Link : updating overview text!");
        ((JLabel)this.helpfulComponents.get(COMPONENT_OVERVIEW_LABEL)).setText("Links : "+(this.links.size())+" links created");
    }
}
