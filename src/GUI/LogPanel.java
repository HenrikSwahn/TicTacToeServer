package GUI;

import javax.swing.*;
import java.awt.*;

/**
 * Created by henrik on 13/04/15.
 */
public class LogPanel extends JPanel {

    private JTextArea area;

    public LogPanel() {

        area = new JTextArea();
        setUpLayout();

    }

    public void append(String msg) {

        String toAppend = ">" + msg + "\n";
        area.append(toAppend);

    }

    private void setUpLayout() {

        setLayout(new GridLayout(1,1));
        JScrollPane jsp = new JScrollPane(area);
        add(jsp);

    }

}
