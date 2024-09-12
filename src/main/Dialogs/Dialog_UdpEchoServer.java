package Dialogs;

import Helpers.LoggingHelper;
import Helpers.PlaceHolderHelper;
import Ns3Objects.UdpEchoCommunication.UdpEchoServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Dialog_UdpEchoServer extends JFrame implements Dialog {
    private JPanel JPanel_main;
    private JLabel lbl_serverIndex;
    private JLabel lbl_selectedServerIndex;
    private JTextField textField_portNo;
    private JLabel lbl_portNo;
    private JLabel lbl_startTime;
    private JTextField textField_startTime;
    private JLabel lbl_upTime;
    private JTextField textField_upTime;
    private JButton btn_Save;

    /**
     * the list of active servers in the current topology
     * */
    public ArrayList<UdpEchoServer> serverList;
    /**
     * The dialog helper for errors and warnings.
     * */
    private Dialog_Helper dialogHelper;
    /**
     * the instance of this class
     * */
    private static Dialog_UdpEchoServer INSTANCE;
    /**
     * the selected node, to configure Udp Echo Server instance...
     * */
    private int selectedNode;

    public static Dialog_UdpEchoServer getInstance(int n) {
        LoggingHelper.LogInfo("Checking for available instance of Dialog_UdpEchoServer!");
        if (INSTANCE == null) {
            LoggingHelper.LogDebug("The instance for Dialog_UdpEchoServer was not available!");
            INSTANCE = new Dialog_UdpEchoServer(n);
        }
        return INSTANCE;
    }

    /**
     * Constructor to create an object of type Dialog_UdpEchoServer
     *
     * @since 1.3.0
     * */
    public Dialog_UdpEchoServer(int n) {
        LoggingHelper.Log("Creating object of type Dialog_UdpEchoServer");

        // initializing the components...
        this.setContentPane(this.JPanel_main);
        this.setTitle("Udp Echo Server Configuration");
        this.setSize(400,200);
        this.setVisible(false);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        this.selectedNode = n;
        this.serverList = new ArrayList<>();
        this.dialogHelper = new Dialog_Helper(this);

        // putting all the placeholders in place...
        PlaceHolderHelper.addPlaceHolder(textField_portNo, PLACEHOLDER_PORT);
        PlaceHolderHelper.addPlaceHolder(textField_startTime,PLACEHOLDER_START_TIME);
        PlaceHolderHelper.addPlaceHolder(textField_upTime, PLACEHOLDER_UP_TIME);
        this.lbl_selectedServerIndex.setText("Selected '"+this.selectedNode+"'");

        // action to be performed when clicking on Save Settings button...
        btn_Save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoggingHelper.LogFunction("Trying to save the Udp Echo Server Configuration");
                // put the action here only...
                // validation of parameters...
                if (validateInputs()) {
                    // Making a new server configuration...
                    UdpEchoServer serverConfig = new UdpEchoServer(selectedNode, Integer.parseInt(textField_portNo.getText()), Integer.parseInt(textField_startTime.getText()), Integer.parseInt(textField_upTime.getText()));
                    // saving the new server configuration
                    serverList.add(serverConfig);
                    // showing the success dialog box
                    dialogHelper.showInformationMsg("The server added successfully!", "Success!");
                    // clearing out all the fields
                    textField_portNo.setText("");
                    textField_startTime.setText("");
                    textField_upTime.setText("");
                    // hiding this dialog box...
                    setVisible(false);
                }
            }
        });
    }

    /**
     * To validate the server configuration parameters...
     *
     * @return A boolean value containing the status of validation of server configuration params.
     * @since 1.3.0
     * */
    private boolean validateInputs() {
        LoggingHelper.LogFunction("Udp Echo Server Config : Validating all inputs");

        if (!textField_portNo.getText().chars().allMatch(Character::isDigit) || textField_portNo.getText().isEmpty()) {
            this.dialogHelper.showErrorMsg("Please enter valid port number!", "Error!");
            return false;
        }

        if (!textField_startTime.getText().chars().allMatch(Character::isDigit) || textField_startTime.getText().isEmpty()) {
            this.dialogHelper.showErrorMsg("Please enter valid start time!", "Error!");
            return false;
        }

        if (!textField_upTime.getText().chars().allMatch(Character::isDigit) || textField_upTime.getText().isEmpty()) {
            this.dialogHelper.showErrorMsg("Please enter valid up time!", "Error!");
            return false;
        }

        return true;
    }

    /**
     * To alter the visibility of the dialog box...
     *
     * @param show a boolean value, whether to show or hide.
     * @since 1.3.0
     * */
    @Override
    public void setVisible(boolean show) {
        super.setVisible(show);
        // do some another processing, if required, before altering the visibility...
    }

    /**
     * It will show the dialog with the configuration changes...
     *
     * @param n Selected Node Index
     * @since 1.3.0
     * @see Dialog_UdpEchoServer#setVisible(boolean)
     * */
    public void showDialog(int n) {
        this.selectedNode = n;
        this.lbl_selectedServerIndex.setText("Selected '"+this.selectedNode+"'");
        this.setVisible(true);
    }
}
