/**
 * <p>
 *     The following package will include the classes for interacting with use
 *     in form of a dialog, it will help to manage, store, add and modify the things
 *     that act as a modules, classes in NS-3
 * </p>
 * */
package Dialogs;

import javax.swing.*;
import java.awt.*;

/**
 * This will help to show confirmation, warning, error and information messages
 * in dialog form
 * */
public class Dialog_Helper {
    /**
     * the parent component, in reference of the dialog box
     * */
    public Component anyParentComponent;

    /**
     * to create an object of type Dialog_Helper
     *
     * @param component the parent component
     * @since 1.0.0
     * */
    public Dialog_Helper(Component component) {
         this.anyParentComponent = component;
    }

    /**
     * to show the confirmation  dialog
     *
     * @param msg the msg that you want to show
     * @param title The title of the dialog
     * @return The integer value showcasing the selected choice of the user
     * @since 1.0.0
     * */
    public int showConfirmationDialog(String msg, String title) {
        return JOptionPane.showConfirmDialog(this.anyParentComponent, msg, title, JOptionPane.YES_NO_OPTION);
    }

    /**
     * to show the warning dialog
     *
     * @param msg the message that you want to show
     * @param title The title of the dialog
     * @since 1.0.0
     * */
    public void showWarningMsg(String msg, String title) {
        this.showDialog(msg, title, JOptionPane.WARNING_MESSAGE);
    }

    /**
     * to show the information dialog
     *
     * @param msg the message that you want to show
     * @param title the title of the dialog
     * @since 1.0.0
     * */
    public void showInformationMsg(String msg, String title) {
        this.showDialog(msg, title, JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * to show the error dialog
     *
     * @param msg the message that you want to show
     * @param title the title of the dialog
     * @since 1.0.0
     * */
    public void showErrorMsg(String msg, String title) {
        this.showDialog(msg, title, JOptionPane.ERROR_MESSAGE);
    }

    /**
     * to show the dialog
     *
     * @param msg the message that you want to show
     * @param title the title of the dialog
     * @param type type of the dialog
     * @since 1.0.0
     * */
    private void showDialog(String msg, String title, int type) {
        JOptionPane.showMessageDialog(this.anyParentComponent, msg, title, type);
    }
}
