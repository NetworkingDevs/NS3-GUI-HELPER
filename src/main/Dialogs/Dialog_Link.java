package Dialogs;

import Helpers.LinkHelper;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

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

    // for serving the functionalities....
    private JComboBox comboBox;
    public ArrayList<LinkHelper> links; // changed this to public on 08/12/23 for accessibility...
    private int lastID = 0;

    public Dialog_Link(JComboBox comboBox) {
        this.comboBox = comboBox;
        this.links = new ArrayList<>();

        this.setContentPane(this.JPanel_main);
        this.setTitle("Create Link");
        this.setSize(400,200);
        this.setVisible(false);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        this.links.add(new LinkHelper(lastID++,"DefaultLink", "3", "500", "MB/s"));
        btn_buildLink.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addLink();
            }
        });
    }

    public void showDefaultLink() {
        this.comboBox.addItem(this.links.get(0));
    }

    private void addLink() {
        LinkHelper link = new LinkHelper(lastID++, this.textField_name.getText().toString(), this.textField_delay.getText().toString(), this.textField_speed.getText().toString(), this.comboBox_speedModifier.getSelectedItem().toString());
        this.links.add(link);
        this.comboBox.addItem(this.links.get(this.links.size()-1));
        JOptionPane.showMessageDialog(this.JPanel_main,"Link Added Successfully with name : "+this.textField_name.getText().toString(),"Success",JOptionPane.INFORMATION_MESSAGE);
    }
}
