package Dialogs;

import Helpers.LoggingHelper;
import Helpers.PlaceHolderHelper;
import Ns3Objects.UdpEchoCommunication.UdpEchoServer;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.function.Predicate;

public class Dialog_UdpEchoServerManager extends JFrame implements Dialog {
    private JPanel JPanel_main;
    private JPanel JPanel_configSettings;
    private JLabel lbl_serverIndex;
    private JComboBox comboBox_serverIndex;
    private JLabel lbl_portNo;
    private JTextField textField_portNo;
    private JLabel lbl_startTime;
    private JTextField textField_startTime;
    private JLabel lbl_upTime;
    private JTextField textField_upTime;
    private JButton saveSettingsButton;
    private JPanel JPanel_filters;
    private JLabel lbl_filterServerIndex;
    private JComboBox comboBox_filterServerIndex;
    private JCheckBox checkBox_applyFilter;
    private JPanel JPanel_manageConfig;
    private JScrollPane JScrollPane_manageConfig;

    /**
     * Total nodes
     * */
    int n;
    /**
     * Edit index for the server configuration
     * */
    private int editIndex = -1;
    /**
     * List of all configured servers
     * */
    ArrayList<UdpEchoServer> serverList;
    /**
     * The dialog helper for warnings and errors
     * */
    Dialog_Helper dialogHelper;
    /**
     * Instance of this class
     * */
    private static Dialog_UdpEchoServerManager INSTANCE;

    /**
     * To get the instance of class {@code Dialog_UdpEchoServerManager}
     *
     * @return The instance of class {@code Dialog_UdpEchoServerManager}
     * @since 1.3.0
     * */
    public static Dialog_UdpEchoServerManager getInstance() {
        LoggingHelper.LogInfo("Checking for the available instance of Dialog_UdpEchoServerManager");
        if (INSTANCE == null) {
            LoggingHelper.LogDebug("The instance for Dialog_UdpEchoServerManager was not available!");
            INSTANCE = new Dialog_UdpEchoServerManager();
        }
        return INSTANCE;
    }

    /**
     * To make the object of type Dialog_UdpEchoServerManager
     *
     * @since 1.3.0
     * */
    public Dialog_UdpEchoServerManager() {
        LoggingHelper.Log("Creating object of type Dialog_UdpEchoServerManager");
        // ==================== BASIC CONF. ====================
        this.setContentPane(this.JPanel_main);
        this.setTitle("UDP Echo Server Manager");
        this.setSize(400,500);
        this.setVisible(false);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        this.checkBox_applyFilter.setSelected(false);
        this.comboBox_filterServerIndex.setEnabled(false);
        // ==================== DONE BASIC CONF. ====================

        this.JPanel_manageConfig.setLayout(new FormLayout("fill:pref:grow, fill:pref:grow, fill:pref:grow"));
        this.JScrollPane_manageConfig.setViewportView(this.JPanel_manageConfig);
        this.dialogHelper = new Dialog_Helper(this);

        PlaceHolderHelper.addPlaceHolder(textField_portNo, PLACEHOLDER_PORT);
        PlaceHolderHelper.addPlaceHolder(textField_startTime, PLACEHOLDER_START_TIME);
        PlaceHolderHelper.addPlaceHolder(textField_upTime, PLACEHOLDER_UP_TIME);

        this.setUpEventListeners();
    }

