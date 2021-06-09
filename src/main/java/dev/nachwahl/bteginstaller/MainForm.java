package dev.nachwahl.bteginstaller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainForm {
    public JPanel MainFormPanel;
    private JButton modpackInstallierenButton;
    private JButton optionenButton;

    public MainForm(final JFrame frame) {
        optionenButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JDialog  about = new JDialog(frame, "Optionale Mods", true);
                about.setContentPane(new OptionsForm().OptionFormPanel);
                about.setSize(500, 300);
                about.setVisible(true);
                about.toFront();
                about.repaint();
            }
        });
    }
}
