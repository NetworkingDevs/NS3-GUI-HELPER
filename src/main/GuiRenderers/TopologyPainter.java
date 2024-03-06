package GuiRenderers;


import Ns3Objects.Links.WIFI;

import java.awt.*;
import java.util.ArrayList;

/**
 * for painting the objects on the canvas
 * this will be centrally responsible entity
 * */
public class TopologyPainter extends Canvas {
    /**
     * the default width of the canvas
     * */
    private static int DEFAULT_WIDTH = 500;
    /**
     * the default height of the canvas
     * */
    private static int DEFAULT_HEIGHT = 500;
    /**
     * the index of server node
     * */
    private static int SERVER_NODE = -1;
    /**
     * the index of client node
     * */
    private static int CLIENT_NODE = -1;
    /**
     * the static node for highlighting of the client and server nodes
     * */
    private static NodePainter HIGHLIGHT = new NodePainter(0,0, 30, "SERVER", Color.black);

    /**
     * width and height of the canvas
     * */
    int width, height;
    /**
     * the list of node, that will be painted on the canvas
     * */
    ArrayList<NodePainter> nodes;
    /**
     * the point to point links that will be painted on the canvas
     * */
    ArrayList<P2pLinkPainter> links;
    /**
     * the CSMA links that will be painted on the canvas
     * */
    ArrayList<CsmaLinkPainter> links_csma;
    /**
     * the Wi-Fi links that will be painted on the canvas
     * */
    ArrayList<WifiLinkPainter> links_wifi;
    /**
     * the reference nodes, shown while adding CSMA link
     * */
    ArrayList<NodePainter> referenceNodes;

    /**
     * to create an object of type Topology Painter
     *
     * @param n list of nodes
     * @param l list of links
     * @see TopologyPainter#TopologyPainter(ArrayList, ArrayList, int, int)
     * @since 0.3.0
     * */
    public TopologyPainter(ArrayList<NodePainter> n, ArrayList<P2pLinkPainter> l) {
        this(n,l,DEFAULT_WIDTH,DEFAULT_HEIGHT);
    }

    /**
     * to create an object of type Topology Painter
     *
     * @param n list of nodes
     * @param l list of links
     * @param w the width of the canvas
     * @param h the height of the canvas
     * @see TopologyPainter#TopologyPainter(ArrayList, ArrayList)
     * @since 0.3.0
     * */
    public TopologyPainter(ArrayList<NodePainter> n, ArrayList<P2pLinkPainter> l, int w, int h) {
        // initializing components...
        this.nodes = new ArrayList<>();
        this.nodes.addAll(n);
        this.links = new ArrayList<>();
        this.links.addAll(l);
        this.links_csma = new ArrayList<>();
        this.links_wifi = new ArrayList<>();
        this.referenceNodes = new ArrayList<>();
        this.width = w;
        this.height = h;
    }

