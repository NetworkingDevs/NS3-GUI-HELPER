package Helpers;

/**
 * Program name: DeviceHelper
 * Program date: 07th December 2023
 * Program owner: Henil Mistry
 * Contributor:
 * Last Modified: 10th December 2023
 *
 * Purpose: This class is just a storage template for Device...
 * */
public class DeviceHelper {
    public LinkHelper linkSettings;
    public NetworkHelper networkSettings;
    public String nodesGroup;
    public String nodeA,nodeB;

    public DeviceHelper(LinkHelper linkSettings, NetworkHelper networkSettings, String nodesGroup) {
        this.linkSettings = linkSettings;
        this.networkSettings = networkSettings;
        this.nodesGroup = nodesGroup;
    }

    public DeviceHelper(LinkHelper linkSettings, NetworkHelper networkSettings, String nodeA, String nodeB) {
        this.linkSettings = linkSettings;
        this.networkSettings = networkSettings;
        this.nodeA = nodeA;
        this.nodeB = nodeB;
        this.nodesGroup = nodeA+nodeB;
    }

    /**
     * Example:
     *  devices01 = pointToPoint.Install(nodes01);
     * */
    public String getDeviceConfCode() {
        return "\ndevices"+this.nodesGroup+" = p2p"+this.linkSettings.name+".Install(nodes"+this.nodesGroup+");";
    }

    /**
     * Example:
     *  address.SetBase("54.0.0.0", "255.0.0.0");
     *  Ipv4InterfaceContainer interfaces01 = address.Assign(devices01);
     * */
    public String getIPConfCode() {
        String firstLine = "address.SetBase(\""+this.networkSettings.netId+"\",\""+this.networkSettings.netMask+"\");";
        String secondLine = "Ipv4InterfaceContainer interfaces"+this.nodesGroup+" = "+"address.Assign(devices"+this.nodesGroup+");";
        return "\n"+firstLine+"\n"+secondLine+"\n";
    }

    /**
     * Example:
     *  nodes01
     * */
    public String getNodesGroup() {
        return "nodes"+this.nodesGroup;
    }

    /**
     * Example:
     *  devices01
     * */
    public String getDevicesGroup() {
        return " devices"+this.nodesGroup;
    }

    /**
     * Example:
     *  nodes01.Add(allNodes.Get(0));
     *  nodes01.Add(allNodes.Get(1));
     * */
    public String getNodesGroupCode() {
        String line1 = this.getNodesGroup()+".Add(allNodes.Get("+this.nodeA+"));";
        String line2 = this.getNodesGroup()+".Add(allNodes.Get("+this.nodeB+"));";
        return line1+"\n"+line2;
    }

    @Override
    public String toString() {
        return "Device connecting : "+nodeA+" and "+nodeB;
    }
}
