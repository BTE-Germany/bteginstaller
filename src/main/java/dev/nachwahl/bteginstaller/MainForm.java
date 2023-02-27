package dev.nachwahl.bteginstaller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainForm {
    private JLabel versionText;
    private JButton installModpackButton;
    private JButton selectOptionalModsButton;
    public JPanel MainFormPanel;

    public MainForm(final JFrame frame, final InstallUtil installUtil) {
        String versionNumber = installUtil.getVersionNumber();
        versionText.setText("v" + versionNumber);
        selectOptionalModsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JDialog  about = new JDialog(frame, "Optionale Mods", true);
                about.setContentPane(new OptionsForm().OptionFormPanel);
                about.setSize(500, 300);
                about.setVisible(true);
                about.toFront();
                about.repaint();
            }
        });
        installModpackButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                installUtil.startInstallation();
            }
        });
    }
}
