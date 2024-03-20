package FileHandler;

import Dialogs.*;
import Ns3Objects.Devices.Device;
import Helpers.LoggingHelper;
import Ns3Objects.Links.NetworkLink;
import StatusHelper.LinkType;

import java.util.ArrayList;
import java.util.Map;

/**
 * This class will be responsible for generating the code
 * for the equivalent topology
 * */
public class CodeGenerator {

    // map fields can be declared here....
    /**
     * a key value for netanim utility
     * */
    public static final String UTILITY_NETANIM = "netanim";
    /**
     * a key value for the wireshark utility
     * */
    public static final String UTILITY_WIRESHARK = "wireshark";
    /**
     * a key value for the name of the topology
     * */
    public static final String NAME_OF_TOPOLOGY = "custom";
    /**
     * key value for total nodes
     * */
    public static final String TOTAL_NODES = "nodes";
    /**
     * a key value for reading the true value representing
     * */
    public static final String VALUE_TRUE = "t";
    /**
     * a key value for reading the false value representing
     * */
    public static final String VALUE_FALSE = "f";

    /**
     * server configuration dialog box
     * */
    private Dialog_ConfigureServer dialogConfigureServer;
    /**
     * client configuration dialog box
     * */
    private Dialog_ConfigureClient dialogConfigureClient;
    /**
     * dialog for connection
     * */
    private Dialog_Connection dialogConnection;
    /**
     * dialog for link
     * */
    private Dialog_Link dialogLink;
    /**
     * dialog for wi-fi links
     */
    private Dialog_WiFiLink dialogWiFiLink;
    /**
     * map to filter out some extra fields
     * */
    private Map<String, String> otherFields;
    /**
     * final code
     * */
    protected String code;

    /**
     * to make an object of type CodeGenerator
     *
     * @param s server configuration dialog
     * @param c client configuration dialog
     * @param conn connection dialog
     * @param link link dialog
     * @param other map of extra fileds
     * @since 0.3.0
     * */
    public CodeGenerator(Dialog_ConfigureServer s, Dialog_ConfigureClient c, Dialog_Connection conn, Dialog_Link link, Dialog_WiFiLink dialogWiFiLink, Map<String, String> other) {
        LoggingHelper.Log("Creating object of type CodeGenerator");
        this.dialogConfigureServer = s;
        this.dialogConfigureClient = c;
        this.dialogConnection = conn;
        this.dialogLink = link;
        this.dialogWiFiLink = dialogWiFiLink;
        this.otherFields = other;
    }