    private void setUpEventListeners() {
        LoggingHelper.Log("Setting up all the event listeners for Udp Echo Server Manager Dialog Box!");

        // action to be performed when clicked on "Apply Filters" checkbox...
        this.checkBox_applyFilter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoggingHelper.LogFunction("Clicked on Apply Filters Checkbox!");
                // what if the "Apply Filters" checkbox is clicked! and it's selected...
                comboBox_filterServerIndex.setEnabled(checkBox_applyFilter.isSelected());
                if (checkBox_applyFilter.isSelected()) {
                    showServerListAgain(comboBox_filterServerIndex.getSelectedIndex());
                } else {
                    showServerListAgain(-1);
                }
            }
        });

        // action to be performed when clicked on "Save Settings" button...
        this.saveSettingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoggingHelper.LogFunction("Clicked on Save Button!");
                if (editIndex > -1) {
                    int index = comboBox_serverIndex.getSelectedIndex();
                    int port = Integer.parseInt(textField_portNo.getText());
                    int startTime = Integer.parseInt(textField_startTime.getText());
                    int upTime = Integer.parseInt(textField_upTime.getText());

                    boolean changedIndex = serverList.get(editIndex).getIndex() != index;
                    boolean changedPort = serverList.get(editIndex).getPortNo() != port;
                    boolean changedStartTime = serverList.get(editIndex).getStartTime() != startTime;
                    boolean changedUpTime = serverList.get(editIndex).getUpTime() != upTime;

                    if (changedIndex || changedPort || changedStartTime || changedUpTime) {
                        serverList.get(editIndex).setIndex(index);
                        serverList.get(editIndex).setPortNo(port);
                        serverList.get(editIndex).setStartTime(startTime);
                        serverList.get(editIndex).setUpTime(upTime);
                        editIndex = -1;
                        dialogHelper.showInformationMsg("Server Configuration Updated Successfully!", "Success!");
                    } else {
                        int yes = dialogHelper.showConfirmationDialog("The configuration is not changed. Are you sure to edit?", "Nothing's changed!");
                        if (yes == JOptionPane.YES_OPTION) {
                            resetAllFields();
                        }
                    }
                } else {
                    serverList.add(new UdpEchoServer(comboBox_serverIndex.getSelectedIndex(),Integer.parseInt(textField_portNo.getText()), Integer.parseInt(textField_startTime.getText()), Integer.parseInt(textField_upTime.getText())));
                    dialogHelper.showInformationMsg("Server Added Successfully!", "Success!");
                    // resetting the fields...
                    resetAllFields();
                }
                // rendering the updated links...
                showServerListAgain();
            }
        });

        // action to be performed when closing the window...
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                editIndex = -1;
                resetAllFields();
            }
        });

        // action to be performed when server index filter dropdowns value is changed...
        this.comboBox_filterServerIndex.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (checkBox_applyFilter.isSelected()) {
                    showServerListAgain(comboBox_filterServerIndex.getSelectedIndex());
                }
            }
        });
    }

    /**
     * To show the dialog box with the configuration changes...
     *
     * @since 1.3.0
     * */
    public void showDialog(int n, ArrayList<UdpEchoServer> serverList) {
        // do some configuration, if needed...
        this.n = n;
        if (this.serverList == null || this.serverList.isEmpty()) {
            this.serverList = new ArrayList<>();
            this.serverList.addAll(serverList);
        } else {
            for (UdpEchoServer server : serverList) {
                // Defining predicate to search on ID
                Predicate<UdpEchoServer> hasIdEquals = serverInRecord -> serverInRecord.getID()==server.getID();
                // searching in existing server list
                UdpEchoServer foundServer =  this.serverList.stream().filter(hasIdEquals).findAny().orElse(null);
                // if no server found with provided server ID
                if (foundServer == null) {
                    // add this new server to existing record
                    this.serverList.add(server);
                }
            }
        }
        this.showServerListAgain();
        this.comboBox_serverIndex.removeAllItems();
        this.comboBox_filterServerIndex.removeAllItems();
        for (int i=0; i<this.n; i++) {
            this.comboBox_serverIndex.addItem("Node : "+i);
            this.comboBox_filterServerIndex.addItem("Node : "+i);
        }
        this.setVisible(true);
    }

    /**
     * To edit the server configuration, it will show the current values
     *
     * @param index The index of the server
     * @since 1.3.0
     * */
    private void showServerConfiguration(int index) {
        LoggingHelper.LogFunction("Dialog Udp Echo Server Manager : show server Settings called!");
        this.editIndex = index;
        UdpEchoServer selectedServer = this.serverList.get(this.editIndex);
        this.comboBox_serverIndex.setSelectedIndex(selectedServer.getIndex());
        this.textField_portNo.setText(String.valueOf(selectedServer.getPortNo()));
        this.textField_startTime.setText(String.valueOf(selectedServer.getStartTime()));
        this.textField_upTime.setText(String.valueOf(selectedServer.getUpTime()));
        dialogHelper.showWarningMsg("Now, you can edit server settings!", "Ready to edit!");
        LoggingHelper.LogDebug("Dialog Udp Echo Server Manager : All fields have been changed to selected link!");
    }

    /**
     * To reset all the fields to default values
     *
     * @since 1.3.0
     * */
    private void resetAllFields() {
        comboBox_serverIndex.setSelectedIndex(0);
        textField_portNo.setText(PLACEHOLDER_PORT);
        textField_startTime.setText(PLACEHOLDER_START_TIME);
        textField_upTime.setText(PLACEHOLDER_UP_TIME);
    }

    /**
     * This will render all the server configuration on the screen
     *
     * @since 1.3.0
     * */
    private void showServerList() {
        this.showServerList(-1);
    }

    /**
     * This will render all the server configurations on the screen,
     * if not available; it will render proper information message.
     * And it will use filter parameter as "Server Index"
     *
     * @param filterIndex The filter server index
     * @since 1.3.0
     * */
    private void showServerList(int filterIndex) {
        LoggingHelper.LogFunction("Dialog Udp Echo Server Manager : Rendering each server config in JPanel!");

        if (this.serverList.isEmpty()) {
            ((FormLayout) this.JPanel_manageConfig.getLayout()).appendRow(new RowSpec("pref"));
            this.JPanel_manageConfig.add(new Label(""), new CellConstraints().xy(1, 1));
            this.JPanel_manageConfig.add(new Label("Nothing to show here yet!"), new CellConstraints().xy(2, 1));
            this.JPanel_manageConfig.add(new Label(""), new CellConstraints().xy(3, 1));
        } else {
            int rowIndex = 0;
            for (int i=0; i<this.serverList.size(); i++) {
                if (filterIndex==-1 || this.serverList.get(i).getIndex() == filterIndex) {
                    // COLUMN 1: The server information in form of label..
                    Label lbl_serverInfo = new Label(this.serverList.get(i).toString());

                    // COLUMN 2: Edit button for the server information...
                    JButton btn_editServerInfo = new JButton("Edit");
                    btn_editServerInfo.setActionCommand(String.valueOf(i));
                    btn_editServerInfo.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            showServerConfiguration(Integer.parseInt(e.getActionCommand()));
                        }
                    });

                    // COLUMN 3: Delete button for the server information...
                    JButton btn_deleteServerInfo = new JButton("Delete");
                    btn_deleteServerInfo.setActionCommand(String.valueOf(i));
                    btn_deleteServerInfo.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            editIndex = -1;
                            int serverIndex = serverList.get(Integer.parseInt(e.getActionCommand())).getIndex();
                            LoggingHelper.LogDebug("Action command while clicking on delete button : "+e.getActionCommand());
                            int yes = dialogHelper.showConfirmationDialog("Do you really want to delete this server?", "Confirmation!");
                            if (yes == JOptionPane.YES_OPTION) {
                                LoggingHelper.LogDebug("User chose to delete server on node "+ serverIndex +" : "+e.getActionCommand());
                                serverList.remove(Integer.parseInt(e.getActionCommand()));
                                LoggingHelper.LogDebug("Deleted server from node : "+serverIndex);
                                showServerListAgain();
                            }
                        }
                    });

                    ((FormLayout) this.JPanel_manageConfig.getLayout()).appendRow(new RowSpec("pref"));
                    this.JPanel_manageConfig.add(lbl_serverInfo, new CellConstraints().xy(1, rowIndex+1));
                    this.JPanel_manageConfig.add(btn_editServerInfo, new CellConstraints().xy(2, rowIndex+1));
                    this.JPanel_manageConfig.add(btn_deleteServerInfo, new CellConstraints().xy(3, rowIndex+1));
                    rowIndex++;
                }
            }
            if (rowIndex == 0) {
                ((FormLayout) this.JPanel_manageConfig.getLayout()).appendRow(new RowSpec("pref"));
                this.JPanel_manageConfig.add(new Label(""), new CellConstraints().xy(1, 1));
                this.JPanel_manageConfig.add(new Label("Nothing to show here! Try changing filter!"), new CellConstraints().xy(2, 1));
                this.JPanel_manageConfig.add(new Label(""), new CellConstraints().xy(3, 1));
            }
        }
    }

    /**
     * To show the server list after some changes in server list
     *
     * @param filterIndex The filter index no.
     * @since 1.3.0
     * @see Dialog_UdpEchoServerManager#showServerList()
     * @see Dialog_UdpEchoServerManager#showServerList(int) 
     * */
    private void showServerListAgain(int filterIndex) {
        LoggingHelper.LogDebug("Dialog Udp Echo Server Manager : Creating a new JPanel (after deleting last server / first time rendering)!");
        this.JPanel_manageConfig = new JPanel();
        this.JPanel_manageConfig.setLayout(new FormLayout("fill:pref:grow, fill:pref:grow, fill:pref:grow"));
        this.JScrollPane_manageConfig.setViewportView(this.JPanel_manageConfig);
        this.dialogHelper = new Dialog_Helper(this);
        this.showServerList(filterIndex);
    }
    
    /**
     * To show the server list after some changes in server list
     * 
     * @since 1.3.0
     * */
    private void showServerListAgain() {
        this.showServerListAgain(-1);
    }

}
