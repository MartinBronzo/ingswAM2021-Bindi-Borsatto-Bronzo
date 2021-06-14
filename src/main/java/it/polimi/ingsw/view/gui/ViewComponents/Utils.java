package it.polimi.ingsw.view.gui.ViewComponents;

import javax.swing.*;

public class Utils {
    public static void multilineJLabelSetText(JLabel label, String string) throws NullPointerException {
        if (label==null || string == null) throw new NullPointerException();

        String converted = "<html>" + string.replaceAll("\n", "<br>") + "</html>";
        label.setText(converted);
    }
}
