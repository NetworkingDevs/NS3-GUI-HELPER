package GuiRenderers;

import java.awt.*;

public class NodePainter implements CanvasPainter {
    public int xPos;
    public int yPos;
    public int radius;
    public String label;
    public Color color;

    private static Color DEFAULT_FILL_COLOR = Color.RED;
    private static int DEFAULT_RADIUS = 20;

    public NodePainter(int x, int y, String label) {
        this(x,y,DEFAULT_RADIUS,label,DEFAULT_FILL_COLOR);
    }

    public NodePainter(int x, int y, int r, String label) {
        this(x,y,r,label,DEFAULT_FILL_COLOR);
    }

    public NodePainter(int x, int y, int r, String label, Color fill) {
            this.xPos = x;
            this.yPos = y;
            this.radius = r;
            this.label = label;
            this.color = fill;
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(this.color);
        g.fillOval(this.xPos,this.yPos,this.radius,this.radius);
        g.drawString(this.label,this.xPos,this.yPos);
    }

}

