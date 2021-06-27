package it.polimi.ingsw.view.gui.ViewComponents.utils;

import javax.swing.*;

/**
 * This class has a static method which can be used to create multilines to be used on labels.
 */
public class MultiLineTextMaker {

    /**
     * Adds the specified string onto the label after making it suitable for the label (that is, it adds the proper HTML tags)
     * @param label the label where the specified string is to be added
     * @param string the string to be changed and added to the specified label
     * @throws NullPointerException if either the label or the string are null pointers
     */
    public static void multilineJLabelSetText(JLabel label, String string) throws NullPointerException {
        if (label==null || string == null) throw new NullPointerException();

        String converted = "<html>" + string.replaceAll("\n", "<br>") + "</html>";
        label.setText(converted);
    }
}
