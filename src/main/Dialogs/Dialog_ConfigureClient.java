package Dialogs;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Dialog_ConfigureClient extends JFrame {
    private JPanel JPanel_main;
    private JLabel lbl_clientIndex;
    private JComboBox comboBox_clientIndex;
    private JLabel lbl_startTime;
    private JTextField textField_startTime;
    private JLabel lbl_upTime;
    private JTextField textField_upTime;
    private JLabel lbl_Mtu;
    private JTextField textField_mtu;
    private JLabel lbl_interval;
    private JTextField textField_interval;
    private JLabel lbl_packets;
    private JTextField textField_packets;
    private JButton btn_save;

    int totalNodes;
    public ArrayList<String> settings;

    public Dialog_ConfigureClient(int n) {
        this.totalNodes = n;
        this.settings = new ArrayList<>();

        // initializing the components...
        this.setContentPane(this.JPanel_main);
        this.setTitle("Client Configuration");
        this.setSize(400,250);
        this.setVisible(false);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        // action to be performed when clicking on save settings button...
        btn_save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // validate inputs...
                if (validateInputs()) {
                    // and if all are valid then save settings...
                    settings = new ArrayList<>();
                    settings.add(String.valueOf(comboBox_clientIndex.getSelectedIndex()));
                    settings.add(textField_startTime.getText());
                    settings.add(textField_upTime.getText());
                    settings.add(textField_mtu.getText());
                    settings.add(textField_interval.getText());
                    settings.add(textField_packets.getText());
                    showConfirmation();
                }
            }
        });
    }

    private void showConfirmation() {
        JOptionPane.showMessageDialog(this, "Client configuration settings has been saved!", "Action Completed!", JOptionPane.INFORMATION_MESSAGE);
        this.setVisible(false);
    }

    private boolean validateInputs() {

        // start time validation...
        if (!textField_startTime.getText().chars().allMatch(Character::isDigit) || textField_startTime.getText().toString().length() == 0) {
            JOptionPane.showMessageDialog(this,"Please enter valid start time!", "Error!", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // up time validation...
        if (!textField_upTime.getText().chars().allMatch(Character::isDigit) || textField_upTime.getText().toString().length() == 0) {
            JOptionPane.showMessageDialog(this,"Please enter valid up time!", "Error!", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // mtu validation...
        if (!textField_mtu.getText().chars().allMatch(Character::isDigit) || textField_mtu.getText().toString().length() == 0) {
            JOptionPane.showMessageDialog(this,"Please enter valid up MTU!", "Error!", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // interval validation...
        if (!textField_interval.getText().chars().allMatch(Character::isDigit) || textField_interval.getText().toString().length() == 0) {
            JOptionPane.showMessageDialog(this,"Please enter valid interval time!", "Error!", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // no. of packets...
        if (!textField_packets.getText().chars().allMatch(Character::isDigit) || textField_packets.getText().toString().length() == 0) {
            JOptionPane.showMessageDialog(this,"Please enter valid no. of packets!", "Error!", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    @Override
    public void setVisible(boolean b) {
        super.setVisible(b);
        if (b) {
            for (int i=0; i<this.totalNodes; i++) {
                this.comboBox_clientIndex.addItem("Node "+i);
            }
        }
    }

    public void showDialog(int n) {
        this.totalNodes = n;
        this.setVisible(true);
    }

    public int getStopTime() {
        return Integer.parseInt(this.settings.get(1)) + Integer.parseInt(this.settings.get(2));
    }

    public String getStartTime() {
        return this.settings.get(1);
    }

    public String getClientIndex() {
        return this.settings.get(0);
    }

    public String getMTU() {
        return this.settings.get(3);
    }

    public String getInterval() {
        return this.settings.get(4);
    }

    public String getPackets() {
        return this.settings.get(5);
    }
}
