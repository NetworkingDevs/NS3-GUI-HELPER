package Dialogs;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Dialog_outputFileChooser extends JFrame {
    private JPanel JPanel_main;
    private JPanel JPanel_top;
    private JCheckBox chkBox_outputPath;
    private JCheckBox chkBox_fileName;
    private JPanel JPanel_middle;
    private JLabel lbl_path;
    private JTextField textField_outputPath;
    private JLabel lbl_fileName;
    private JTextField textField_fileName;
    private JPanel JPanel_bottom;
    private JButton btn_save;

    // These are variable created for serving the functionalities....
    String outputPath;
    String fileName;
    Dialog_Helper dialogHelper;

    public Dialog_outputFileChooser() {
        // ==================== BASIC CONF. ====================
        this.setContentPane(this.JPanel_main);
        this.setTitle("Output Configuration");
        this.setSize(400,200);
        this.setVisible(true);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        // ==================== DONE BASIC CONF. ====================

        this.dialogHelper = new Dialog_Helper(this);
        this.setUpEventListeners();
        this.textField_fileName.setText("output");
        this.outputPath = new String();
        this.fileName = new String();
    }

    public void setPath(String value) {
        this.textField_outputPath.setText(value);
    }

    public void setFileName(String value) {
        this.textField_fileName.setText(value);
    }

    public String getOutputPath() {
        return outputPath;
    }

    public String getFileName() {
        return fileName;
    }

    private void setUpEventListeners() {
        // action to perform when clicking on output path check box...
        chkBox_outputPath.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textField_outputPath.setEnabled(!chkBox_outputPath.isSelected());
            }
        });

        // action to perform when clicking on file name check box...
        chkBox_fileName.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textField_fileName.setEnabled(!chkBox_fileName.isSelected());
            }
        });

        // action to perform when clicking on save button...
        btn_save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                outputPath = textField_outputPath.getText();
                fileName = textField_fileName.getText();
                dialogHelper.showInformationMsg("Settings has been saved!", "Success!");
                setVisible(false);
            }
        });
    }
}
