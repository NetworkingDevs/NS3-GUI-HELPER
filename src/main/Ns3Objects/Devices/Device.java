/**
 * <p>
 *     The following package includes the storage type, for NetDevices objects
 *     in NS-3, it will be responsible to store the data and enerate the code
 *     for NetDevices class.
 * </p>
 * */
package Ns3Objects.Devices;

import Helpers.DebuggingHelper;
import Ns3Objects.Links.NetworkLink;
import Ns3Objects.Netoworks.Network;
import StatusHelper.LinkType;

import java.util.ArrayList;

/**
 * A class to store information of a Device in NS-3
 * */
public class Device {
    /**
     * to identification of each CSMA devices
     * */
    public int CSMA_INDEX = 0;
    /**
     * for storing the links settings between the nodes in the device
     * */
    public NetworkLink linkSettings;
    /**
     * for storing the network configuration between the nodes in the device
     * */
    public Network networkSettings;
    /**
     * for storing the group of nodes
     * */
    public String nodesGroup;
    /**
     * for storing the index of first two nodes in case of link type is wired point-to-point
     * */
    public String nodeA,nodeB;
    /**
     * to store the nodes index in case of csma link
     * */
    public ArrayList<Integer> nodes;

    /**
     * to instantiate the object of type Device
     *
     * @param linkSettings the link settings
     * @param networkSettings the network settings
     * @param nodes the list of nodes, if type is CSMA link
     * @param csma_index the unique number
     * @since 0.3.0
     * */
    public Device(NetworkLink linkSettings, Network networkSettings, ArrayList<Integer> nodes, int csma_index) {
        this.linkSettings = linkSettings;
        this.networkSettings = networkSettings;
        this.nodes = new ArrayList<>();
        this.nodes.addAll(nodes);
        this.nodesGroup = String.valueOf(csma_index);
        this.CSMA_INDEX = csma_index;
    }

    /**
     * to instantiate the object of type Device, in case of wired point-to-point link
     *
     * @param linkSettings the link settings
     * @param networkSettings the network settings
     * @param nodeA the index of first node
     * @param nodeB the index of second node
     * @since 0.3.0
     * */
    public Device(NetworkLink linkSettings, Network networkSettings, String nodeA, String nodeB) {
        this.linkSettings = linkSettings;
        this.networkSettings = networkSettings;
        this.nodeA = nodeA;
        this.nodeB = nodeB;
        this.nodesGroup = nodeA+nodeB;
    }

    /**
     * <p>
     *     To generate the device configuration code
     * </p>
     * <p>
     * Example:
     * <pre>{@code
     *      devices01 = pointToPoint.Install(nodes01); // (For P2P Link)
     *      csmaDevices0 = csmaLink.Install(csmaNodes0); // (For CSMA Link)
     *  }
     *  </pre>
     * </p>
     *
     * @return the device configuration code for NS-3 Script
     * @since 0.3.0
     * */
    public String getDeviceConfCode() {
        if(this.linkSettings.getLinkType() == LinkType.LINK_CSMA) {
            return "\ncsmaDevices"+CSMA_INDEX+" = csma"+this.linkSettings.getName()+".Install(csmaNodes"+CSMA_INDEX+");";
        } else { // assumed to be point to point link...
            return "\ndevices"+this.nodesGroup+" = p2p"+this.linkSettings.getName()+".Install(nodes"+this.nodesGroup+");";
        }
    }

    /**
     * <p>
     *     To generate the network configuration code for the nodes in device
     * </p>
     * <p>
     *     <pre>
     *         {@code
     * Example:
     *  address.SetBase("54.0.0.0", "255.0.0.0");
     *  Ipv4InterfaceContainer interfaces01 = address.Assign(devices01);
     *
     *  // (For CSMA)
     *  address.SetBase("54.0.0.0","255.0.0.0");
     *  Ipv4InterfaceContainer csmaInterface0 = address.Assign(csmaDevices0);
     *      }
     *      </pre>
     *  </p>
     *
     * @return the IP configuration code for the NS-3 Script
     * @since 0.3.0
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
     * <p>
     *     to get the alias for the node group, used in NodeContainer
     * </p>
     * <p>
     *      <pre>
     *          {@code
     *              Example:
     *                  nodes01
     *
     *                  // (For CSMA)
     *                  csmaNodes0
     *          }
     *      </pre>
     * </p>
     *
     * @return the alias for the nodes group
     * @since 0.3.0
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
     * <p>
     *     to get the device group for NetDeviceContainer in NS-3 Script
     * </p>
     * <p>
     *     <pre>
     *         {@code
     * Example:
     *  devices01
     *
     *  // (For CSMA)
     *  csmaDevices0
     *  }
     *      </pre>
     *  </p>
     * @return the alias for the device group in NS-3 Script
     * @since 0.3.0
     * */
    public String getDevicesGroup() {
        if (this.linkSettings.getLinkType()==LinkType.LINK_CSMA) {
            return " csmaDevices"+CSMA_INDEX;
        } else { // assumed to be point to point...
            return " devices"+this.nodesGroup;
        }
    }

    /**
     * <p>
     *     to get the code for node group
     * </p>
     * <p>
     *     <pre>
     *         {@code
     * Example:
     *  nodes01.Add(allNodes.Get(0));
     *  nodes01.Add(allNodes.Get(1));
     *
     *  // (For CSMA)
     *  csmaNodes0.Add(allNodes.Get(0));
     *  csmaNodes0.Add(allNodes.Get(1));
     *  // ......(variable)
     *  }
     *  </pre>
     *  </p>
     * @return the nodes groups code in NS-3
     * @since 0.3.0
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

    /**
     * to get the information out of devices
     *
     * @return information string of device
     * */
    @Override
    public String toString() {
        return "Device connecting : "+nodeA+" and "+nodeB;
    }
}
