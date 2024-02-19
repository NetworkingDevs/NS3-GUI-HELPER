package GuiRenderers;

import java.awt.*;
import java.util.ArrayList;

/**
 * Program name: CsmaLinkRenderer
 * Program date: 19-02-2024
 * Program owner: henil
 * Contributor:
 * Last Modified: 19-02-2024
 *
 * <p>
 * Purpose: for rendering the CSMA Link onto
 * the canvas
 * </p>
 */
public class CsmaLinkPainter implements CanvasPainter {
    public Color linkColor;
    public ArrayList<Integer> nodeList;
    public ArrayList<NodePainter> nodes;

    public static Stroke dashed = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);

    private static Color DEFAULT_LINK_COLOR = Color.GREEN;

    public CsmaLinkPainter(ArrayList<Integer> nodeList, ArrayList<NodePainter> nodes, Color color) {
        this.nodes = nodes;
        this.nodeList = nodeList;
        this.linkColor = color;
    }

    public CsmaLinkPainter(ArrayList<Integer> nodeList, ArrayList<NodePainter> nodes) {
        this(nodeList,nodes,DEFAULT_LINK_COLOR);
    }

    @Override
    public void paint(Graphics g) {
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
