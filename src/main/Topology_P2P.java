import FileHandler.Writer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.JavaBean;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Topology_P2P extends JFrame {
    private JPanel JPanel_main;
    private JPanel JPanel_ConfigTopology;
    private JLabel lbl_Delay;
    private JTextField textField_Delay;
    private JLabel lbl_DataRate;
    private JTextField textField_DataRate;
    private JComboBox comboBox_DataRate;
    private JPanel JPanel_ConfigServer;
    private JPanel JPanel_ConfigIP;
    private JRadioButton radioBtn_autoip;
    private JRadioButton radioBtn_manualip;
    private JLabel lbl_StartIp;
    private JTextField textField_StartIp;
    private JLabel lbl_Netmask;
    private JTextField textField_Netmask;
    private JRadioButton radioBtn_N0;
    private JRadioButton radioBtn_N1;
    private JTextField textField_portno;
    private JTextField textField_startTime;
    private JTextField textField_upTime;
    private JTextField textField_mtu;
    private JTextField textField_Interval;
    private JTextField textField_packets;
    private JLabel lbl_Port;
    private JLabel lbl_StartTime;
    private JLabel lbl_UpTime;
    private JLabel lbl_mtu;
    private JLabel lbl_Interval;
    private JLabel lbl_Packets;
    private JPanel JPanel_ConfigClient;
    private JTextField textField_startTimeClient;
    private JTextField textField_UpTimeClient;
    private JLabel lbl_startTimeClient;
    private JLabel lbl_UpTimeClient;
    private JPanel JPanel_ConfigUtilities;
    private JCheckBox checkBox_wireshark;
    private JCheckBox checkBox_NetAnim;
    private JButton btn_go;

    // Functionalities Required Below Things...
    private ArrayList<String> param = new ArrayList<>();
    private String OutputPath;
    Writer writer;

    public Topology_P2P(String path) {
        this.OutputPath = path;
        this.setContentPane(this.JPanel_main);
        this.setTitle("Topology Helper - P2P");
        this.setSize(400,800);
        this.setVisible(true);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.writer = new Writer(this.OutputPath);

        btn_go.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // validations will go here....
                // phase-1 : Validation of configuration of topology...
                ValidateTopologyConfig();
                // phase-2 : Validation of configuration of ip/subnet...
                ValidateIpSettings();
                // phase-3 : Validation of configuration of server...
                ValidateServerConfig();
                // phase-4 : Validation of configuration of client...
                ValidateClientConfig();

                // now tracking utilities...
                if(checkBox_wireshark.isSelected()) {
                    param.add("Wireshark");
                }

                if(checkBox_NetAnim.isSelected()) {
                    param.add("NetAnim");
                }

                // file handling will go here...
                CreateFile();
            }
        });

        radioBtn_manualip.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // enable fields: Start IP, Netmask
                textField_StartIp.setEnabled(true);
                textField_Netmask.setEnabled(true);
            }
        });

        radioBtn_autoip.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // disable fields: Start IP, Netmask
                textField_StartIp.setEnabled(false);
                textField_Netmask.setEnabled(false);
            }
        });
    }

    private void CreateFile() {
        this.writer = new Writer(this.OutputPath);

        String moduleString = """
                #include "ns3/netanim-module.h"
                """;
        String netanimString = """
                AnimationInterface anim("animationFirst.xml"); 
                """;
        if(!this.param.get(this.param.size()-1).equals("NetAnim")) {
            moduleString = "";
            netanimString = "";
        }

        int StopTimeServer = Integer.parseInt(this.param.get(7))+Integer.parseInt(this.param.get(8));
        int StopTimeClient = Integer.parseInt(this.param.get(12))+Integer.parseInt(this.param.get(13));
        String clientNode = (this.param.get(5).equals("0"))?"1":"0";
        String wiresharkString = """
                pointToPoint.EnablePcapAll("first");
                """;
        if(this.param.indexOf("Wireshark") < 0) {
            wiresharkString = "";
        }

        String first = """
                #include "ns3/applications-module.h"
                #include "ns3/core-module.h"
                #include "ns3/internet-module.h"
                #include "ns3/network-module.h"
                #include "ns3/point-to-point-module.h"
                """
                + moduleString +
                """                
                using namespace ns3;
                                
                NS_LOG_COMPONENT_DEFINE("FirstScriptExample");
                                
                int
                main(int argc, char* argv[])
                {
                    CommandLine cmd(__FILE__);
                    cmd.Parse(argc, argv);
                                
                    Time::SetResolution(Time::NS);
                    LogComponentEnable("UdpEchoClientApplication", LOG_LEVEL_INFO);
                    LogComponentEnable("UdpEchoServerApplication", LOG_LEVEL_INFO);
                                
                    NodeContainer nodes;
                    nodes.Create(2);
                                
                    PointToPointHelper pointToPoint;
                    pointToPoint.SetDeviceAttribute("DataRate", StringValue(\""""
                    + this.param.get(1)+this.param.get(2) +
                    """
                    \"));
                    pointToPoint.SetChannelAttribute("Delay", StringValue(\""""
                        + this.param.get(0) +
                    """
                    ms\"));
                                
                    NetDeviceContainer devices;
                    devices = pointToPoint.Install(nodes);
                                
                    InternetStackHelper stack;
                    stack.Install(nodes);
                                
                    Ipv4AddressHelper address;
                    address.SetBase(\""""
                    + this.param.get(3) +
                    """
                    \", \""""
                    + this.param.get(4) +
                    """
                    \");
                                
                    Ipv4InterfaceContainer interfaces = address.Assign(devices);
                                
                    UdpEchoServerHelper echoServer("""
                    + this.param.get(6) +
                    """
                    );
                                
                    ApplicationContainer serverApps = echoServer.Install(nodes.Get("""
                    + this.param.get(5) +
                    """
                    ));
                    serverApps.Start(Seconds("""
                    + this.param.get(7) +
                    """
                    .0));
                    serverApps.Stop(Seconds("""
                    + StopTimeServer +
                    """
                    .0));
                                
                    UdpEchoClientHelper echoClient(interfaces.GetAddress("""
                    + this.param.get(5) +
                    """
                    ), """
                    + this.param.get(6) +
                    """
                    );
                    echoClient.SetAttribute("MaxPackets", UintegerValue("""
                    + this.param.get(11) +
                    """
                    ));
                    echoClient.SetAttribute("Interval", TimeValue(Seconds("""
                    + this.param.get(10) +
                    """
                    .0)));
                    echoClient.SetAttribute("PacketSize", UintegerValue("""
                    + this.param.get(9) +
                    """
                    ));
                                
                    ApplicationContainer clientApps = echoClient.Install(nodes.Get("""
                    + clientNode +
                    """
                    ));
                    clientApps.Start(Seconds("""
                    + this.param.get(12) +
                    """
                    .0));
                    clientApps.Stop(Seconds("""
                    + StopTimeClient +
                    """
                    .0));
                    
                    """
                    + netanimString + "\n" + wiresharkString +
                    """
                                
                    Simulator::Run();
                    Simulator::Destroy();
                    return 0;
                }
                """;
        this.writer.writeToFile(first);
        this.writer.closeTheFile();
        this.param = new ArrayList<>();
    }

    private void ValidateTopologyConfig() {

        String FieldDelay = textField_Delay.getText().toString();
        String FieldDataRate = textField_DataRate.getText().toString();
        String FieldDataRateSpeedModifier = comboBox_DataRate.getSelectedItem().toString();

        // validate textField:Delay (it should be number)
        if(!FieldDelay.chars().allMatch(Character :: isDigit) || FieldDelay.length() == 0) {
            JOptionPane.showMessageDialog(JPanel_main, "Please enter valid delay!", "WARNING", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // validate textField:DataRate (it should be number)
        if(!FieldDataRate.chars().allMatch(Character::isDigit) || FieldDataRate.length() == 0) {
            JOptionPane.showMessageDialog(JPanel_main, "Please enter valid data rate!", "WARNING", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // store all the parameters for later use...
        param.add(FieldDelay);
        param.add(FieldDataRate);
        param.add(FieldDataRateSpeedModifier);
    }

    private void ValidateIpSettings() {

        String FieldStartIp = "54.0.0.1";
        String FieldNetMask = "255.0.0.0";

        if(!radioBtn_autoip.isSelected()) {
            FieldStartIp = textField_StartIp.getText().toString();
            FieldNetMask = textField_Netmask.getText().toString();
            String zeroTo255 = "(\\d{1,2}|(0|1)\\" + "d{2}|2[0-4]\\d|25[0-5])";
            String regex = zeroTo255 + "\\." + zeroTo255 + "\\." + zeroTo255 + "\\." + zeroTo255;
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(FieldStartIp);

            // validate textField:StartIP (it should be valid ip address)
            if(FieldStartIp.length() == 0 || !m.matches()) {
                JOptionPane.showMessageDialog(JPanel_main, "Please enter valid IP Address!", "WARNING", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        // store all the parameters for later use...
        param.add(FieldStartIp);
        param.add(FieldNetMask);
    }

    private void ValidateServerConfig() {

        String FieldNode = (radioBtn_N0.isSelected())?"0":"1";
        String FieldPort = textField_portno.getText().toString();
        String FieldStartTime = textField_startTime.getText().toString();
        String FieldUptime = textField_upTime.getText().toString();
        String FieldMTU = textField_mtu.getText().toString();
        String FieldInterval = textField_Interval.getText().toString();
        String FieldPackets = textField_packets.getText().toString();

        // validate textField:Port (it should -> not be empty / number)
        if(FieldPort.length() == 0 || !FieldPort.chars().allMatch(Character::isDigit)) {
            JOptionPane.showMessageDialog(JPanel_main, "Please enter valid Port Number!", "WARNING", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // validate textField:StartTime (it should -> not be empty / number)
        if(FieldStartTime.length() == 0 || !FieldStartTime.chars().allMatch(Character::isDigit)) {
            JOptionPane.showMessageDialog(JPanel_main, "Please enter valid Server Start Time!", "WARNING", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // validate textField:UpTime (it should -> not be empty / number)
        if(FieldUptime.length() == 0 || !FieldUptime.chars().allMatch(Character::isDigit)) {
            JOptionPane.showMessageDialog(JPanel_main, "Please enter valid Server Up Time!", "WARNING", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // validate textField:MTU (it should -> not be empty / number / >20 & <= 1500)
        if(FieldMTU.length() == 0 || !FieldMTU.chars().allMatch(Character::isDigit) || 20 > Integer.parseInt(FieldMTU) || Integer.parseInt(FieldMTU) > 1500) {
            JOptionPane.showMessageDialog(JPanel_main, "Please enter valid Packet Size ( 20 <= MTU <= 1500 )!", "WARNING", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // validate textField:Interval (it should -> not be empty / number)
        if(FieldInterval.length() == 0 || !FieldInterval.chars().allMatch(Character::isDigit)) {
            JOptionPane.showMessageDialog(JPanel_main, "Please enter valid Interval!", "WARNING", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // validate textField:Packets (it should -> not be empty / number / >0)
        if(FieldPackets.length() == 0 || !FieldPackets.chars().allMatch(Character::isDigit) || Integer.parseInt(FieldPackets) <= 0) {
            JOptionPane.showMessageDialog(JPanel_main, "Please enter valid No. of Packets!", "WARNING", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // store all the parameters for later use...
        param.add(FieldNode);
        param.add(FieldPort);
        param.add(FieldStartTime);
        param.add(FieldUptime);
        param.add(FieldMTU);
        param.add(FieldInterval);
        param.add(FieldPackets);
    }

    private void ValidateClientConfig() {

        String FieldStartTime = textField_startTimeClient.getText().toString();
        String FieldUpTime = textField_UpTimeClient.getText().toString();

        // validate textField:StartTime (it should -> not be empty / number)
        if(FieldStartTime.length() == 0 || !FieldStartTime.chars().allMatch(Character::isDigit)) {
            JOptionPane.showMessageDialog(JPanel_main, "Please enter valid Client Start Time!", "WARNING", JOptionPane.WARNING_MESSAGE);
        }

        // validate textField:UpTime (it should -> not be empty / number)
        if(FieldUpTime.length() == 0 || !FieldUpTime.chars().allMatch(Character::isDigit)) {
            JOptionPane.showMessageDialog(JPanel_main, "Please enter valid Client Up Time!", "WARNING", JOptionPane.WARNING_MESSAGE);
        }

        // store all the parameters for later use...
        param.add(FieldStartTime);
        param.add(FieldUpTime);
    }
}
