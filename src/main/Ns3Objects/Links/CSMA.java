package Ns3Objects.Links;

import StatusHelper.LinkType;

public class CSMA implements NetworkLink{
    
    public int id;
    public String name;
    public String delay;
    public String dataRate;
    public String speedModifier;
    public LinkType linkType;
    public boolean enablePcap;
    public boolean isDefault;
    public boolean isUsed;

    public CSMA(int id,String name,String delay,String dataRate,String speedModifier, boolean enablePcap) {
        this.id  = id;
        this.name = name;
        this.delay = delay;
        this.dataRate = dataRate;
        this.speedModifier = speedModifier;
        this.enablePcap = enablePcap;
        this.linkType = LinkType.LINK_CSMA;
        this.isDefault = false;
        this.isUsed = false;
    }

    // new constructor will support previous once...
    public CSMA(int id,String name,String delay,String dataRate,String speedModifier, boolean enablePcap, boolean isDefault) {
        this(id,name,delay,dataRate,speedModifier,enablePcap);
        this.isDefault = isDefault;
    }

    public CSMA(int id,String name,String delay,String dataRate,String speedModifier, boolean enablePcap, boolean isDefault, boolean isUsed) {
        this(id,name,delay,dataRate,speedModifier,enablePcap,isDefault);
        this.isUsed = isUsed;
    }

    @Override
    public String toString() {    
        return this.name+" (CSMA) - "+this.delay+"ms,"+this.dataRate+this.speedModifier;
    }

    // for storing in file for settings...
    @Override
    public String forSettings() {
        return this.name+"|"+this.delay+"|"+this.dataRate+"|"+this.speedModifier+"|"+((this.enablePcap)?"Y":"N")+"|CSMA";
    }

    @Override
    public boolean isDefault() {
        return isDefault;
    }

    @Override
    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    @Override
    public boolean isUsed() {
        return isUsed;
    }

    @Override
    public void setUsed(boolean used) {
        isUsed = used;
    }

    /**
     * Example:
     *  CsmaHelper csma;
     *  csma.SetChannelAttribute("DataRate", StringValue("500Mbps"));
     *  csma.SetChannelAttribute("Delay", TimeValue(NanoSeconds(6560)));
     * */
    @Override
    public String toCode() {
        String line1, line2, line3;
        line1 = "CsmaHelper csma"+this.getName().replace(" ", "_")+";";
        line2 = "csma"+getName().replace(" ", "_")+".SetChannelAttribute(\"DataRate\",StringValue(\""+(this.dataRate+this.speedModifier)+"\"));";
        line3 = "csma"+getName().replace(" ", "_")+".SetChannelAttribute(\"Delay\",StringValue(\""+this.delay+"ms\"));";
        return line1+"\n"+line2+"\n"+line3;
    }

    /**
     * Example:
     * csmaName.EnablePcapAll("Name");
     * @return Equivalent code for ".EnablePcapAll()" method
     */
    @Override
    public String getPacketCaptureAllCode() {
        String name = getName().replace(" ","_");
        String code = "csma"+name+".EnablePcapAll(\""+name+"\");";
        return code;
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

    @Override
    public String getDelay() {
        return this.delay;
    }

    @Override
    public void setDelay(String delay) {
        this.delay = delay;
    }

    @Override
    public String getDataRate() {
        return this.dataRate;
    }

    @Override
    public void setDataRate(String dataRate) {
        this.dataRate = dataRate;
    }

    @Override
    public String getSpeedModifier() {
        return this.speedModifier;
    }

    @Override
    public void setSpeedModifier(String speedModifier) {
        this.speedModifier = speedModifier;
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
        return enablePcap;
    }

    @Override
    public void setEnablePcap(boolean enablePcap) {
        this.enablePcap = enablePcap;
    }
}
