package dev.nachwahl.bteginstaller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class MainForm {
    private JButton installModpackButton;
    private JButton selectOptionalModsButton;
    public JPanel MainFormPanel;
    private JComboBox modpackVersionDropDown;
    private JLabel versionText;
    private JButton selectOptionalShadersButton;
    public static String selectedItem;

    public MainForm(final JFrame frame, final InstallUtil installUtil) {

        frame.setResizable(false);
       // String versionNumber = "1.1";
       // versionText.setText("v" + versionNumber);

        for (Modpack m : Installer.modpacks) {
            String label = m.getLabel();
            modpackVersionDropDown.addItem(label);
        }

        selectedItem = modpackVersionDropDown.getSelectedItem().toString();

        modpackVersionDropDown.addActionListener(e -> {
            selectedItem = modpackVersionDropDown.getSelectedItem().toString();
        });

        selectOptionalModsButton.addActionListener(e -> {
            JDialog about = new JDialog(frame, "Optionale Mods", true);
            about.setContentPane(new OptionsForm(about,installUtil).OptionFormPanel);
            about.setSize(500, 300);
            about.setLocationRelativeTo(null);
            about.setVisible(true);
            about.toFront();
            about.repaint();
        });
        selectOptionalShadersButton.addActionListener(e -> {
            JDialog about = new JDialog(frame, "Optionale Shader", true);
            about.setContentPane(new ShadersForm(about,installUtil).ShaderFormPanel);
            about.setSize(500, 300);
            about.setLocationRelativeTo(null);
            about.setVisible(true);
            about.toFront();
            about.repaint();
        });
        installModpackButton.addActionListener(e -> {
            String modpackVersion = modpackVersionDropDown.getSelectedItem().toString();
            JDialog loading = new JDialog(frame, "Installiere...", true);
            loading.setContentPane(new LoadingForm(installUtil, frame, loading, modpackVersion).LoadingForm);
            loading.setResizable(false);
            loading.setSize(500, 150);
            loading.setLocationRelativeTo(null);
            loading.setVisible(true);
            loading.toFront();
            loading.repaint();

        });


    }

}
