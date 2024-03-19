package dev.nachwahl.bteginstaller;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class FinishForm{
    public JPanel FinishForm;
    private JLabel installiereLabel;
    private InstallUtil installUtil;

    public FinishForm(InstallUtil installUtil, JFrame frame, JDialog finish) {
        this.installUtil = installUtil;
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
            }
        });


    }

}
