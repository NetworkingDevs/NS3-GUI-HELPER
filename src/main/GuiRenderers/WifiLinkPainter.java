package GuiRenderers;

import Helpers.LoggingHelper;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

/**
 * to manage and paint Wi-Fi Links
 * */
public class WifiLinkPainter implements CanvasPainter {

    /**
     * list of index of nodes which are part of CSMA channel
     * */
    public ArrayList<Integer> nodeList;
    /**
     * the nodes which are part of CSMA channel
     * */
    public ArrayList<NodePainter> nodes;
    /**
     * image for wi-fi icon
     * */
    Image icon_wifi;
    /**
     * image for access point icon
     * */
    Image icon_ap;
    {
        try {
            icon_wifi = ImageIO.read(getClass().getClassLoader().getResource("icon_wifi.png"));
            icon_ap = ImageIO.read(getClass().getClassLoader().getResource("icon_ap.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * to create the object of type CSMA Link
     *
     * @param nodeList the index of node
     * @param nodes the nodes
     * @since 1.2.0
     * */
    public WifiLinkPainter(ArrayList<Integer> nodeList, ArrayList<NodePainter> nodes) {
        LoggingHelper.Log("Creating object of type WiFiLinkPainter");
        this.nodeList = nodeList;
        this.nodes = nodes;
    }

    @Override
    public void paint(Graphics g) {
        LoggingHelper.LogFunction("WiFiLink Painter : Painting on canvas!");
        NodePainter AccessPointNode =  this.nodes.get(this.nodeList.get(0));
        g.drawImage(icon_ap, AccessPointNode.xPos, AccessPointNode.yPos, AccessPointNode.radius, AccessPointNode.radius, new JFrame());
        for (int i=1; i<this.nodeList.size(); i++) {
            NodePainter node = this.nodes.get(this.nodeList.get(i));
            g.drawImage(icon_wifi, node.xPos, node.yPos, node.radius, node.radius, new JFrame());
        }
    }
}
