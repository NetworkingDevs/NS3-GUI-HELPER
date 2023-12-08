package Helpers;

/**
 * Program name: DeviceHelper
 * Program date: 07th December 2023
 * Program owner: Henil Mistry
 * Contributor:
 * Last Modified: 07th December 2023
 *
 * Purpose: This class is just a storage template for Device...
 * */
public class DeviceHelper {
    public LinkHelper linkSettings;
    public NetworkHelper networkSettings;
    public String nodesGroup;

    public DeviceHelper(LinkHelper linkSettings, NetworkHelper networkSettings, String nodesGroup) {
        this.linkSettings = linkSettings;
        this.networkSettings = networkSettings;
        this.nodesGroup = nodesGroup;
    }

    /**
     * Example:
     *  devices01 = pointToPoint.Install(nodes01);
     * */
    public String getDeviceConfCode() {
        return "\ndevices"+this.nodesGroup+" = "+this.linkSettings.name+".Install(nodes"+this.nodesGroup+");";
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
}
