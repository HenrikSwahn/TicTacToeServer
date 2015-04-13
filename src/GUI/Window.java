package GUI;

import javax.swing.*;
import java.awt.*;

/**
 * Created by henrik on 13/04/15.
 */
public class Window extends JFrame {

    private GridBagConstraints gbc;


    public Window(String windowName) {

        setName(windowName);
        gbc = new GridBagConstraints();
        setUpLayout();

    }

    private void setUpLayout() {

        setLayout(new GridBagLayout());
        setSize(new Dimension(320,800));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        setUpPanels();
        setVisible(true);

    }

    private  void setUpPanels() {



    }
}
