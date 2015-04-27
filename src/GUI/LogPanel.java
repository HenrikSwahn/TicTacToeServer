package GUI;

import Model.GameActionObject;

import javax.swing.*;
import java.awt.*;

/**
 * Created by henrik on 13/04/15.
 */
public class LogPanel extends JPanel {

    private JTextArea area;

    public LogPanel() {

        area = new JTextArea();
        area.setEditable(false);
        setUpLayout();

    }

    public void append(Object obj) {

        if(obj instanceof String) {

            obj = ">" + obj + "\n";
            area.append((String)obj);

        }else if(obj instanceof GameActionObject) {

            area.append(String.valueOf(((GameActionObject)obj).getId()));

        }
    }

    private void setUpLayout() {

        setLayout(new GridLayout(1,1));
        JScrollPane jsp = new JScrollPane(area);
        add(jsp);

    }
}
