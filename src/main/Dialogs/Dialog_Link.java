package Dialogs;

import Links.CSMA;
import Links.NetworkLink;
import Links.P2P;
import StatusHelper.LinkType;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Map;

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
    public static final String COMPONENT_COMBO_BOX = "Link_ComboBox";
    public static final String COMPONENT_OVERVIEW_LABEL = "Link_OverviewLabel";

    public static boolean SHOW_DEFAULT = false;

    // for serving the functionalities....
    Map<String, JComponent> helpfulComponents;
    public ArrayList<NetworkLink> links, defaultLinks; // changed this to public on 08/12/23 for accessibility...
    Dialog_Helper dialogHelper;

    public Dialog_Link(Map<String, JComponent> components) {
        this.helpfulComponents = components;
        this.links = new ArrayList<>();
        this.defaultLinks = new ArrayList<>();
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
                addLink();
                updateOverviewTxt();
            }
        });
    }

    public void showLinks() {
        ((JComboBox)this.helpfulComponents.get(COMPONENT_COMBO_BOX)).removeAllItems();
        if (SHOW_DEFAULT) {
            for (NetworkLink link : defaultLinks) {
                ((JComboBox)this.helpfulComponents.get(COMPONENT_COMBO_BOX)).addItem(link.toString());
            }
        }
        for (NetworkLink link : links) {
            ((JComboBox)this.helpfulComponents.get(COMPONENT_COMBO_BOX)).addItem(link.toString());
        }
    }

    public void setDefaultLinks(ArrayList<NetworkLink> links) {
        this.defaultLinks = links;
    }

    public void showDefaultLinks(boolean show) {
        SHOW_DEFAULT = show;
        this.showLinks();
    }

    public ArrayList<NetworkLink> getAllLinks() {
        ArrayList<NetworkLink> links = this.links;
        if (defaultLinks.size() > 0) {
            links.addAll(defaultLinks);
        }
        return links;
    }

    private void addLink() {
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

    private void updateOverviewTxt() {
        ((JLabel)this.helpfulComponents.get(COMPONENT_OVERVIEW_LABEL)).setText("Links : "+(this.links.size())+" links created");
    }
}
