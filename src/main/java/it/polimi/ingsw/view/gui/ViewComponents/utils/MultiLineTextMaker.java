package it.polimi.ingsw.view.gui.ViewComponents.utils;

import javax.swing.*;

public class MultiLineTextMaker {
    public static void multilineJLabelSetText(JLabel label, String string) throws NullPointerException {
        if (label==null || string == null) throw new NullPointerException();

        String converted = "<html>" + string.replaceAll("\n", "<br>") + "</html>";
        label.setText(converted);
    }
}
