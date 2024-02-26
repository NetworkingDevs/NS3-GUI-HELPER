package Ns3Objects.Links;

import StatusHelper.LinkType;

public interface NetworkLink {

    int CHOICE_INDEX_P2P = 0;
    int CHOICE_INDEX_CSMA = 1;

    String LABEL_P2P = "P2P";
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
     * Example:
     *  PointToPointHelper pointToPoint;
     *  pointToPoint.SetDeviceAttribute("DataRate", StringValue("500Mbps"));
     *  pointToPoint.SetChannelAttribute("Delay", StringValue("2ms"));
     * */
    public String toCode();

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
