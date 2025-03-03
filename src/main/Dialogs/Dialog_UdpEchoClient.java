package Dialogs;

import Helpers.LoggingHelper;
import Helpers.PlaceHolderHelper;
import Ns3Objects.Devices.Device;
import Ns3Objects.UdpEchoCommunication.UdpEchoClient;
import StatusHelper.LinkType;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Dialog_UdpEchoClient extends JFrame implements Dialog {
    private JPanel JPanel_main;
    private JLabel lbl_clientIndex;
    private JLabel lbl_selectedClientIndex;
    private JLabel lbl_startTime;
    private JTextField textField_startTime;
    private JLabel lbl_upTime;
    private JTextField textField_upTime;
    private JLabel lbl_mtu;
    private JTextField textField_mtu;
    private JLabel lbl_packets;
    private JTextField textField_packetCount;
    private JLabel lbl_interval;
    private JTextField textField_interval;
    private JLabel lbl_serverNode;
    private JComboBox comboBox_serverNode;
    private JLabel lbl_interface;
    private JComboBox comboBox_interface;
    private JButton btn_Save;

    public ArrayList<UdpEchoClient> clientList;
    private Dialog_Helper dialogHelper;
    private static Dialog_UdpEchoClient INSTANCE;
    private int selectedNode;
    private Dialog_Connection dialogConnection;
    private Dialog_UdpEchoServer dialogUdpEchoServer;
    private Map<Integer, ArrayList<Device>> serverInterfaces;

    public static Dialog_UdpEchoClient getInstance(int n) {
        LoggingHelper.LogInfo("Checking for available instance of Dialog_UdpEchoClient!");
        if (INSTANCE == null) {
            LoggingHelper.LogDebug("The instance for Dialog_UdpEchoClient was not available!");
            INSTANCE = new Dialog_UdpEchoClient(n);
        }
        return INSTANCE;
    }

    public Dialog_UdpEchoClient(int n) {
        LoggingHelper.Log("Creating object of type Dialog_UdpEchoClient");

        this.setContentPane(this.JPanel_main);
        this.setTitle("Udp Echo Client Configuration");
        this.setSize(500,600);
        this.setVisible(false);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        this.selectedNode = n;
        this.clientList = new ArrayList<>();
        this.dialogHelper = new Dialog_Helper(this);
        this.dialogConnection = Dialog_Connection.getInstance();
        this.dialogUdpEchoServer = Dialog_UdpEchoServer.getInstance(0);

        PlaceHolderHelper.addPlaceHolder(textField_startTime, PLACEHOLDER_START_TIME);
        PlaceHolderHelper.addPlaceHolder(textField_upTime, PLACEHOLDER_UP_TIME);
        PlaceHolderHelper.addPlaceHolder(textField_mtu, PLACEHOLDER_MTU);
        PlaceHolderHelper.addPlaceHolder(textField_interval, PLACEHOLDER_INTERVAL);
        PlaceHolderHelper.addPlaceHolder(textField_packetCount, PLACEHOLDER_TOT_PACKETS);

        btn_Save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoggingHelper.LogFunction("Trying to save the Udp Echo Client Configuration");

                if (validateInputs()) {
                    int startTime = Integer.parseInt(textField_startTime.getText());
                    int upTime = Integer.parseInt(textField_upTime.getText());
                    int mtu = Integer.parseInt(textField_mtu.getText());
                    int interval = Integer.parseInt(textField_interval.getText());
                    int packets = Integer.parseInt(textField_packetCount.getText());
                    int serverIndex = Integer.parseInt(comboBox_serverNode.getSelectedItem().toString());
                    int deviceIndex = 0;
                    UdpEchoClient clientConfig = new UdpEchoClient(selectedNode, startTime, upTime, mtu, interval, packets, serverIndex, deviceIndex);

                    clientList.add(clientConfig);
                    dialogHelper.showInformationMsg("The client added successfully!", "Success!");
                    resetAllFields();
                    setVisible(false);
                }
            }
        });
        comboBox_serverNode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showInterfaces(serverInterfaces, Integer.parseInt(comboBox_serverNode.getSelectedItem().toString()));
            }
        });
    }

    private boolean validateInputs() {
        LoggingHelper.LogFunction("Udp Echo Client Config : Validating all inputs");

        if (!textField_startTime.getText().chars().allMatch(Character::isDigit) || textField_startTime.getText().isEmpty()) {
            this.dialogHelper.showErrorMsg("Please enter valid start time!", "Error!");
            return false;
        }

        if (!textField_upTime.getText().chars().allMatch(Character::isDigit) || textField_upTime.getText().isEmpty()) {
            this.dialogHelper.showErrorMsg("Please enter a valid up time!", "Error!");
            return false;
        }

        if (!textField_mtu.getText().chars().allMatch(Character::isDigit) || textField_mtu.getText().isEmpty()) {
            this.dialogHelper.showErrorMsg("Please enter a valid MTU!", "Error!");
            return false;
        }

        if (!textField_interval.getText().chars().allMatch(Character::isDigit) || textField_interval.getText().isEmpty()) {
            this.dialogHelper.showErrorMsg("Please enter a valid interval time!", "Error!");
            return false;
        }

        if (!textField_packetCount.getText().chars().allMatch(Character::isDigit) || textField_packetCount.getText().isEmpty()) {
            this.dialogHelper.showErrorMsg("Please enter a valid packet count!", "Error!");
            return false;
        }

        return true;
    }

    private void resetAllFields() {
        this.textField_startTime.setText(PLACEHOLDER_START_TIME);
        this.textField_upTime.setText(PLACEHOLDER_UP_TIME);
        this.textField_mtu.setText(PLACEHOLDER_MTU);
        this.textField_interval.setText(PLACEHOLDER_INTERVAL);
        this.textField_packetCount.setText(PLACEHOLDER_TOT_PACKETS);
        this.comboBox_interface.removeAllItems();
        this.comboBox_serverNode.removeAllItems();
    }

    private void showInterfaces(Map<Integer, ArrayList<Device>> serverInterfaces, int index) {
        this.comboBox_interface.removeAllItems();
        for (int i=0; i<serverInterfaces.get(index).size(); i++) {
            if (serverInterfaces.get(index).get(i).linkSettings.getLinkType()==LinkType.LINK_P2P) {
                if (Objects.equals(serverInterfaces.get(index).get(i).nodeA, String.valueOf(index)) || Objects.equals(serverInterfaces.get(index).get(i).nodeB, String.valueOf(index))) {
                    this.comboBox_interface.addItem("Point to Point Link --> " + serverInterfaces.get(index).get(i).linkSettings);
                }
            } else if (serverInterfaces.get(index).get(i).linkSettings.getLinkType()==LinkType.LINK_CSMA) {
                for (int node : serverInterfaces.get(index).get(i).nodes) {
                    if (index == node) {
                        this.comboBox_interface.addItem("CSMA Link --> "+serverInterfaces.get(index).get(i).linkSettings);
                        break;
                    }
                }
            } else {
                for (int node : serverInterfaces.get(index).get(i).nodes) {
                    if (index == node) {
                        this.comboBox_interface.addItem("WiFi Link --> "+serverInterfaces.get(index).get(i).linkSettings);
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void setVisible(boolean show) {
        if (show) {
            this.serverInterfaces = new HashMap<>();
            ArrayList<Integer> activeServerList = dialogUdpEchoServer.getActiveServerList();

            for (Device device : this.dialogConnection.getAllDevices()) {
                System.out.println(device);
                if (device.linkSettings.getLinkType() == LinkType.LINK_P2P) {
                    if (Integer.parseInt(device.nodeA) == this.selectedNode) {
                       if (activeServerList.contains(Integer.parseInt(device.nodeB))) {
                           if (serverInterfaces.containsKey(Integer.parseInt(device.nodeB))) {
                               serverInterfaces.get(Integer.parseInt(device.nodeB)).add(device);
                           } else {
                               serverInterfaces.computeIfAbsent(Integer.parseInt(device.nodeB), k -> new ArrayList<>()).add(device);
                           }
                       }
                    } else if (Integer.parseInt(device.nodeB) == this.selectedNode) {
                        if (activeServerList.contains(Integer.parseInt(device.nodeA))) {
                            if (serverInterfaces.containsKey(Integer.parseInt(device.nodeA))) {
                                serverInterfaces.get(Integer.parseInt(device.nodeA)).add(device);
                            } else {
                                serverInterfaces.computeIfAbsent(Integer.parseInt(device.nodeA), k -> new ArrayList<>()).add(device);
                            }
                        }
                    }
                } else {
                    System.out.println("In other than P2P");
                    for (int i = 0; i<device.nodes.size(); i++) {
                        System.out.println("Selected Node : "+this.selectedNode+" --- Device Node : "+device.nodes.get(i)+" --- at i : "+i);
                        if (this.selectedNode == device.nodes.get(i)) {
                            if (i == 0) {
                                if (activeServerList.contains(device.nodes.get(1))) {
                                    if (serverInterfaces.containsKey(device.nodes.get(1))) {
                                        serverInterfaces.get(device.nodes.get(1)).add(device);
                                    } else {
                                        serverInterfaces.computeIfAbsent(device.nodes.get(1), k -> new ArrayList<>()).add(device);
                                    }
                                }
                            } else if (i == device.nodes.size()-1) {
                                if (activeServerList.contains(device.nodes.get(device.nodes.size()-1))) {
                                    if (serverInterfaces.containsKey(device.nodes.size()-1)) {
                                        serverInterfaces.get(device.nodes.size()-1).add(device);
                                    } else {
                                        serverInterfaces.computeIfAbsent(device.nodes.size()-1, k -> new ArrayList<>()).add(device);
                                    }
                                }
                            } else {
                                if (activeServerList.contains(device.nodes.get(i-1))) {
                                    if (serverInterfaces.containsKey(device.nodes.get(i-1))) {
                                        serverInterfaces.get(device.nodes.get(i-1)).add(device);
                                    } else {
                                        serverInterfaces.computeIfAbsent(device.nodes.get(i-1), k -> new ArrayList<>()).add(device);
                                    }
                                }

                                if (activeServerList.contains(device.nodes.get(i+1))) {
                                    if (serverInterfaces.containsKey(device.nodes.get(i+1))) {
                                        serverInterfaces.get(device.nodes.get(i+1)).add(device);
                                    } else {
                                        serverInterfaces.computeIfAbsent(device.nodes.get(i+1), k -> new ArrayList<>()).add(device);
                                    }
                                }
                            }
                        }
                    }
                }
            }

            this.comboBox_serverNode.removeAllItems();
            for (int relatedServerNode : serverInterfaces.keySet()) {
                this.comboBox_serverNode.addItem(relatedServerNode);
            }

            showInterfaces(serverInterfaces, Integer.parseInt(comboBox_serverNode.getItemAt(0).toString()));
        }
        super.setVisible(show);
    }

    public void showDialog(int n) {
        this.selectedNode = n;
        this.lbl_selectedClientIndex.setText("Selected '"+this.selectedNode+"'");
        this.setVisible(true);
    }
}
