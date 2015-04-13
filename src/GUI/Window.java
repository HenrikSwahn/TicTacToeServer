package GUI;

import javax.swing.*;
import java.awt.*;

/**
 * Created by henrik on 13/04/15.
 */
public class Window extends JFrame {

    private GridBagConstraints gbc;
    private LogPanel log;


    public Window(String windowName) {

        gbc = new GridBagConstraints();
        log = new LogPanel();
        setName(windowName);
        setUpLayout();
        log.append(windowName);

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

        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weighty = 1.0;
        gbc.weightx = 1.0;
        add(log,gbc);

    }
}
