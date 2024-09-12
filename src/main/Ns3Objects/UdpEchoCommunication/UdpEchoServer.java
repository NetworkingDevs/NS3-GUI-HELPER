package Ns3Objects.UdpEchoCommunication;

/**
 * To store and manage the UdpEchoServerHelper Objects in NS3...
 *
 * @since 1.3.0
 * */
public class UdpEchoServer implements Communication {

    /**
     * For maintaining the identity of the objects...
     * */
    private static int ID_COUNT = 0;
    /**
     * ID (identity) of this object
     * */
    private int ID = -1;
    /**
     * The index of the node
     * */
    int index;
    /**
     * The port no. for the server
     * */
    int portNo;
    /**
     * start time of the server...
     * */
    int startTime;
    /**
     * total uptime of the server...
     * */
    int upTime;

    /**
     * Constructor to create the object of type UdpEchoServer...
     *
     * @since 1.3.0
     * */
    public UdpEchoServer(int index, int portNo, int startTime, int upTime) {
        this.index = index;
        this.portNo = portNo;
        this.startTime = startTime;
        this.upTime = upTime;
        this.ID = ID_COUNT;
        ID_COUNT++;
    }

    /**
     * To get the ID no. of this object...
     *
     * @since 1.3.0
     * @return ID no. of this object!
     * */
    public int getID() {
        return this.ID;
    }

    /**
     * To get the node index...
     *
     * @since 1.3.0
     * @return Integer value of the node index
     * */
    @Override
    public int getIndex() {
        return this.index;
    }

    /**
     * To set the node index...
     *
     * @param index The node index
     * @since 1.3.0
     * */
    @Override
    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * To get the port no...
     *
     * @since 1.3.0
     * @return Integer value of the port no.
     * */
    @Override
    public int getPortNo() {
        return this.portNo;
    }

    /**
     * To set the port no...
     *
     * @param portNo The port number
     * @since 1.3.0
     * */
    @Override
    public void setPortNo(int portNo) {
        this.portNo = portNo;
    }

    /**
     * To get the start time...
     *
     * @since 1.3.0
     * @return Integer value of the start time
     * */
    @Override
    public int getStartTime() {
        return this.startTime;
    }

    /**
     * To set the start time...
     *
     * @param startTime The start time
     * @since 1.3.0
     * */
    @Override
    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    /**
     * To get the uptime...
     *
     * @since 1.3.0
     * @return Integer value of the uptime
     * */
    @Override
    public int getUpTime() {
        return this.upTime;
    }

    /**
     * To set the uptime...
     *
     * @param upTime The uptime
     * @since 1.3.0
     * */
    @Override
    public void setUpTime(int upTime) {
        this.upTime = upTime;
    }

    /**
     * To get the string format for directly printing...
     *
     * @return String format information for the Server Configuration...
     * @since 1.3.0
     * */
    @Override
    public String toString() {
        return "Server Index "+this.index+" - Port:"+this.portNo;
    }
}