    /**
     * This function is used for generating code for the topology
     * */
    public void GenerateCode() {
        LoggingHelper.LogFunction("Generating code!");
        // some variable parameters configuration ======================================================================
        String topology = this.otherFields.get(NAME_OF_TOPOLOGY);

        String netAnimModuleString = """
                #include "ns3/netanim-module.h"
                """;
        String netanimUtilityString = """
                AnimationInterface anim(\"animation"""+topology+"""
                .xml\"); 
                """;
        StringBuilder wiresharkUtilityString = new StringBuilder();
        int StopTimeServer = this.dialogConfigureServer.getStopTime();
        int StopTimeClient = this.dialogConfigureClient.getStopTime();
        if (this.otherFields.get(UTILITY_WIRESHARK).compareToIgnoreCase(VALUE_TRUE) == 0) {
            LoggingHelper.LogLogic("CodeGenerator : generating pcap for entire link which has enabled pcap and it is used!");
            for(NetworkLink l : dialogLink.links) {
                if (l.isUsed() && l.getEnablePcap()) {
                    wiresharkUtilityString.append(l.getPacketCaptureAllCode()+"\n");
                }
            }
        }
        if (this.otherFields.get(UTILITY_NETANIM).compareToIgnoreCase(VALUE_FALSE) == 0) {
            LoggingHelper.LogLogic("CodeGenerator : discarded the include for netanim module because utility is not selected!");
            netAnimModuleString = "";
            netanimUtilityString = "";
        }
        String nodesGrp = new String();
        String nodesGrpCode = new String();
        String linkConfigCode = new String();
        String devicesGrp = new String();
        String deviceConfigCode = new String();
        String ipConfigCode = new String();
        String primaryServerGrp = new String();
        String serverPrimaryIndex = new String();
        boolean serverPrimaryConfigured = false;

        LoggingHelper.LogDebug("Devices : "+this.dialogConnection.devices.size()+" CSMA Devices : "+this.dialogConnection.devices_csma.size());
        LoggingHelper.LogLogic("CodeGenerator : Generating nodes group!");
        for(Device device : this.dialogConnection.devices) {
            nodesGrp = nodesGrp.concat(device.getNodesGroup()+",");
            if (!serverPrimaryConfigured) {
                if (device.nodeA.compareToIgnoreCase(this.dialogConfigureServer.getServerIndex())==0) {
                    LoggingHelper.LogLogic("CodeGenerator : primary server index and primary server group is found!");
                    primaryServerGrp = "interfaces"+device.nodesGroup;
                    serverPrimaryIndex = "0";
                    serverPrimaryConfigured = true;
                } else if (device.nodeB.compareToIgnoreCase(this.dialogConfigureServer.getServerIndex()) == 0) {
                    LoggingHelper.LogLogic("CodeGenerator : primary server index and primary server group is found!");
                    primaryServerGrp = "interfaces"+device.nodesGroup;
                    serverPrimaryIndex = "1";
                    serverPrimaryConfigured = true;
                }
                // System.out.println("Primary Index : "+serverPrimaryIndex+" Primary Group : "+primaryServerGrp);
            }
        }

        for (Device device : this.dialogConnection.devices_csma) {
            nodesGrp = nodesGrp.concat(device.getNodesGroup()+",");
            if (!serverPrimaryConfigured) {
                ArrayList<Integer> nodes = new ArrayList<>();
                nodes.addAll(device.nodes);
                for (int j=0; j<nodes.size(); j++) {
                    int i = nodes.get(j);
                    if (String.valueOf(i).compareToIgnoreCase(this.dialogConfigureServer.getServerIndex())==0) {
                        LoggingHelper.LogLogic("CodeGenerator : primary server index and primary server group is found!");
                        if (device.linkSettings.getLinkType() == LinkType.LINK_CSMA) {
                            primaryServerGrp = "csmaInterfaces"+device.CSMA_INDEX;
                            serverPrimaryIndex = String.valueOf(j);
                        } else { // assumed to be wi-fi
                            if (device.nodes.get(0).equals(String.valueOf(j))) {
                                primaryServerGrp = "interfacesAp_"+device.linkSettings.getName();
                                serverPrimaryIndex = String.valueOf(j);
                            } else {
                                primaryServerGrp = "interfacesSta_"+device.linkSettings.getName();
                                serverPrimaryIndex = String.valueOf(j-1);
                            }
                        }
                        serverPrimaryConfigured = true;
                    }
                }
            }
        }
        nodesGrp = nodesGrp.substring(0,nodesGrp.length()-1);

        LoggingHelper.LogLogic("CodeGenerator : generating code for nodes group!");
        for (Device device : this.dialogConnection.devices) {
            nodesGrpCode = nodesGrpCode.concat(device.getNodesGroupCode()+"\n");
        }
        for (Device device : this.dialogConnection.devices_csma) {
            nodesGrpCode = nodesGrpCode.concat(device.getNodesGroupCode()+"\n");
        }

        LoggingHelper.LogLogic("CodeGenerator : generating link configuration code for used links!");
        for (NetworkLink link : this.dialogLink.links) {
            if (link.isUsed()) {
                linkConfigCode = linkConfigCode.concat(link.toCode()+"\n");
            }
        }
        for (NetworkLink link : this.dialogWiFiLink.links) {
            if (link.isUsed()) {
                linkConfigCode = linkConfigCode.concat(link.toCode()+"\n");
            }
        }

        LoggingHelper.LogLogic("CodeGenerator : generating device group!");
        for (Device device : this.dialogConnection.devices) {
            devicesGrp = devicesGrp.concat(device.getDevicesGroup()+",");
        }
        for (Device device : this.dialogConnection.devices_csma) {
            if (device.linkSettings.getLinkType()!=LinkType.LINK_WIFI) {
                devicesGrp = devicesGrp.concat(device.getDevicesGroup()+",");
            }
        }
        devicesGrp = devicesGrp.substring(0,devicesGrp.length()-1);
        if (devicesGrp.trim().length() > 0) {
            devicesGrp = "NetDeviceContainer "+devicesGrp+";";
        }

        LoggingHelper.LogLogic("CodeGenerator : generating device configuration code!");
        for (Device device : this.dialogConnection.devices) {
            deviceConfigCode = deviceConfigCode.concat(device.getDeviceConfCode());
        }
        for (Device device : this.dialogConnection.devices_csma) {
            deviceConfigCode = deviceConfigCode.concat(device.getDeviceConfCode());
        }

        LoggingHelper.LogLogic("CodeGenerator : generating ip configuration code!");
        for (Device device : this.dialogConnection.devices) {
            ipConfigCode = ipConfigCode.concat(device.getIPConfCode()+"\n");
        }
        for (Device device : this.dialogConnection.devices_csma) {
            ipConfigCode = ipConfigCode.concat(device.getIPConfCode()+"\n");
        }
        // variable parameters configuration ends ======================================================================

        LoggingHelper.LogLogic("CodeGenerator : generating full code via concatenation!");
        this.code = """
                #include "ns3/applications-module.h"
                #include "ns3/core-module.h"
                #include "ns3/internet-module.h"
                #include "ns3/network-module.h"
                #include "ns3/point-to-point-module.h"
                #include "ns3/csma-module.h"
                #include "ns3/mobility-module.h"
                #include "ns3/ssid.h"
                #include "ns3/yans-wifi-helper.h"
                #include "ns3/wifi-standards.h"
                """
                + netAnimModuleString +
                """
                                
                using namespace ns3;
                                
                NS_LOG_COMPONENT_DEFINE(\""""+topology+"""
                Example\");
                                
                int
                main(int argc, char* argv[])
                {
                    CommandLine cmd(__FILE__);
                    cmd.Parse(argc, argv);
                                
                    Time::SetResolution(Time::NS);
                    LogComponentEnable("UdpEchoClientApplication", LOG_LEVEL_INFO);
                    LogComponentEnable("UdpEchoServerApplication", LOG_LEVEL_INFO);
                   \s
                    // step-1 = creating group of nodes....
                    NodeContainer allNodes,"""
                + nodesGrp +
                """
                ;
                allNodes.Create("""
                + this.otherFields.get(TOTAL_NODES) +
                """
                );
                
                """
                + nodesGrpCode +
                """
                            
                // step-2 = create link
                """
                + linkConfigCode +
                """
               \s
                // step-3 = creating devices
                """
                + devicesGrp +
                """
                """
                + deviceConfigCode +
                """
                
                MobilityHelper mobility;
                mobility.SetMobilityModel("ns3::ConstantPositionMobilityModel");
                mobility.Install(allNodes);
                            
                // step-4 = Install ip stack
                InternetStackHelper stack;
                stack.Install(allNodes);
               
                // step-5 = Assignment of IP Address
                Ipv4AddressHelper address;
               
                """
                + ipConfigCode +
                """
              
                // step-6 = server configuration
                UdpEchoServerHelper echoServer("""
                + this.dialogConfigureServer.getPortNumber() +"""
                    );
                                
                    ApplicationContainer serverApps = echoServer.Install(allNodes.Get("""
                + this.dialogConfigureServer.getServerIndex() + """
                    ));
                    serverApps.Start(Seconds("""+ this.dialogConfigureServer.getStartTime()  +"""
                    .0));
                    serverApps.Stop(Seconds("""+ StopTimeServer +"""
                    .0));
                   
                    // step-7 = client configuration
                    UdpEchoClientHelper echoClient("""+ primaryServerGrp +"""
                    .GetAddress("""+ serverPrimaryIndex + """
                ),"""+ this.dialogConfigureServer.getPortNumber() +"""
                    );
                    echoClient.SetAttribute("MaxPackets", UintegerValue("""+ this.dialogConfigureClient.getPackets() +"""
                    ));
                    echoClient.SetAttribute("Interval", TimeValue(Seconds("""+ this.dialogConfigureClient.getInterval() +"""
                    .0)));
                    echoClient.SetAttribute("PacketSize", UintegerValue("""+ this.dialogConfigureClient.getMTU() +"""
                    ));
                                
                    ApplicationContainer clientApps = echoClient.Install(allNodes.Get("""+ this.dialogConfigureClient.getClientIndex() +"""
                    ));
                    clientApps.Start(Seconds("""+ this.dialogConfigureClient.getStartTime() +"""
                    .0));
                    clientApps.Stop(Seconds("""+ StopTimeClient +"""
                    .0));
                   
                    Ipv4GlobalRoutingHelper::PopulateRoutingTables();
                    """
                + netanimUtilityString + "\n" + wiresharkUtilityString +
                """
               
                            
                Simulator::Run();
                Simulator::Destroy();
                return 0;
            }
            """;
    }

    public String getCode() {
        LoggingHelper.LogFunction("CodeGenerator : get code called!");
        return this.code;
    }

}
