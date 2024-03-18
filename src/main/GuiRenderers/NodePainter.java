package GuiRenderers;

import Helpers.LoggingHelper;

import java.awt.*;

/**
 * to manage, paint and store the node
 * */
public class NodePainter implements CanvasPainter {

    /**
     * x-value for the node
     * */
    public int xPos;
    /**
     * y-value for the node
     * */
    public int yPos;
    /**
     * radius value for the node
     * */
    public int radius;
    /**
     * label for the node
     * */
    public String label;
    /**
     * color for the node
     * */
    public Color color;

    /**
     * default color for the node
     * */
    private static Color DEFAULT_FILL_COLOR = Color.RED;
    /**
     * default radius for the node
     * */
    private static int DEFAULT_RADIUS = 20;

    /**
     * to create the object if type NodePainter
     *
     * @param x x-position
     * @param y y-position
     * @param label label for the node
     * @see NodePainter#NodePainter(int, int, int, String, Color)
     * @see NodePainter#NodePainter(int, int, int, String)
     * @since 0.3.0
     * */
    public NodePainter(int x, int y, String label) {
        this(x,y,DEFAULT_RADIUS,label,DEFAULT_FILL_COLOR);
    }

    /**
     * to create the object if type NodePainter
     *
     * @param x x-position
     * @param y y-position
     * @param r radius value
     * @param label label for the node
     * @see NodePainter#NodePainter(int, int, int, String, Color)
     * @see NodePainter#NodePainter(int, int, String)
     * @since 0.3.0
     * */
    public NodePainter(int x, int y, int r, String label) {
        this(x,y,r,label,DEFAULT_FILL_COLOR);
    }

    /**
     * to create the object if type NodePainter
     *
     * @param x x-position
     * @param y y-position
     * @param r radius value
     * @param label label for the node
     * @param fill the color for the node
     * @see NodePainter#NodePainter(int, int, int, String)
     * @see NodePainter#NodePainter(int, int, String)
     * @since 0.3.0
     * */
    public NodePainter(int x, int y, int r, String label, Color fill) {
        LoggingHelper.Log("Creating object of type NodePainter");
        this.xPos = x;
        this.yPos = y;
        this.radius = r;
        this.label = label;
        this.color = fill;
    }

    /**
     * to paint the node on the canvas
     *
     * @param g the Graphics from the painter
     * @since 0.3.0
     * */
    @Override
    public void paint(Graphics g) {
        LoggingHelper.LogFunction("Node Painter : Painting on the canvas!");
        g.setColor(this.color);
        g.fillOval(this.xPos,this.yPos,this.radius,this.radius);
        g.drawString(this.label,this.xPos,this.yPos);
    }

}

