package Dialogs;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Dialog_ConfigureServer extends JFrame {
    private JPanel JPanel_main;
    private JLabel lbl_serverIndex;
    private JComboBox comboBox_serverInex;
    private JLabel lbl_portNo;
    private JTextField textField_portNo;
    private JLabel lbl_startTime;
    private JTextField textField_startTime;
    private JLabel lbl_upTime;
    private JTextField textField_upTime;
    private JButton btn_save;

    int totalNodes;
    public ArrayList<String> settings;

    public Dialog_ConfigureServer(int n) {
        this.totalNodes = n;
        this.settings = new ArrayList<>();

        // initializing the components...
        this.setContentPane(this.JPanel_main);
        this.setTitle("Server Configuration");
        this.setSize(400,200);
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
                    settings.add(String.valueOf(comboBox_serverInex.getSelectedIndex()));
                    settings.add(textField_portNo.getText());
                    settings.add(textField_startTime.getText());
                    settings.add(textField_upTime.getText());
                    showConfirmation();
                }
            }
        });
    }

    private void showConfirmation() {
        JOptionPane.showMessageDialog(this, "Server configuration settings has been saved!", "Action Completed!", JOptionPane.INFORMATION_MESSAGE);
        this.setVisible(false);
    }

    private boolean validateInputs() {
        // port number validation...
        if (!textField_portNo.getText().chars().allMatch(Character::isDigit) || textField_portNo.getText().toString().length() == 0) {
            JOptionPane.showMessageDialog(this,"Please enter valid port number!", "Error!", JOptionPane.ERROR_MESSAGE);
            return false;
        }

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

        return true;
    }

    @Override
    public void setVisible(boolean b) {
        super.setVisible(b);
        if (b) {
            for (int i=0; i<this.totalNodes; i++) {
                this.comboBox_serverInex.addItem("Node "+i);
            }
        }
    }

    public void showDialog(int n) {
        this.totalNodes = n;
        this.setVisible(true);
    }

    public int getStopTime() {
        return Integer.parseInt(this.settings.get(2)) + Integer.parseInt(this.settings.get(3));
    }

    public String getServerIndex() {
        return this.settings.get(0);
    }

    public String getPortNumber() {
        return this.settings.get(1);
    }

    public String getStartTime() {
        return this.settings.get(2);
    }
}
