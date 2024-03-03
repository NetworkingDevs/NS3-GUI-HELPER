package Dialogs;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Map;

/**
 * to manage the configuration of one UDP Echo client
 * */
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

    // mention all the components that have to be taken here....
    /**
     * the key for overview label
     * */
    public static final String COMPONENT_OVERVIEW_LABEL = "Server_OverviewLabel";

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
     * the client configuration settings
     * */
    public ArrayList<String> settings;
    /**
     * dialog helper
     * */
    Dialog_Helper dialogHelper;


    /**
     * to create the object of type Dialog_ConfigureClient Server
     *
     * @param n total no. of nodes
     * @param helpfulComponents the map of helpful components
     * @since 0.3.0
     * */
    public Dialog_ConfigureClient(int n, Map<String, JComponent> helpfulComponents) {
        this.helpfulComponents = helpfulComponents;
        this.totalNodes = n;
        this.settings = new ArrayList<>();
        this.dialogHelper = new Dialog_Helper(this.JPanel_main);

        // initializing the components...
        this.setContentPane(this.JPanel_main);
        this.setTitle("Client Configuration");
        this.setSize(400,275);
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
                    updateOverviewTxt();
                    dialogHelper.showInformationMsg("Client configuration settings has been saved!", "Action Completed!");
                    setVisible(false);
                }
            }
        });
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
                this.comboBox_clientIndex.addItem("Node "+i);
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
     * to get the stop time of the client
     *
     * @since 1.0.0
     * */
    public int getStopTime() {
        return Integer.parseInt(this.settings.get(1)) + Integer.parseInt(this.settings.get(2));
    }

    /**
     * to get the start time of the client
     *
     * @since 1.0.0
     * */
    public String getStartTime() {
        return this.settings.get(1);
    }

    /**
     * to get the index of the client node
     *
     * @since 1.0.0
     * */
    public String getClientIndex() {
        return this.settings.get(0);
    }

    /**
     * to get the MTU of the client
     *
     * @since 1.0.0
     * */
    public String getMTU() {
        return this.settings.get(3);
    }

    /**
     * to get the interval of the client
     *
     * @since 1.0.0
     * */
    public String getInterval() {
        return this.settings.get(4);
    }

    /**
     * to get the no. of packets for the client
     *
     * @since 1.0.0
     * */
    public String getPackets() {
        return this.settings.get(5);
    }

    /**
     * to update the overview text
     *
     * @since 0.3.0
     * */
    private void updateOverviewTxt() {
        ((JLabel)this.helpfulComponents.get(COMPONENT_OVERVIEW_LABEL)).setText("Client Index : Configured node "+this.settings.get(0));
    }

    /**
     * to validate the input settings
     *
     * @return the boolean value indicating the validity of the inputs
     * @since 0.3.0
     * */
    private boolean validateInputs() {

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

        // mtu validation...
        if (!textField_mtu.getText().chars().allMatch(Character::isDigit) || textField_mtu.getText().toString().length() == 0) {
            this.dialogHelper.showErrorMsg("Please enter valid up MTU!", "Error!");
            return false;
        }

        // interval validation...
        if (!textField_interval.getText().chars().allMatch(Character::isDigit) || textField_interval.getText().toString().length() == 0) {
            this.dialogHelper.showErrorMsg("Please enter valid interval time!", "Error!");
            return false;
        }

        // no. of packets...
        if (!textField_packets.getText().chars().allMatch(Character::isDigit) || textField_packets.getText().toString().length() == 0) {
            this.dialogHelper.showErrorMsg("Please enter valid no. of packets!", "Error!");
            return false;
        }

        return true;
    }
}
