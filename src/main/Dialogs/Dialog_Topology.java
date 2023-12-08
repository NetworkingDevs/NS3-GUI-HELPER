package Dialogs;

import Helpers.DeviceHelper;
import Helpers.LinkHelper;
import Helpers.NetworkHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;


/**
 * Program name: Dialog_Topology
 * Program date: 12th November 2023
 * Program owner: Henil Mistry
 * Contributor:
 * Last Modified: 08th December 2023
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
    ImageIcon icon = new ImageIcon("src/main/Resources/link_color.png");
    Image img = icon.getImage();
    // for storing the devices, and it's configuration...
    ArrayList<DeviceHelper> devices;
    int grid;
    int nodes;
    // for passing it to the Dialog_Device...
    ArrayList<LinkHelper> links;
    ArrayList<NetworkHelper> networks;

    public Dialog_Topology(int gridSize, ArrayList<LinkHelper> l, ArrayList<NetworkHelper> n) {

        // initializing the components...
        this.devices = new ArrayList<>();
        this.links = l;
        this.networks = n;

        // for grid calculation...
        this.nodes = gridSize;
        this.grid = (int) Math.floor(this.nodes/2);

        this.setContentPane(this.JPanel_main);
        this.JPanel_topology_btns.setLayout(new GridLayout(grid,grid*2,3,3));

        // change : added the action listener on each button which is added...
        // change made on : 08th Dec., 2023
        // for generating button...
        for(int i=0; i<gridSize; i++) {
            JButton btn = new JButton(String.valueOf(i));
            btn.setHorizontalTextPosition(SwingConstants.TRAILING);
            btn.setActionCommand(String.valueOf(i));
            btn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    openDevicesDialog(Integer.parseInt(e.getActionCommand()));
                }
            });
            this.JPanel_topology_btns.add(btn);
            Image scaledImg = img.getScaledInstance(100,100,Image.SCALE_SMOOTH);
            ImageIcon linkIcon = new ImageIcon(scaledImg);
            btn.setIcon(linkIcon);
        }

        // initializing this component...
        this.setTitle("GUI Devices Setup");
        this.setSize(500,500);
        this.setVisible(true);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        btn_setUp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                printDeviceConfig();
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

    private void AskForConfirmation() {
        // System.out.println("Nodes : "+this.nodes+" Devices : "+this.devices.size());
        if (this.nodes != this.devices.size()) {
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

    private void printDeviceConfig() {
        for (DeviceHelper device : this.devices) {
            System.out.println(device.getDeviceConfCode());
        }
    }

    // added : Added this function because 'i' cannot be accessed outside of scope...
    private void openDevicesDialog(int i) {
        // System.out.println("GRID : "+this.nodes+" i : "+i);
        Dialog_Device dialogDevice = new Dialog_Device(String.valueOf(i),String.valueOf(((i+1)%this.nodes)), "Device configuration for node "+i+" and "+((i+1)%this.nodes),this.links, this.networks, this);
    }

    /*

    // Completed on : 07th Dec. 2023
    // This is the example of Constructor for this dialog...
    public static void main(String[] args) {
         new Dialog_Topology(4); // 4 = number of nodes in ring topology
    }

     */

}
