package GuiRenderers;

import java.awt.*;

/**
 * to manage, paint and store the P2P Links
 * */
public class P2pLinkPainter implements CanvasPainter {

    /**
     * to mark nodes who are part of point to point link
     * */
    public NodePainter nodeA,nodeB;
    /**
     * for color of the link
     * */
    public Color linkColor;

    /**
     * to map the default color of the point-to-point link
     * */
    private static Color DEFAULT_LINK_COLOR = Color.black;

    /**
     * to create the object of type P2pLinkPainter
     *
     * @param a the first node
     * @param b the second node
     * @see P2pLinkPainter#P2pLinkPainter(NodePainter, NodePainter, Color)
     * @since 0.3.0
     * */
    public P2pLinkPainter(NodePainter a, NodePainter b) {
        this(a, b, DEFAULT_LINK_COLOR);
    }

    /**
     * to create the object of type P2pLinkPainter
     *
     * @param a the first node
     * @param b the second node
     * @param color the color of the link
     * @see P2pLinkPainter#P2pLinkPainter(NodePainter, NodePainter)
     * @since 0.3.0
     * */
    public P2pLinkPainter(NodePainter a, NodePainter b, Color color) {
        this.nodeA = a;
        this.nodeB = b;
        this.linkColor = color;
    }

    /**
     * to paint the P2P Link on the canvas
     *
     * @param g Graphics for painting, from painter
     * @since 0.3.0
     * */
    @Override
    public void paint(Graphics g) {
        int adjustA = this.nodeA.radius/2;
        int adjustB = this.nodeB.radius/2;
        g.setColor(this.linkColor);
        g.drawLine(this.nodeA.xPos + adjustA, this.nodeA.yPos + adjustA, this.nodeB.xPos + adjustB, this.nodeB.yPos + adjustB);
    }
}
