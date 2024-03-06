package Dialogs;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Map;

/**
 * to manage the configuration of one UDP Echo server
 * */
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
    /**
     * the key for overview label
     * */
    public static final String COMPONENT_OVERVIEW_LABEL = "Client_OverviewLabel";

    // for serving the functionalities....
    /**
     * the map for helpful components
     * */
    Map<String, JComponent> helpfulComponents;

    // for serving the functionalities...
    /**
     * the total no. of nodes
     * */
    int totalNodes;
    /**
     * the server configuration settings
     * */
    public ArrayList<String> settings;
    /**
     * dialog helper
     * */
    Dialog_Helper dialogHelper;

    /**
     * to create the object of type Dialog_ConfigureServer
     *
     * @param n total no. of nodes
     * @param helpfulComponents the map of helpful components
     * @since 0.3.0
     * */
    public Dialog_ConfigureServer(int n, Map<String, JComponent> helpfulComponents) {
        this.totalNodes = n;
        this.helpfulComponents = helpfulComponents;
        this.settings = new ArrayList<>();
        this.dialogHelper = new Dialog_Helper(this.JPanel_main);

        // initializing the components...
        this.setContentPane(this.JPanel_main);
        this.setTitle("Server Configuration");
        this.setSize(400,225);
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

    /**
     * to update the overview text
     *
     * @since 0.3.0
     * */
    private void updateOverviewTxt() {
        ((JLabel)this.helpfulComponents.get(COMPONENT_OVERVIEW_LABEL)).setText("Server Index : Configured node "+this.settings.get(0));
    }

    /**
     * to validate the input settings
     *
     * @return the boolean value indicating the validity of the inputs
     * @since 0.3.0
     * */
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

    /**
     * to make the dialog visible with list of all nodes
     *
     * @param b whether to show or hide
     * @since 0.3.0
     * */
    @Override
    public void setVisible(boolean b) {
        super.setVisible(b);
        if (b) {
            for (int i=0; i<this.totalNodes; i++) {
                this.comboBox_serverInex.addItem("Node "+i);
            }
        }
    }

    /**
     * to show dialog with nodes
     *
     * @param n the total nodes
     * @since 0.3.0
     * */
    public void showDialog(int n) {
        this.totalNodes = n;
        this.setVisible(true);
    }

    /**
     * to get the stop time of the server
     *
     * @since 1.0.0
     * */
    public int getStopTime() {
        return Integer.parseInt(this.settings.get(2)) + Integer.parseInt(this.settings.get(3));
    }

    /**
     * to get the index of the server node
     *
     * @since 1.0.0
     * */
    public String getServerIndex() {
        return this.settings.get(0);
    }

    /**
     * to get the port number of the server
     *
     * @since 1.0.0
     * */
    public String getPortNumber() {
        return this.settings.get(1);
    }

    /**
     * to get the start time of the server
     *
     * @since 1.0.0
     * */
    public String getStartTime() {
        return this.settings.get(2);
    }
}
