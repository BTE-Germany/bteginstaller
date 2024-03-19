package dev.nachwahl.bteginstaller;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class OptionsForm {
    public JPanel OptionFormPanel;
    private JButton speichernButton;
    private JPanel DATA;
    private JDialog dialog;

    public OptionsForm(JDialog dialog, final InstallUtil installUtil) {
        this.dialog = dialog;

        DATA.setLayout(new BorderLayout()); // Setzt das Hauptlayout zu BorderLayout

        JPanel centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER; // Beendet die Zeile
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1; // Verteilt den zusätzlichen horizontalen Raum

        for (Modpack m : Installer.modpacks) {
            if (m.getLabel().equals(MainForm.selectedItem)) {
                for (int i = 0; i < m.getOptionalMods().size(); i++) {
                    HashMap<String, String> hm = m.getOptionalMods().get(i);
                    JCheckBox checkbox = new JCheckBox();
                    checkbox.setName(hm.get("label"));
                    checkbox.setText(hm.get("label"));
                    checkbox.setSelected("true".equals(hm.get("on")));
                    checkbox.addActionListener(e -> hm.put("on", checkbox.isSelected() ? "true" : "false"));

                    JPanel modPanel = new JPanel();
                    modPanel.setLayout(new BoxLayout(modPanel, BoxLayout.Y_AXIS));
                    modPanel.add(checkbox);

                    // Beschreibung in kleinerer und kursiver Schrift
                    JLabel descriptionLabel = new JLabel("       " + hm.get("desc"));
                    Font currentFont = descriptionLabel.getFont();
                    descriptionLabel.setFont(currentFont.deriveFont(Font.ITALIC, currentFont.getSize() - 2f));
                    modPanel.add(descriptionLabel);

                    centerPanel.add(modPanel, gbc);
                }
            }
        }

        gbc.weighty = 1;  // Wichtig, um zu gewährleisten, dass Komponenten oben starten und der Platz unten bleibt
        centerPanel.add(Box.createGlue(), gbc); // Fügt den Klebstoff hinzu, um den Inhalt zu zentrieren

        DATA.add(centerPanel, BorderLayout.CENTER); // Fügt das centerPanel in der Mitte hinzu

        speichernButton.addActionListener(e -> {
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.dispose();
        });
    }
}

