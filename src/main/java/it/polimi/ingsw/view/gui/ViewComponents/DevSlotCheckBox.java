package it.polimi.ingsw.view.gui.ViewComponents;

import it.polimi.ingsw.view.gui.ViewComponents.OnlyViewPanels.DevSlotOnlyView;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class DevSlotCheckBox extends JPanel{
    DevSlotOnlyView devSlotOnlyView;
    List<JCheckBox> checkBoxList;


    public DevSlotCheckBox(DevSlotOnlyView devSlotOnlyView) {
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.devSlotOnlyView = devSlotOnlyView;
        this.add(devSlotOnlyView);

        checkBoxList = new ArrayList<>();
        JPanel checkPanel = new JPanel();
        for(int i = 0; i<3; i++){
            //adds the checkbox to the panel
            JCheckBox checkBox = new JCheckBox();
            checkBoxList.add(checkBox);
            checkPanel.add(checkBox);
            checkPanel.add(Box.createRigidArea(new Dimension(60, 0)));
        }
        this.add(checkPanel);
    }

    public List<Integer> getSelectedDevSlotIndexes(){
        List<Integer> devSlotList = new ArrayList<>();

        for(int i = 0; i < checkBoxList.size(); i++){
            if(checkBoxList.get(i).isSelected())
                devSlotList.add(i);
        }

        return devSlotList;
    }
}
