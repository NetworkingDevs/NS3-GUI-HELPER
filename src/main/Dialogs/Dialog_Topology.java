package Dialogs;

import javax.swing.*;
import java.awt.*;

public class Dialog_Topology extends JFrame {
    private JPanel JPanel_main;
    private JPanel JPanel_topology_btns;
    private JButton btn_setUp;
    private JScrollPane JScrollPane_btns_links;

    ImageIcon icon = new ImageIcon("src/main/Resources/link_color.png");
    Image img = icon.getImage();

    public Dialog_Topology(int gridSize) {

        int grid = (int) Math.floor(gridSize/2);

        this.setContentPane(this.JPanel_main);
        this.JPanel_topology_btns.setLayout(new GridLayout(grid,grid*2,3,3));

        for(int i=0; i<gridSize; i++) {
            JButton btn = new JButton(String.valueOf(i));
            btn.setHorizontalTextPosition(SwingConstants.TRAILING);
            this.JPanel_topology_btns.add(btn);
            Image scaledImg = img.getScaledInstance(100,100,Image.SCALE_SMOOTH);
            ImageIcon linkIcon = new ImageIcon(scaledImg);
            btn.setIcon(linkIcon);
        }

        this.setTitle("GUI Topology Setup");
        this.setSize(500,500);
        this.setVisible(true);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    }

    public static void main(String[] args) {
        new Dialog_Topology(4);
    }
}
