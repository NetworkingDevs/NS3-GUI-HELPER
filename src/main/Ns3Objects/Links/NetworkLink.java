/**
 * <p>
 *     The following package includes the storage type for the various
 *     network links, point-to-point, common bus, etc. which represent
 *     PointToPointHelper, CsmaHelper in NS-3 Scripts
 *
 *     They will be help in storing information and code generation
 * </p>
 * */
package Ns3Objects.Links;

import StatusHelper.LinkType;

/**
 * to store and manage network links objects in NS-3
 * */
public interface NetworkLink {

    /**
     * to map the index with the type of the P2P link
     * */
    int CHOICE_INDEX_P2P = 0;
    /**
     * to map the index with the type of the CSMA link
     * */
    int CHOICE_INDEX_CSMA = 1;

    /**
     * label for rendering P2P link
     * */
    String LABEL_P2P = "P2P";
    /**
     * label for rendering the CSMA link
     * */
    String LABEL_CSMA = "CSMA";

    public int getId();

    public void setId(int id);

    public String getName();

    public void setName(String name);

    public String getDelay();

    public void setDelay(String delay);

    public String getDataRate();

    public void setDataRate(String dataRate);

    public String getSpeedModifier();

    public void setSpeedModifier(String speedModifier);

    public LinkType getLinkType();

    public void setLinkType(LinkType linkType);

    public boolean getEnablePcap();

    public void setEnablePcap(boolean enablePcap);

    // for storing in file for settings...
    public String forSettings();

    boolean isDefault();

    void setDefault(boolean isDefault);

    boolean isUsed();

    void setUsed(boolean isUsed);

    public String getPacketCaptureAllCode();

    /**
     * <p>
     *     to generate the link configuration code
     * </p>
     * <p>
     *     <pre>
     *         {@code
     * Example:
     *  PointToPointHelper pointToPoint;
     *  pointToPoint.SetDeviceAttribute("DataRate", StringValue("500Mbps"));
     *  pointToPoint.SetChannelAttribute("Delay", StringValue("2ms"));
     *  }
     *  </pre>
     *  </p>
     *
     * @return the link configuration code for the link
     * @since 1.1.0
     * */
    public String toCode();

    /**
     * to get the Link type from the provided choice
     *
     * @see LinkType
     * @return Link type
     * @since 1.1.0
     * */
    static LinkType getLinkType(int choiceIndex) {
        if(choiceIndex == CHOICE_INDEX_P2P) {
            return LinkType.LINK_P2P;
        } else if (choiceIndex == CHOICE_INDEX_CSMA){
            return LinkType.LINK_CSMA;
        } else { // default case...
            return LinkType.LINK_P2P;
        }
    }
}
