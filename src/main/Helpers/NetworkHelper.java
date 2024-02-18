package Helpers;

public class NetworkHelper {
    public int id;
    public String netId;
    public String netMask;
    public String name;

    public NetworkHelper(int id, String netId, String netMask, String name) {
        this.id = id;
        this.netId = netId;
        this.netMask = netMask;
        this.name = name;
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
