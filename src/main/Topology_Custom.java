import GuiHelpers.NodeHelper;
import GuiHelpers.P2PLinkHelper;
import GuiHelpers.TopologyPainter;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;

public class Topology_Custom extends JFrame {
    private JPanel JPanel_main;
    private JPanel JPanel_left;
    private JPanel JPanel_right;
    private JLabel lbl_info;
    private JButton btn_generateCode;
    private JScrollPane JScrollPane_right;
    private JPanel JPanel_configurationWindow;
    private JScrollPane JScrollPane_canvasContainer;
    private JPanel JPanel_tools;
    private JButton btn_node;
    private JButton btn_link;
    private JPanel JPanel_topologyConfiguration;
    private JPanel JPanel_utilityConfiguration;
    private JCheckBox chkBox_wireshark;
    private JCheckBox chkBox_netanim;
    private JPanel JPanel_grpLink;
    private JLabel lbl_links;
    private JButton btn_addLinks;
    private JComboBox comboBox_links;
    private JPanel JPanel_grpNetwork;
    private JLabel lbl_network;
    private JButton btn_addNetwork;
    private JComboBox comboBox_networks;
    private JButton btn_configServer;
    private JButton btn_configClient;

    TopologyPainter painter = new TopologyPainter(new ArrayList<NodeHelper>(), new ArrayList<P2PLinkHelper>(), 400, 400);
    Image img;

    {
        try {
            img = ImageIO.read(getClass().getClassLoader().getResource("info.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Topology_Custom() {
        this.setContentPane(this.JPanel_main);
        this.setTitle("Topology Helper - Custom Topology");
        this.setSize(800,500);
        this.setVisible(true);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.JScrollPane_canvasContainer.setViewportView(this.painter);
        this.JScrollPane_right.setViewportView(this.JPanel_configurationWindow);

        this.painter.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                System.out.println("Clicked at x : "+e.getX()+" y : "+e.getY());
            }
        });

        Image scaledImg = img.getScaledInstance(30,30,Image.SCALE_SMOOTH);
        ImageIcon infoIcon = new ImageIcon(scaledImg);
        lbl_info.setIcon(infoIcon);

        btn_node.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lbl_info.setText("Node Tool Selected: Click anywhere on the canvas to add a node.");
            }
        });
        btn_link.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lbl_info.setText("Link Tool Selected: To create a link, click on two nodes sequentially.");
            }
        });
    }

    public static void main(String[] args) {
        new Topology_Custom();
    }

}
