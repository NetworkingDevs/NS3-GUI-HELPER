package Dialogs;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Map;

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

    // mention all the components that have to be taken here....
    public static final String COMPONENT_OVERVIEW_LABEL = "Client_OverviewLabel";

    // for serving the functionalities....
    Map<String, JComponent> helpfulComponents;

    // for serving the functionalities...
    int totalNodes;
    public ArrayList<String> settings;
    Dialog_Helper dialogHelper;

    public Dialog_ConfigureServer(int n, Map<String, JComponent> helpfulComponents) {
        this.totalNodes = n;
        this.helpfulComponents = helpfulComponents;
        this.settings = new ArrayList<>();
        this.dialogHelper = new Dialog_Helper(this.JPanel_main);

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
                    updateOverviewTxt();
                    dialogHelper.showInformationMsg("Server configuration settings has been saved!", "Action Completed!");
                    setVisible(false);
                }
            }
        });
    }

    private void updateOverviewTxt() {
        ((JLabel)this.helpfulComponents.get(COMPONENT_OVERVIEW_LABEL)).setText("Server Index : Configured node "+this.settings.get(0));
    }

    private boolean validateInputs() {
        // port number validation...
        if (!textField_portNo.getText().chars().allMatch(Character::isDigit) || textField_portNo.getText().toString().length() == 0) {
            this.dialogHelper.showErrorMsg("Please enter valid port number!", "Error!");
            return false;
        }

        // start time validation...
        if (!textField_startTime.getText().chars().allMatch(Character::isDigit) || textField_startTime.getText().toString().length() == 0) {
            this.dialogHelper.showErrorMsg("Please enter valid start time!", "Error!");
            return false;
        }

        // up time validation...
        if (!textField_upTime.getText().chars().allMatch(Character::isDigit) || textField_upTime.getText().toString().length() == 0) {
            this.dialogHelper.showErrorMsg("Please enter valid up time!", "Error!");
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
