package Links;

import StatusHelper.LinkType;

public interface NetworkLink {
    
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

    // for storing in file for settings...
    public String forSettings();

    /**
     * Example:
     *  PointToPointHelper pointToPoint;
     *  pointToPoint.SetDeviceAttribute("DataRate", StringValue("500Mbps"));
     *  pointToPoint.SetChannelAttribute("Delay", StringValue("2ms"));
     * */
    public String toCode();

}
