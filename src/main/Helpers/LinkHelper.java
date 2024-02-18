package Helpers;

import StatusHelper.LinkType;

/**
 * Program name: LinkHelper
 * Program date: 24th October 2023
 * Program owner: Henil Mistry
 * Contributor:
 * Last Modified: 10th December 2023
 *
 * Purpose: This class will help for storing information about links used...
 * */
public class LinkHelper {
    public int id;
    public String name;
    public String delay;
    public String dataRate;
    public String speedModifier;
    public LinkType linkType;

    public LinkHelper(int id,String name,String delay,String dataRate,String speedModifier) {
        this.id  = id;
        this.name = name;
        this.delay = delay;
        this.dataRate = dataRate;
        this.speedModifier = speedModifier;
        this.linkType = linkType;
    }

    @Override
    public String toString() {
        return this.name+((this.linkType==LinkType.LINK_P2P)?("P2P"):("CSMA"))+" - "+this.delay+"ms,"+this.dataRate+this.speedModifier;
    }

    // for storing in file for settings...
    public String forSettings() {
        return ((this.linkType==LinkType.LINK_P2P)?("P2P"):("CSMA"))+"|"+this.name+"|"+this.delay+"|"+this.dataRate+"|"+this.speedModifier;
    }

    // added this on 10/12/2023 for code generation of links, and it's configuration...
    public String getLinkConfigCode() {
        if (this.linkType == LinkType.LINK_P2P) {
            return this.toP2pCode();
        } else { // assumed to be CSMA link...
            return this.toCsmaCode();
        }
    }

    /**
     * Example:
     *  PointToPointHelper pointToPoint;
     *  pointToPoint.SetDeviceAttribute("DataRate", StringValue("500Mbps"));
     *  pointToPoint.SetChannelAttribute("Delay", StringValue("2ms"));
     * */
    private String toP2pCode() {
        String line1, line2, line3;
        line1 = "PointToPointHelper p2p"+this.name+";";
        line2 = "p2p"+this.name+".SetDeviceAttribute(\"DataRate\",StringValue(\""+(this.dataRate+this.speedModifier)+"\"));";
        line3 = "p2p"+this.name+".SetChannelAttribute(\"Delay\",StringValue(\""+this.delay+"ms\"));";
        return line1+"\n"+line2+"\n"+line3;
    }

    private String toCsmaCode() {
        // TODO: Write a equivalent code for CSMA link
        return "";
    }
}
