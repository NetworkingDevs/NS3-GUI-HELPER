/**
 * <p>
 *     The following package includes the storage type, for Network configuration
 *     in NS-3, it will be responsible to store the data and generate the code
 *     for Network configuration
 * </p>
 * */
package Ns3Objects.Netoworks;

/**
 * a storage type for network settings in NS-3
 * */
public class Network {
    /**
     * for unique identity
     * */
    public int id;
    /**
     * for net id of the network settings
     * */
    public String netId;
    /**
     * for netmask of the network settings
     * */
    public String netMask;
    /**
     * for name alias name
     * */
    public String name;
    /**
     * for selecting as default network settings
     * */
    public boolean isDefault;

    /**
     * to make the object of type Network
     *
     * @param id unique identity
     * @param netId net id of the network
     * @param netMask subnet mask for the network
     * @param name alias name of the network
     * @since 0.3.0
     * */
    public Network(int id, String netId, String netMask, String name) {
        this.id = id;
        this.netId = netId;
        this.netMask = netMask;
        this.name = name;
        this.isDefault = false;
    }

    /**
     * to make the object of type Network
     *
     * @param id unique identity
     * @param netId net id of the network
     * @param netMask subnet mask for the network
     * @param name alias name of the network
     * @param isDefault select as default link
     * @since 0.3.0
     * */
    public Network(int id, String netId, String netMask, String name, boolean isDefault) {
        this(id,netId,netMask,name);
        this.isDefault = isDefault;
    }

    /**
     * to render the network settings
     *
     * @return the network settings information
     * @since 0.3.0
     * */
    @Override
    public String toString() {
        return this.name+" - "+this.netId+"/"+this.netMask;
    }

    // for storing in file for settings...
    /**
     * to generate the storage format in settings file
     *
     * @return storage format for settings file
     * @since 0.3.0
     * */
    public String forSettings() {
        return this.netId+"|"+this.netMask+"|"+this.name;
    }
}
