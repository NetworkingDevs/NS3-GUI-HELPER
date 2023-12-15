package GuiHelpers;

import org.w3c.dom.Node;

import java.awt.*;
import java.util.ArrayList;

public class TopologyPainter extends Canvas {
    private static int DEFAULT_WIDTH = 500;
    private static int DEFAULT_HEIGHT = 500;

    int width, height;
    ArrayList<NodeHelper> nodes;
    ArrayList<P2PLinkHelper> links;

    public TopologyPainter(ArrayList<NodeHelper> n, ArrayList<P2PLinkHelper> l) {
        this(n,l,DEFAULT_WIDTH,DEFAULT_HEIGHT);
    }

    public TopologyPainter(ArrayList<NodeHelper> n, ArrayList<P2PLinkHelper> l, int w, int h) {
        // initializing components...
        this.nodes = new ArrayList<>();
        this.nodes.addAll(n);
        this.links = new ArrayList<>();
        this.links.addAll(l);
        this.width = w;
        this.height = h;
    }

    @Override
    public void paint(Graphics g) {
       super.paint(g);
       for (NodeHelper node : this.nodes) {
           node.paintNode(g);
       }
       for (P2PLinkHelper link : this.links) {
           link.paintLink(g);
       }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(500,500);
    }
}
