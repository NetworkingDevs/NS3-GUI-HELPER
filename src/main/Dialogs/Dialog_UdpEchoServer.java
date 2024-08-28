package Dialogs;

import Helpers.LoggingHelper;
import Helpers.PlaceHolderHelper;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
        this.setSize(400,300);
        this.setVisible(false);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        this.selectedNode = n;

        // putting all the placeholders in place...
        PlaceHolderHelper.addPlaceHolder(textField_portNo, PLACEHOLDER_PORT);
        PlaceHolderHelper.addPlaceHolder(textField_startTime,PLACEHOLDER_START_TIME);
        PlaceHolderHelper.addPlaceHolder(textField_upTime, PLACEHOLDER_UP_TIME);
        this.lbl_selectedServerIndex.setText("Selected '"+this.selectedNode+"'");

        // action to be performed when clicking on Save Settings button...
        btn_Save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // put the action here only...
            }
        });
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
