package dev.nachwahl.bteginstaller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class MainForm {
    private JLabel versionText;
    private JButton installModpackButton;
    private JButton selectOptionalModsButton;
    public JPanel MainFormPanel;

    public MainForm(final JFrame frame, final InstallUtil installUtil) {

        frame.setResizable(false);
        String versionNumber = "1.0";
        versionText.setText("v" + versionNumber);
        selectOptionalModsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JDialog about = new JDialog(frame, "Optionale Mods", true);
                about.setContentPane(new OptionsForm(about,installUtil).OptionFormPanel);
                about.setResizable(false);
                about.setSize(500, 300);
                about.setLocationRelativeTo(null);
                about.setVisible(true);
                about.toFront();
                about.repaint();
            }
        });
        installModpackButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JDialog loading = new JDialog(frame, "Installiere...", true);
                loading.setContentPane(new LoadingForm(installUtil, frame, loading).LoadingForm);
                loading.setResizable(false);
                loading.setSize(500, 150);
                loading.setLocationRelativeTo(null);
                loading.setVisible(true);
                loading.toFront();
                loading.repaint();

            }
        });
    }
}
