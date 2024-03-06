package Helpers;

import javax.swing.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

/**
 * it will be responsible for adding placeholder
 *
 * @since 1.2.0
 * */
public class PlaceHolderHelper {
    /**
     * it will add the events for place-holder on given text field
     *
     * @param textField The target text field
     * @param placeholder the desired placeholder string
     * @since 1.2.0
     * */
    public static void addPlaceHolder(JTextField textField, String placeholder) {
        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                if (textField.getText().equals(placeholder)) {
                    textField.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                if (textField.getText().equals("")) {
                    textField.setText(placeholder);
                }
            }
        });
    }
}
