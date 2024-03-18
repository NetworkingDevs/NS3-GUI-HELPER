/**
 * <p>
 *     The following package includes the classes for rendering
 *     the objects on the canvas of the application and it will help
 *     to interact the mouse with canvas.
 *
 *     It will draw the elements on the canvas not like that it will
 *     render the images or something.
 * </p>
 * */
package GuiRenderers;

import java.awt.*;

/**
 * The template for any object to be rendered on the canvas
 * */
public interface CanvasPainter {
    /**
     * to paint the object/element on the canvas
     * */
    public void paint(Graphics g);
}
