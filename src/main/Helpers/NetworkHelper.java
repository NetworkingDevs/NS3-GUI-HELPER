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

    public String toString() {
        return this.name+" - "+this.netId+"/"+this.netMask;
    }
}
