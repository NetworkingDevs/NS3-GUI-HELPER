package Dialogs;

import Helpers.LinkHelper;
import Helpers.NetworkHelper;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Dialog_Network extends JFrame {
    private JPanel JPanel_main;
    private JLabel lbl_netId;
    private JTextField textField_netid;
    private JLabel lbl_netmask;
    private JTextField textField_netmask;
    private JLabel lbl_networkName;
    private JTextField textField_network_name;
    private JButton btn_makeNetwork;

    // for serving the functionalities....
    private JComboBox comboBox;
    ArrayList<NetworkHelper> links;
    private int lastID = 0;

    public Dialog_Network(JComboBox comboBox) {
        this.comboBox = comboBox;
        this.links = new ArrayList<>();

        this.setContentPane(this.JPanel_main);
        this.setTitle("Create Network");
        this.setSize(400,200);
        this.setVisible(true);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        this.links.add(new NetworkHelper(lastID++, "54.0.0.0","255.0.0.0","Default"));
        btn_makeNetwork.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addNetwork();
            }
        });
    }

    public void showDefaultNetwork() {
        this.comboBox.addItem(this.links.get(0));
    }

    private void addNetwork() {
        NetworkHelper network = new NetworkHelper(lastID++, this.textField_netid.getText().toString(), this.textField_netmask.getText().toString(), this.textField_network_name.getText().toString());
        this.links.add(network);
        this.comboBox.addItem(this.links.get(this.links.size()-1));
        JOptionPane.showMessageDialog(this.JPanel_main,"Network Added Successfully with name : "+this.textField_network_name.getText().toString(),"Success",JOptionPane.INFORMATION_MESSAGE);
    }
}
