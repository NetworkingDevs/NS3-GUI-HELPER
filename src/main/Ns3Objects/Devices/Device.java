package Ns3Objects.Devices;

/**
 * Program name: DeviceHelper
 * Program date: 07th December 2023
 * Program owner: Henil Mistry
 * Contributor:
 * Last Modified: 10th December 2023
 *
 * Purpose: This class is just a storage template for Device...
 * */

import Helpers.DebuggingHelper;
import Ns3Objects.Links.NetworkLink;
import Ns3Objects.Netoworks.Network;
import StatusHelper.LinkType;

import java.util.ArrayList;

;

public class Device {
    public int CSMA_INDEX = 0;
    public NetworkLink linkSettings;
    public Network networkSettings;
    public String nodesGroup;
    public String nodeA,nodeB;
    public ArrayList<Integer> nodes;

    public Device(NetworkLink linkSettings, Network networkSettings, ArrayList<Integer> nodes, int csma_index) {
        this.linkSettings = linkSettings;
        this.networkSettings = networkSettings;
        this.nodes = new ArrayList<>();
        this.nodes.addAll(nodes);
        this.nodesGroup = String.valueOf(csma_index);
        this.CSMA_INDEX = csma_index;
    }

    public Device(NetworkLink linkSettings, Network networkSettings, String nodeA, String nodeB) {
        this.linkSettings = linkSettings;
        this.networkSettings = networkSettings;
        this.nodeA = nodeA;
        this.nodeB = nodeB;
        this.nodesGroup = nodeA+nodeB;
    }

    /**
     * Example:
     *  devices01 = pointToPoint.Install(nodes01); (For P2P Link)
     *
     *  csmaDevices0 = csmaLink.Install(csmaNodes0); (For CSMA Link)
     * */
    public String getDeviceConfCode() {
        if(this.linkSettings.getLinkType() == LinkType.LINK_CSMA) {
            return "\ncsmaDevices"+CSMA_INDEX+" = csma"+this.linkSettings.getName()+".Install(csmaNodes"+CSMA_INDEX+");";
        } else { // assumed to be point to point link...
            return "\ndevices"+this.nodesGroup+" = p2p"+this.linkSettings.getName()+".Install(nodes"+this.nodesGroup+");";
        }
    }

    /**
     * Example:
     *  address.SetBase("54.0.0.0", "255.0.0.0");
     *  Ipv4InterfaceContainer interfaces01 = address.Assign(devices01);
     *
     *  (For CSMA)
     *  address.SetBase("54.0.0.0","255.0.0.0");
     *  Ipv4InterfaceContainer csmaInterface0 = address.Assign(csmaDevices0);
     * */
    public String getIPConfCode() {
        String firstLine = "", secondLine = "";
        if (this.linkSettings.getLinkType() == LinkType.LINK_CSMA) {
            firstLine = "address.SetBase(\""+this.networkSettings.netId+"\",\""+this.networkSettings.netMask+"\");";
            secondLine = "Ipv4InterfaceContainer csmaInterfaces"+CSMA_INDEX+" = "+"address.Assign(csmaDevices"+CSMA_INDEX+");";
        } else {
            firstLine = "address.SetBase(\""+this.networkSettings.netId+"\",\""+this.networkSettings.netMask+"\");";
            secondLine = "Ipv4InterfaceContainer interfaces"+this.nodesGroup+" = "+"address.Assign(devices"+this.nodesGroup+");";
        }
        return "\n"+firstLine+"\n"+secondLine+"\n";
    }

    /**
     * Example:
     *  nodes01
     *
     *  (For CSMA)
     *  csmaNodes0
     * */
    public String getNodesGroup() {
        if (this.linkSettings.getLinkType()==LinkType.LINK_CSMA) {
            DebuggingHelper.Debugln("Generating nodes group for csma nodes...");
            return "csmaNodes"+CSMA_INDEX;
        } else { // assumed to be point to point...
            return "nodes"+this.nodesGroup;
        }
    }

    /**
     * Example:
     *  devices01
     *
     *  (For CSMA)
     *  csmaDevices0
     * */
    public String getDevicesGroup() {
        if (this.linkSettings.getLinkType()==LinkType.LINK_CSMA) {
            return " csmaDevices"+CSMA_INDEX;
        } else { // assumed to be point to point...
            return " devices"+this.nodesGroup;
        }
    }

    /**
     * Example:
     *  nodes01.Add(allNodes.Get(0));
     *  nodes01.Add(allNodes.Get(1));
     *
     *  (For CSMA)
     *  csmaNodes0.Add(allNodes.Get(0));
     *  csmaNodes0.Add(allNodes.Get(1));
     *  ......(variable)
     * */
    public String getNodesGroupCode() {
        String line1 = "", line2 = "";
        DebuggingHelper.Debugln("Link Info from Device : "+this.linkSettings.toString());
        if (this.linkSettings.getLinkType()==LinkType.LINK_CSMA) {
            for(int i : this.nodes) {
                line1 = line1.concat("\ncsmaNodes"+this.CSMA_INDEX+".Add(allNodes.Get("+i+"));");
            }
        } else { // assumed to be point to point...
            line1 = this.getNodesGroup()+".Add(allNodes.Get("+this.nodeA+"));";
            line2 = this.getNodesGroup()+".Add(allNodes.Get("+this.nodeB+"));";
        }
        return line1+"\n"+line2;
    }

    @Override
    public String toString() {
        return "Device connecting : "+nodeA+" and "+nodeB;
    }
}
