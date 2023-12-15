package GuiHelpers;

import java.awt.*;

public class P2PLinkHelper {
    public NodeHelper nodeA,nodeB;
    Color linkColor;

    private static Color DEFAULT_LINK_COLOR = Color.black;

    public P2PLinkHelper(NodeHelper a, NodeHelper b) {
        this(a, b, DEFAULT_LINK_COLOR);
    }

    public P2PLinkHelper(NodeHelper a, NodeHelper b, Color color) {
        this.nodeA = a;
        this.nodeB = b;
        this.linkColor = color;
    }

    public void paintLink(Graphics g) {
        int adjustA = this.nodeA.radius/2;
        int adjustB = this.nodeB.radius/2;
        g.setColor(this.linkColor);
        g.drawLine(this.nodeA.xPos + adjustA, this.nodeA.yPos + adjustA, this.nodeB.xPos + adjustB, this.nodeB.yPos + adjustB);
    }
}
