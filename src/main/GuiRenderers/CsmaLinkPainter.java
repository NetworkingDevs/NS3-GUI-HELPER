package GuiRenderers;

import Helpers.LoggingHelper;

import java.awt.*;
import java.util.ArrayList;

/**
 * to manage, paint and store the CSMA Link
 * */
public class CsmaLinkPainter implements CanvasPainter {
    /**
     * the color of the link
     * */
    public Color linkColor;
    /**
     * list of index of nodes which are part of CSMA channel
     * */
    public ArrayList<Integer> nodeList;
    /**
     * the nodes which are part of CSMA channel
     * */
    public ArrayList<NodePainter> nodes;

    /**
     * dashed line for stroke painting of the CSMA chnnel
     * */
    public static Stroke dashed = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);

    /**
     * default link color
     * */
    private static Color DEFAULT_LINK_COLOR = Color.GREEN;

    /**
     * to create the object of type CSMA Link
     *
     * @param nodeList the index of node
     * @param nodes the nodes
     * @param color the color of the link
     * @see CsmaLinkPainter#CsmaLinkPainter(ArrayList, ArrayList)
     * @since 1.1.0
     * */
    public CsmaLinkPainter(ArrayList<Integer> nodeList, ArrayList<NodePainter> nodes, Color color) {
        LoggingHelper.Log("Creating object of type CsmaLinkPainter");
        this.nodes = nodes;
        this.nodeList = nodeList;
        this.linkColor = color;
    }

    /**
     * to create the object of type CSMA Link
     *
     * @param nodeList the index of node
     * @param nodes the nodes
     * @see CsmaLinkPainter#CsmaLinkPainter(ArrayList, ArrayList, Color)
     * @since 1.1.0
     * */
    public CsmaLinkPainter(ArrayList<Integer> nodeList, ArrayList<NodePainter> nodes) {
        this(nodeList,nodes,DEFAULT_LINK_COLOR);
    }

    /**
     * to paint the CSMA link
     *
     * @param g the Graphics from the painter
     * @since 1.1.0
     * */
    @Override
    public void paint(Graphics g) {
        LoggingHelper.LogFunction("Csma Link Paiting : Painting on canvas!");
        int adjustA = 0, adjustB = 0;
        NodePainter nodeA, nodeB;
        for(int i=0; i<=nodeList.size()-2; i++) {
            nodeA = this.nodes.get(this.nodeList.get(i));
            nodeB = this.nodes.get(this.nodeList.get(i+1));
            adjustA = nodeA.radius / 2 ;
            adjustB = nodeB.radius / 2 ;
            g.setColor(this.linkColor);
            ((Graphics2D)g).setStroke(dashed);
            g.drawLine(nodeA.xPos + adjustA, nodeA.yPos + adjustA, nodeB.xPos + adjustB, nodeB.yPos + adjustB);
        }
    }
}