    /**
     * to paint the objects on the canvas
     *
     * @param g the graphics in the CANVAS
     * @since 0.3.0
     * */
    @Override
    public void paint(Graphics g) {
        super.paint(g);

        g.clearRect(0,0,this.width,this.height);

        // painting reference nodes...
        for (int i=0; i<this.referenceNodes.size(); i++) {
            this.referenceNodes.get(i).paint(g);
        }

        // painting the nodes...
        for (int i=0; i<this.nodes.size(); i++) {
            if (SERVER_NODE == i) {
                HIGHLIGHT.label = "SERVER";
                HIGHLIGHT.color = Color.black;
                HIGHLIGHT.xPos = this.nodes.get(i).xPos - 5;
                HIGHLIGHT.yPos = this.nodes.get(i).yPos - 5;
                HIGHLIGHT.paint(g);
            }

            if (CLIENT_NODE == i) {
                HIGHLIGHT.label = "CLIENT";
                HIGHLIGHT.color = Color.black;
                HIGHLIGHT.xPos = this.nodes.get(i).xPos - 5;
                HIGHLIGHT.yPos = this.nodes.get(i).yPos - 5;
                HIGHLIGHT.paint(g);
            }
            this.nodes.get(i).paint(g);
        }

        // painting point to point links...
        for (int i=0; i<this.links.size(); i++) {
            this.links.get(i).paint(g);
        }

        // painting csma links...
        for(int i=0; i<this.links_csma.size(); i++) {
            this.links_csma.get(i).paint(g);
        }

        // painting wi-fi links...
        for(int i=0; i<this.links_wifi.size(); i++) {
            this.links_wifi.get(i).paint(g);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(this.width,this.height);
    }

    /**
     * to add nodes for painting
     *
     * @param node the node to be added
     * @since 0.3.0
     * */
    public void addNode(NodePainter node) {
        this.nodes.add(node);
    }

    /**
     * to add the P2P link for painting
     *
     * @param link the link to be added
     * @see TopologyPainter#addLink(CsmaLinkPainter)
     * @since 0.3.0
     * */
    public void addLink(P2pLinkPainter link) {
        this.links.add(link);
    }

    /**
     * to add the CSMA link for painting
     *
     * @param link the link to be added
     * @see TopologyPainter#addLink(P2pLinkPainter)
     * @since 0.3.0
     * */
    public void addLink(CsmaLinkPainter link) {
        this.links_csma.add(link);
    }

    /**
     * to add the Wi-Fi link for painting
     *
     * @param link the link to be added
     * @since 1.2.0
     * */
    public void addLink(WifiLinkPainter link) {
        this.links_wifi.add(link);
    }

    /**
     * to add the reference node
     *
     * @param node the node to be added
     * @since 1.1.0
     * */
    public void addRefNode(NodePainter node) {
        this.referenceNodes.add(node);
    }

    /**
     * to add the reference node and directly painting
     *
     * @param node the node to be added
     * @since 1.1.0
     * */
    public void addAndPrintRefNode(NodePainter node) {
        this.addRefNode(node);
        this.repaint();
    }

    /**
     * to add the node and direct painting
     *
     * @param node the node to be added
     * @since 0.3.0
     * */
    public void addAndPrintNode(NodePainter node) {
        this.addNode(node);
        this.repaint();
    }

    /**
     * to add the link and direct painting
     *
     * @param link the link to be added
     * @since 0.3.0
     * */
    public void addAndPrintLink(CsmaLinkPainter link) {
        this.addLink(link);
        this.repaint();
    }

    /**
     * to add the link and direct painting
     *
     * @param link the link to be added
     * @since 0.3.0
     * */
    public void addAndPrintLink(P2pLinkPainter link) {
        this.addLink(link);
        this.repaint();
    }

    /**
     * to add the link and direct painting
     *
     * @param link the link to be added
     * @since 1.2.0
     * */
    public void addAndPrintLink(WifiLinkPainter link) {
        this.addLink(link);
        this.repaint();
    }

    /**
     * to get the index of node, with which the mouse is collided
     *
     * @param x x-position of the mouse
     * @param y y-position of the mouse
     * @return the integer value, representing the index of the first collided node,
     *          and it will return -1, in case not collided with any of the node
     * @since 0.3.0
     * */
    public int pointCollideWithAny(int x, int y) {
        int index = -1;

        for (int i=0; i<this.nodes.size(); i++) {
            if (this.checkCollisionWithNode(this.nodes.get(i), x, y)) {
                index = i;
                break;
            }
        }

        return index;
    }

    /**
     * to enabling highlighting of the server and client node
     *
     * @param s server index
     * @param c client index
     * @since 1.0.0
     * */
    public void enableView(int s, int c) {
        SERVER_NODE = s;
        CLIENT_NODE = c;
        System.out.println("Server : "+s+" client : "+c);
        this.repaint();
    }

    /**
     * to check that whether the node is collided with the mouse position
     *
     * @param n Node
     * @param x x-position of the mouse
     * @param y y-position of the mouse
     * @return The boolean value, showing that whether the node is colliding or not
     * @see TopologyPainter#pointCollideWithAny(int, int)
     * @since 0.3.0
     * */
    private boolean checkCollisionWithNode(NodePainter n, int x, int y) {
        int distance = (int) Math.sqrt(Math.pow(n.xPos - x,2) + Math.pow(n.yPos - y, 2));
        // System.out.println("Distance : "+distance); // just for testing...
        if (distance < n.radius) {
            return true;
        }
        return false;
    }

    public ArrayList<NodePainter> getNodes() {
        return nodes;
    }

    public void setNodes(ArrayList<NodePainter> nodes) {
        this.nodes = nodes;
    }

    public ArrayList<P2pLinkPainter> getLinks() {
        return links;
    }

    public void setLinks(ArrayList<P2pLinkPainter> links) {
        this.links = links;
    }

    public ArrayList<NodePainter> getReferenceNodes() {
        return referenceNodes;
    }

    public void setReferenceNodes(ArrayList<NodePainter> referenceNodes) {
        this.referenceNodes = referenceNodes;
    }

    public ArrayList<CsmaLinkPainter> getLinks_csma() {
        return links_csma;
    }

    public void setLinks_csma(ArrayList<CsmaLinkPainter> links_csma) {
        this.links_csma = links_csma;
    }
}
