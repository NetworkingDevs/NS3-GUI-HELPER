package Netowkrs;

public class Network {
    public int id;
    public String netId;
    public String netMask;
    public String name;
    public boolean isDefault;

    public Network(int id, String netId, String netMask, String name) {
        this.id = id;
        this.netId = netId;
        this.netMask = netMask;
        this.name = name;
        this.isDefault = false;
    }

    public Network(int id, String netId, String netMask, String name, boolean isDefault) {
        this(id,netId,netMask,name);
        this.isDefault = isDefault;
    }

    @Override
    public String toString() {
        return this.name+" - "+this.netId+"/"+this.netMask;
    }

    // for storing in file for settings...
    public String forSettings() {
        return this.netId+"|"+this.netMask+"|"+this.name;
    }
}
