package Ns3Objects.Links;

import StatusHelper.LinkType;

/**
 * to store the link of type YansWifiHelper in terms of NS-3 Script
 * */
public class WIFI implements NetworkLink  {

    /**
     * for unique identity
     * */
    public int id;
    /**
     * for alias name
     * */
    public String name;
    /**
     * for delay in "ms"
     * @deprecated
     * */
    private String delay;
    /**
     * for data rate
     * @deprecated
     * */
    private String dataRate;
    /**
     * for speed modifier of link
     * @deprecated
     * */
    private String speedModifier;
    /**
     * for link type
     * */
    private LinkType linkType;
    /**
     * for enabling packet capture of entire link
     * */
    private boolean enablePcap;
    /**
     * for selecting as default link
     * */
    private boolean isDefault;
    /**
     * for marking as used link
     * */
    private boolean isUsed;
    /**
     * the Wi-Fi standard
     * */
    public String standard;
    /**
     * the remote session manager for Wi-Fi
     * */
    public String remoteStationManager;
    /**
     * The ssid for this wi-fi channel
     * */
    public String ssid;

    /**
     * to create an object of type Wi-Fi
     *
     * @param id the unique identity number
     * @param name The alias name for the channel
     * @param standard the wi-fi standard
     * @param remoteStationManager the remote station manager
     * @param ssid the ssid for this channel
     * @param enablePcap for enabling the packet capture of entire link
     * @param isDefault for marking as default link
     * @param isUsed for marking as used link
     * @see WIFI#WIFI(int, String, String, String, String, boolean, boolean)
     * @see WIFI#WIFI(int, String, String, String, String, boolean)
     * @since 1.2.0
     * */
    public WIFI(int id, String name, String standard, String remoteStationManager, String ssid, boolean enablePcap, boolean isDefault, boolean isUsed) {
        this.id = id;
        this.name = name;
        this.enablePcap = enablePcap;
        this.isDefault = isDefault;
        this.isUsed = isUsed;
        this.standard = standard;
        this.remoteStationManager = remoteStationManager;
        this.ssid = ssid;
        this.linkType = LinkType.LINK_WIFI;
    }
    
    /**
     * to create an object of type Wi-Fi
     * 
     * @param id the unique identity number
     * @param name The alias name for the channel
     * @param standard the wi-fi standard
     * @param remoteStationManager the remote station manager
     * @param ssid the ssid for this channel
     * @param enablePcap for enabling the packet capture of entire link
     * @param isDefault for marking as default link
     * @see WIFI#WIFI(int, String, String, String, String, boolean, boolean, boolean)
     * @see WIFI#WIFI(int, String, String, String, String, boolean) 
     * @since 1.2.0
     * */
    public WIFI(int id, String name, String standard, String remoteStationManager, String ssid, boolean enablePcap, boolean isDefault) {
        this(id,name,standard,remoteStationManager,ssid,enablePcap,isDefault,false);
    }

    /**
     * to create an object of type Wi-Fi
     *
     * @param id the unique identity number
     * @param name The alias name for the channel
     * @param standard the wi-fi standard
     * @param remoteStationManager the remote station manager
     * @param ssid the ssid for this channel
     * @param enablePcap for enabling the packet capture of entire link
     * @see WIFI#WIFI(int, String, String, String, String, boolean, boolean, boolean)
     * @see WIFI#WIFI(int, String, String, String, String, boolean, boolean) 
     * @since 1.2.0
     * */
    public WIFI(int id, String name, String standard, String remoteStationManager, String ssid, boolean enablePcap) {
        this(id,name,standard,remoteStationManager,ssid,enablePcap,false,false);
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @deprecated
     * */
    @Override
    public String getDelay() {
        return null;
    }

    /**
     * @deprecated
     * */
    @Override
    public void setDelay(String delay) {

    }

    /**
     * @deprecated
     * */
    @Override
    public String getDataRate() {
        return null;
    }

    /**
     * @deprecated
     * */
    @Override
    public void setDataRate(String dataRate) {

    }

    /**
     * @deprecated
     * */
    @Override
    public String getSpeedModifier() {
        return null;
    }

    /**
     * @deprecated
     * */
    @Override
    public void setSpeedModifier(String speedModifier) {

    }

    @Override
    public LinkType getLinkType() {
        return this.linkType;
    }

    @Override
    public void setLinkType(LinkType linkType) {
        this.linkType = linkType;
    }

    @Override
    public boolean getEnablePcap() {
        return this.enablePcap;
    }

    @Override
    public void setEnablePcap(boolean enablePcap) {
        this.enablePcap = enablePcap;
    }

    /**
     * to generate the storage format in settings file
     *
     * @return storage format for settings file
     * @since 1.2.0
     * */    @Override
    public String forSettings() {
        return this.id+"|"+this.name+"|"+this.standard+"|"+this.remoteStationManager+"|"+this.ssid+"|"+((this.enablePcap)?("Y"):"N")+"|WIFI";
    }

    /**
     * to render the link information
     *
     * @return the link information
     * @since 0.3.0
     * */
    @Override
    public String toString() {
        return this.name+" (WIFI) - "+this.ssid;
    }

    @Override
    public boolean isDefault() {
        return this.isDefault;
    }

    @Override
    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    @Override
    public boolean isUsed() {
        return this.isUsed;
    }

    @Override
    public void setUsed(boolean isUsed) {
        this.isUsed = isUsed;
    }

    /**
     * <p>
     *     to generate the code for packet capture of entire link
     * </p>
     * <p>
     * <pre>
     * {@code
     * Example:
     * phy.EnablePcap("<name>", <devices>);
     * }
     * </pre>
     * </p>
     *
     * @return Equivalent code for generating packet capture files
     * @since 1.2.0
     */
    @Override
    public String getPacketCaptureAllCode() {
        // TODO: generate the code here
        return null;
    }

    /**
     * <p>
     *     to generate the link configuration code
     * </p>
     * <p>
     * <pre>
     * {@code
     * Example:
     *      YansWifiChannelHelper channel_name = YansWifiChannelHelper::Default();
     *      YansWifiPhyHelper phy_name;
     *      WifiMacHelper mac_name;
     *      WifiHelper wifi_name;
     *      Ssid ssid_name = Ssid("<name>");
     *      phy_name.SetChannel(channel_name.Create());
     *      wifi_name.SetStandard(<standard>);
     *      wifi_name.SetRemoteStationManager(<manager>);     *
     *  }
     *  </pre>
     * </p>
     *
     * @return the link configuration code
     * @since 1.2.0
     * */
    @Override
    public String toCode() {
        String alias_name = this.name.replace(" ","_");
        String linkConfigCode = """
                YansWifiChannelHelper channel_"""
                + alias_name +
                """
                 = YansWifiChannelHelper::Default();
                YansWifiPhyHelper phy_"""
                + alias_name +
                """
                ;
                WifiMacHelper mac_"""
                + alias_name +
                """
                ;
                WifiHelper wifi_"""
                + alias_name +
                """
                ;
                Ssid ssid_"""
                + alias_name +
                """
                 = Ssid(\""""
                + this.ssid +
                """
                \");
                phy_"""
                + alias_name +
        """
                .SetChannel(channel_"""
                + alias_name +
                """
                .Create());
                wifi_"""
                + alias_name +
                """
                .SetStandard("""
                + this.standard +
                """
                );
                wifi_"""
                + alias_name +
        """
                .SetRemoteStationManager(\""""
                + this.remoteStationManager +
                """
                \");
                """;
        return linkConfigCode;
    }
}
