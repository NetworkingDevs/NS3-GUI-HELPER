package Ns3Objects.UdpEchoCommunication;

/**
 * To store and manage the Udp Echo Communication objects in NS3
 *
 * @since 1.3.0
 * */
public interface Communication {

    int getIndex();

    void setIndex(int index);

    int getPortNo();

    void setPortNo(int portNo);

    int getStartTime();

    void setStartTime(int startTime);

    int getUpTime();

    void setUpTime(int upTime);

}
