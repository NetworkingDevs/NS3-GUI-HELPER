import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
        this.setSize(400,400);
        this.setVisible(true);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        btn_Go.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int FieldTopology = comboBox_Topology.getSelectedIndex();
                String FieldOutputPath = textField_outputFolder.getText().toString();

                if(FieldTopology == 0) {
                    Topology_P2P p2p = new Topology_P2P(FieldOutputPath);
                }
            }
        });
    }

    public static void main(String[] args) {
        Main_Screen main = new Main_Screen();
    }
}
