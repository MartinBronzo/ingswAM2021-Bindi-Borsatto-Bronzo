package it.polimi.ingsw.view.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GuiClientcopy {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(
            new Runnable() {
                    public void run() {
                        createAndShowGUI();
                        //createDialogLogin();
                    }
            }
        );
    }
    private static void createAndShowGUI() {
        System.out.println("Created GUI on EDT? "+ SwingUtilities.isEventDispatchThread());
        JFrame f = new CustomFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(1000,615);
        f.setVisible(true);
    }


    private static void createDialogLogin(){
        System.out.println("Created GUI on EDT? "+SwingUtilities.isEventDispatchThread());
        JFrame f = new JFrame();
        JDialog d = new JDialog(f , "what's your Nickname?", true);
        JTextField t = new JTextField("nickname");
        t.setBounds(50,100, 200,30);
        f.add(t);
        f.setSize(400,400);
        f.setLayout(null);
        f.setVisible(true);
        d.setLayout( new FlowLayout() );
        JButton b = new JButton ("Login");
        b.addActionListener ( new ActionListener()
        {
            public void actionPerformed( ActionEvent e )
            {
                //TODO: sen message thing
                d.setVisible(false);
            }
        });
        d.add( new JLabel ("Click button to continue."));
        d.add(b);
        d.setSize(300,300);
        d.setVisible(true);
    }
}
