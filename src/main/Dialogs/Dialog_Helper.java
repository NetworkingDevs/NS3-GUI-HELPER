/**
 * Program name: Dialog_Base
 * Program date: 03-01-2024
 * Program owner: henil
 * Contributor:
 * Last Modified: 03-01-2024
 * <p>
 * Purpose:
 */
package Dialogs;

import javax.swing.*;
import java.awt.*;

public class Dialog_Helper {
    public Component anyParentComponent;

    public Dialog_Helper(Component component) {
         this.anyParentComponent = component;
    }

    public int showConfirmationDialog(String msg, String title) {
        return JOptionPane.showConfirmDialog(this.anyParentComponent, msg, title, JOptionPane.YES_NO_OPTION);
    }

    public void showWarningMsg(String msg, String title) {
        this.showDialog(msg, title, JOptionPane.WARNING_MESSAGE);
    }

    public void showInformationMsg(String msg, String title) {
        this.showDialog(msg, title, JOptionPane.INFORMATION_MESSAGE);
    }

    public void showErrorMsg(String msg, String title) {
        this.showDialog(msg, title, JOptionPane.ERROR_MESSAGE);
    }

    private void showDialog(String msg, String title, int type) {
        JOptionPane.showMessageDialog(this.anyParentComponent, msg, title, type);
    }
}
