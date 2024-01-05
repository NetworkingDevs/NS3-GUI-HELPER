/**
 * Program name: Dialog_DefaultLinkConfig
 * Program date: 04-01-2024
 * Program owner: henil
 * Contributor:
 * Last Modified: 04-01-2024
 * <p>
 * Purpose: This class will manage creation and management of default links...
 */
package Dialogs;

import Helpers.DebuggingHelper;
import Helpers.LinkHelper;
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

    public ArrayList<LinkHelper> defaultLinks;
    Dialog_Helper dialogHelper;
    int editIndex = -1;

    public Dialog_DefaultLinkConfig(ArrayList<LinkHelper> links) {
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

        if (DebuggingHelper.TESTING_STATUS) {
            this.defaultLinks = new ArrayList<>();
            this.defaultLinks.add(new LinkHelper(0, "link1", "3","500","MB/s"));
            this.defaultLinks.add(new LinkHelper(1, "link2", "2","1","GB/s"));
            this.defaultLinks.add(new LinkHelper(2, "link3", "1","500","KB/s"));
            this.showLinksAgain();
        } else {
            this.defaultLinks = links;
            if (this.defaultLinks.size() > 0) {
                this.showLinks();
            }
        }

        this.setUpEventListeners();
    }

    public void showLinks() {
        DebuggingHelper.Debugln("Rendering each link in JPanel!");
        for (int i=0; i<this.defaultLinks.size(); i++) {
            DebuggingHelper.Debugln("Rendering link : "+i);
            // making a label...
            JLabel lbl = new JLabel(this.defaultLinks.get(i).toString());

            // making edit button...
            JButton btnUpdate = new JButton("Edit");
            btnUpdate.setActionCommand(String.valueOf(i));
            btnUpdate.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    DebuggingHelper.Debugln("Action command while clicking on edit button : "+e.getActionCommand());
                    // set all the link params to configuration form...
                    showLinkSettings(Integer.parseInt(e.getActionCommand()));
                }
            });

            // making a button for link deletion...
            JButton btn = new JButton("Delete");
            btn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    DebuggingHelper.Debugln("Action command while clicking on delete button : "+e.getActionCommand());
                    int yes = dialogHelper.showConfirmationDialog("Do you really want to delete this link?", "Confirmation!");
                    if (yes == JOptionPane.YES_OPTION) {
                        DebuggingHelper.Debugln("User chose to delete link with index : "+e.getActionCommand());
                        defaultLinks.remove(Integer.parseInt(e.getActionCommand()));
                        DebuggingHelper.Debugln("Deleted link from default link!");
                        showLinksAgain();
                    }
                }
            });
            btn.setActionCommand(String.valueOf(i));

            ((FormLayout) this.JPanel_links.getLayout()).appendRow(new RowSpec("pref"));
            this.JPanel_links.add(lbl, new CellConstraints().xy(1, i+1));
            this.JPanel_links.add(btnUpdate, new CellConstraints().xy(2,i+1));
            this.JPanel_links.add(btn, new CellConstraints().xy(3, i+1));

            DebuggingHelper.Debugln("Link "+i+" Rendered!");
        }
    }

    public void showLinksAgain() {
        DebuggingHelper.Debugln("Creating a new JPanel (after deleting last link / first time rendering)!");
        this.JPanel_links = new JPanel();
        this.JPanel_links.setLayout(new FormLayout("fill:pref:grow, fill:pref:grow, fill:pref:grow"));
        this.JPanel_links.setSize(new Dimension(400, 500));
        this.JScrollPane_linkManager.setViewportView(this.JPanel_links);
        this.showLinks();
        DebuggingHelper.Debugln("Rendering new JPanel on JScrollPane!");
    }

    private void showLinkSettings(int index) {
        this.editIndex = index;
        LinkHelper selectedLink = this.defaultLinks.get(index);
        this.textField_delay.setText(selectedLink.delay);
        this.textField_dataRate.setText(selectedLink.dataRate);
        this.textField_alias.setText(selectedLink.name);
        this.comboBox_datarateModifier.setSelectedIndex(this.getDataRateIndex(selectedLink.speedModifier));
        DebuggingHelper.Debugln("All fields have been changed to selected link!");
    }

    private int getDataRateIndex(String value) {
        if (value.equals("KB/s")) {
            return 0;
        } else if (value.equals("MB/s")) {
            return 1;
        } else { // assumed to be GB/s
            return 2;
        }
    }

    private void setUpEventListeners() {
        // act as a placeholder
        this.textField_delay.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                if (textField_delay.getText().equals("Enter delay (in ms)")) {
                    textField_delay.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                if (textField_delay.getText().equals("")) {
                    textField_delay.setText("Enter delay (in ms)");
                }
            }
        });
        // act as a placeholder
        this.textField_dataRate.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                if (textField_dataRate.getText().equals("Enter data rate")) {
                    textField_dataRate.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                if (textField_dataRate.getText().equals("")) {
                    textField_dataRate.setText("Enter data rate");
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
                    defaultLinks.get(editIndex).delay = textField_delay.getText();
                    defaultLinks.get(editIndex).dataRate = textField_dataRate.getText();
                    defaultLinks.get(editIndex).name = textField_alias.getText();
                    defaultLinks.get(editIndex).speedModifier = comboBox_datarateModifier.getSelectedItem().toString();
                    editIndex = -1;
                    dialogHelper.showInformationMsg("Link has been updated successfully!", "Success!");
                } else {
                    defaultLinks.add(new LinkHelper(defaultLinks.size(), textField_alias.getText(), textField_delay.getText(), textField_dataRate.getText(), comboBox_datarateModifier.getSelectedItem().toString()));
                    dialogHelper.showInformationMsg("Link has been added successfully!", "Success!");
                }
                showLinksAgain();
                DebuggingHelper.Debugln("Links have been changed and rendered successfully after clicking on save button!");
            }
        });
    }
}
