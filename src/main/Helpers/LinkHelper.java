package Helpers;

public class LinkHelper {
    public int id;
    public String name;
    public String delay;
    public String dataRate;
    public String speedModifier;

    public LinkHelper(int id,String name,String delay,String dataRate,String speedModifier) {
        this.id  = id;
        this.name = name;
        this.delay = delay;
        this.dataRate = dataRate;
        this.speedModifier = speedModifier;
    }

    public String toString() {
        return this.name+" - "+this.delay+"ms,"+this.dataRate+this.speedModifier;
    }
}
