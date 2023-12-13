package Dialogs;

import Helpers.DeviceHelper;
import Helpers.LinkHelper;
import Helpers.NetworkHelper;
import StatusHelper.TopologyStatus;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;


/**
 * Program name: Dialog_Topology
 * Program date: 12th November 2023
 * Program owner: Henil Mistry
 * Contributor:
 * Last Modified: 10th December 2023
 *
 * Purpose: This class helps for setting up the 'Topology' in GUI form.
 * */
public class Dialog_Topology extends JFrame {
    private JPanel JPanel_main;
    private JPanel JPanel_topology_btns;
    private JButton btn_setUp;
    private JScrollPane JScrollPane_btns_links;

    // for functionalities...
    // for image icon to be used on button...
    Image img;

    {
        try {
            img = ImageIO.read(getClass().getClassLoader().getResource("link_color.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    // ImageIcon icon = new ImageIcon(computer);
    // Image img = icon.getImage();

    // for storing the devices, and it's configuration...
    public ArrayList<DeviceHelper> devices;
    int grid;
    int nodes;
    // for passing it to the Dialog_Device...
    ArrayList<LinkHelper> links;
    ArrayList<NetworkHelper> networks;
    // for storing which type of topology is currently active...
    TopologyStatus TOPOLOGY;

    public Dialog_Topology(int nodes, ArrayList<LinkHelper> l, ArrayList<NetworkHelper> n, TopologyStatus status) {
        // setting the topology...
        this.TOPOLOGY = status;
        // change made on 11/12/2023
        // This line is very important...because it enables the network list to be refilled again if misplaced
        Dialog_Device.DONE_NETWORK_COLLECTION = false;

        // initializing the components...
        this.devices = new ArrayList<>();
        this.links = l;
        this.networks = n;

        // for grid calculation...
        this.nodes = nodes;
        this.grid = (int) Math.ceil((this.getButtons(this.nodes)/2));

        this.setContentPane(this.JPanel_main);
        this.JPanel_topology_btns.setLayout(new GridLayout(this.grid,this.grid,3,3));

        // change : added the action listener on each button which is added...
        // change made on : 08th Dec., 2023
        // for generating button...
        this.addButtons(this.nodes);


        // initializing this component...
        this.setTitle("GUI Devices Setup");
        this.setSize(500,500);
        this.setVisible(true);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        btn_setUp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // changed made on : 11/12/2023
                // Removed this function, it was just for debugging purpose...
                // printDeviceConfig();
                showInfoMsg();
            }
        });
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);

                AskForConfirmation();
            }
        });
    }


    public Dialog_Topology(int nodes, ArrayList<LinkHelper> l, ArrayList<NetworkHelper> n) {
        this(nodes,l,n,TopologyStatus.TOPOLOGY_RING);
    }

    // this function has been created for adding buttons according to type of topology...
    private void addButtons(int nodes) {
        int btns = this.getButtons(nodes);
        if (TOPOLOGY == TopologyStatus.TOPOLOGY_RING) {
            System.out.println("Inside Ring...");
            for(int i=0; i<btns; i++) {
                this.addButton(String.valueOf(i),String.valueOf(i));
            }
        } else if (TOPOLOGY == TopologyStatus.TOPOLOGY_MESH) {
            int count = 0;
            for (int i=0; i<=nodes-2; i++) {
                for (int j=i+1; j<=nodes-1; j++) {
                    String command = i + "-" + j;
                    this.addButton(String.valueOf(count), command);
                    count ++;
                }
            }
        } else { // expected to be star topology...
            for (int i=0; i<btns; i++) {
                this.addButton(String.valueOf(i),String.valueOf(i));
            }
        }
    }

    private void addButton(String label, String command) {
        JButton btn = new JButton(label);
        btn.setHorizontalTextPosition(SwingConstants.TRAILING);
        btn.setActionCommand(command);
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (TOPOLOGY == TopologyStatus.TOPOLOGY_MESH) {
                    String[] nodes = e.getActionCommand().split("-");
                    openDevicesDialog(Integer.parseInt(nodes[0]),Integer.parseInt(nodes[1]));
                } else {
                    openDevicesDialog(Integer.parseInt(e.getActionCommand()));
                }
            }
        });
        this.JPanel_topology_btns.add(btn);
        Image scaledImg = img.getScaledInstance(100,100,Image.SCALE_SMOOTH);
        ImageIcon linkIcon = new ImageIcon(scaledImg);
        btn.setIcon(linkIcon);
    }

    // this function will return the no. of buttons required, based on (no. of nodes, type of topology)
    private int getButtons(int nodes) {
        if (TOPOLOGY == TopologyStatus.TOPOLOGY_RING) {
          return nodes;// n links...
        } else if (TOPOLOGY == TopologyStatus.TOPOLOGY_MESH) {
            return ( ((nodes)*(nodes-1))/2 ); // (n*n-1)/2 links...
        } else { // expected to be star topology...
            return (nodes-1); // n-1 links...
        }
    }

    private void AskForConfirmation() {
        int expectedConfiguredLinks = this.getButtons(this.nodes);
        // System.out.println("Nodes : "+this.nodes+" Devices : "+this.devices.size());
        if (this.devices.size() != expectedConfiguredLinks) {
            int option = JOptionPane.showConfirmDialog(JPanel_main,"Some or All devices are left configured!\nAre you sure you want to close the topology config?","Quit!",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
            if(option == JOptionPane.YES_OPTION) {
                this.setVisible(false);
            }
        } else {
            this.setVisible(false);
        }
    }

    private void showInfoMsg() {
        if (this.nodes == this.devices.size()) {
            JOptionPane.showMessageDialog(this,"Topology has been configured successfully!", "Topology Config.", JOptionPane.INFORMATION_MESSAGE);
            this.setVisible(false);
        } else {
            AskForConfirmation();
        }
    }

    // change made on : 11/12/2023
    // Removed this function, because of 2 reasons; no needed anymore and it was just for debugging purpose...
    /*
    private void printDeviceConfig() {
        for (DeviceHelper device : this.devices) {
            System.out.println(device.getDeviceConfCode());
        }
    }
    */

    // added : Added this function because 'i' cannot be accessed outside of scope...
    private void openDevicesDialog(int i) {
        this.openDevicesDialog(i, 0);
    }

    // this variant is made, just for keeping the mesh topology in the mind...
    // this variant will only be used better when it's mesh topology...
    private void openDevicesDialog(int i,int j) {
        // System.out.println("GRID : "+this.nodes+" i : "+i);
        if (TOPOLOGY == TopologyStatus.TOPOLOGY_RING) {
            Dialog_Device dialogDevice = new Dialog_Device(String.valueOf(i),String.valueOf(((i+1)%this.nodes)), "Device configuration for node "+i+" and "+((i+1)%this.nodes),this.links, this.networks, this);
        } else if (TOPOLOGY == TopologyStatus.TOPOLOGY_MESH) {
            Dialog_Device dialogDevice = new Dialog_Device(String.valueOf(i),String.valueOf(j),"Device Configuration for node "+i+" and "+j,this.links, this.networks, this);
        } else { // expected to be star...
            // always center node will be '0'
            Dialog_Device dialogDevice = new Dialog_Device("0",String.valueOf(i+1), "Device configuration for node 0 and "+((i+1)),this.links, this.networks, this);
        }
    }

    /*

    // Completed on : 07th Dec. 2023
    // This is the example of Constructor for this dialog...
    public static void main(String[] args) {
         new Dialog_Topology(4); // 4 = number of nodes in ring topology
    }

     */

}
