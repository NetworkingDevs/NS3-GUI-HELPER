package Dialogs;

import GuiHelpers.NodeHelper;
import GuiHelpers.P2PLinkHelper;
import GuiHelpers.TopologyPainter;
import Helpers.LinkHelper;
import Helpers.NetworkHelper;
import StatusHelper.TopologyStatus;

import javax.swing.*;
import java.util.ArrayList;

public class Dialog_TopologyGUI extends JFrame {
    private JPanel JPanel_main;
    private JScrollPane JScrollPane_main;
    private JButton btn_setUpTopology;
    private JPanel JPanel_drawingArea;

    // for serving the functionalities...
    public TopologyPainter painter;
    int totalNodes;
    ArrayList<LinkHelper> links;
    ArrayList<NetworkHelper> networks;
    TopologyStatus topologyStatus;
    ArrayList<NodeHelper> GUInodes;
    ArrayList<P2PLinkHelper> GUIlinks;

    public Dialog_TopologyGUI(int nodes, ArrayList<LinkHelper> l, ArrayList<NetworkHelper> n, TopologyStatus status) {
        this.GUInodes = new ArrayList<>();
        this.GUIlinks = new ArrayList<>();
        this.totalNodes = nodes;
        this.links = new ArrayList<>();
        this.links.addAll(l);
        this.networks = new ArrayList<>();
        this.networks.addAll(n);
        this.topologyStatus = status;

        this.paintTopology();
        this.JScrollPane_main.setViewportView(painter);

        // initializing this component...
        this.setTitle("GUI Devices Setup");
        this.setSize(500,500);
        this.setVisible(true);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        this.setContentPane(this.JPanel_main);

        /*
        Timer timer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Repaint the canvas
                painter.repaint();
            }
        });
        timer.start();
        */
    }

    private boolean collisionExist() {
        // base condition...
        // so that the 'generateNodes()' function will be called at least once...
        if (this.GUInodes == null || this.GUInodes.size() == 0 || this.GUInodes.size() != this.totalNodes) {
            return true;
        } else {
            // check for collision
            return false;
        }
    }

    private void generateNodes() {
        // it will generate nodes...
        int x=0,y=0;
        for (int i=0; i<this.totalNodes; i++) {
            x = (int) (Math.random() * 250) + 25;
            y = (int) (Math.random() * 250) + 25;
            this.GUInodes.add(new NodeHelper(x,y,String.valueOf(i)));
        }
    }

    private void generateLinks() {
        if (this.topologyStatus == TopologyStatus.TOPOLOGY_RING) {

            // if there are 'n' nodes...(n > 2)
            // there will be 'n' links...
            for (int i=0; i<this.totalNodes; i++) {
                this.GUIlinks.add(new P2PLinkHelper(this.GUInodes.get(i), this.GUInodes.get(((i+1)%this.totalNodes))));
            }

        } else if (this.topologyStatus == TopologyStatus.TOPOLOGY_MESH) {

            // if there are 'n' nodes...(n > 2)
            // there will be 'n(n-1)/2' links...
            for (int i=0; i<this.totalNodes-1; i++) {
                for (int j=i+1; j<this.totalNodes; j++) {
                    this.GUIlinks.add(new P2PLinkHelper(this.GUInodes.get(i), this.GUInodes.get(j)));
                }
            }

        } else { // assumed to be having star topology...

            // if there are 'n' nodes...(n > 3)
            // there will be 'n-1' links...
            for (int i=1; i<this.totalNodes; i++) {
                this.GUIlinks.add(new P2PLinkHelper(this.GUInodes.get(0), this.GUInodes.get(i)));
            }
        }
    }

    private void paintTopology() {
        // making the topology here...

        // generating nodes...
        // such that they don't collide with each other...
        if (collisionExist()) {
            this.generateNodes();
        }

        // generating links...
        this.generateLinks();
        System.out.println("length of GUINodes "+this.GUInodes.size()+" length of GUILinks : "+this.GUIlinks.size());
        this.painter = new TopologyPainter(this.GUInodes, this.GUIlinks);
        this.painter.repaint();
    }
}
