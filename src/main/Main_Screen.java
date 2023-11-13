import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileOutputStream;

public class Main_Screen extends JFrame {
    private JPanel JPanel_main;
    private JPanel JPanel_config;
    private JLabel lbl_topology;
    private JComboBox comboBox_Topology;
    private JLabel lbl_filepath;
    private JTextField textField_outputFolder;
    private JButton btn_Go;

    public Main_Screen() {
        this.setContentPane(this.JPanel_main);
        this.setTitle("Topology Helper - NS3");
        this.setSize(400,300);
        this.setVisible(true);
        this.setResizable(false);
        // this was by default...
        // this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // this has been added for user confirmation...
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);

                int option = JOptionPane.showConfirmDialog(JPanel_main,"Are you sure you want to close the app?","Quit!",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                if(option == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });

        btn_Go.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int FieldTopology = comboBox_Topology.getSelectedIndex();
                String FieldOutputPath = textField_outputFolder.getText().toString();

                if(FieldTopology == 0) {
                    Topology_P2P p2p = new Topology_P2P(FieldOutputPath);
                } else if(FieldTopology == 1) {
                    Topology_Ring ring = new Topology_Ring(FieldOutputPath);
                }
            }
        });
    }

    public static void main(String[] args) {
        Main_Screen main = new Main_Screen();
    }
}
