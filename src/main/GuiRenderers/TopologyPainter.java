package GuiRenderers;


import java.awt.*;
import java.util.ArrayList;

public class TopologyPainter extends Canvas {
    private static int DEFAULT_WIDTH = 500;
    private static int DEFAULT_HEIGHT = 500;
    private static int SERVER_NODE = -1;
    private static int CLIENT_NODE = -1;
    private static NodePainter HIGHLIGHT = new NodePainter(0,0, 30, "SERVER", Color.black);

    int width, height;
    ArrayList<NodePainter> nodes;
    ArrayList<P2pLinkPainter> links;
    ArrayList<CsmaLinkPainter> links_csma;
    ArrayList<NodePainter> referenceNodes;

    public TopologyPainter(ArrayList<NodePainter> n, ArrayList<P2pLinkPainter> l) {
        this(n,l,DEFAULT_WIDTH,DEFAULT_HEIGHT);
    }

    public TopologyPainter(ArrayList<NodePainter> n, ArrayList<P2pLinkPainter> l, int w, int h) {
        // initializing components...
        this.nodes = new ArrayList<>();
        this.nodes.addAll(n);
        this.links = new ArrayList<>();
        this.links.addAll(l);
        this.links_csma = new ArrayList<>();
        this.referenceNodes = new ArrayList<>();
        this.width = w;
        this.height = h;
    }

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
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(500,500);
    }

    public void addNode(NodePainter node) {
        this.nodes.add(node);
    }

    public void addLink(P2pLinkPainter link) {
        this.links.add(link);
    }

    public void addLink(CsmaLinkPainter link) {
        this.links_csma.add(link);
    }

    public void addRefNode(NodePainter node) {
        this.referenceNodes.add(node);
    }

    public void addAndPrintRefNode(NodePainter node) {
        this.addRefNode(node);
        this.repaint();
    }

    public void addAndPrintNode(NodePainter node) {
        this.addNode(node);
        this.repaint();
    }

    public void addAndPrintLink(CsmaLinkPainter link) {
        this.addLink(link);
        this.repaint();
    }

    public void addAndPrintLink(P2pLinkPainter link) {
        this.addLink(link);
        this.repaint();
    }

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

    public void enableView() {
        this.enableView(-1,-1);
    }

    public void enableView(int s, int c) {
        SERVER_NODE = s;
        CLIENT_NODE = c;
        System.out.println("Server : "+s+" client : "+c);
        this.repaint();
    }

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
