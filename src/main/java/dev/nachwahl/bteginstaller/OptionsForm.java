package dev.nachwahl.bteginstaller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;

public class OptionsForm {
    public JPanel OptionFormPanel;
    private JButton speichernButton;
    private JPanel DATA;
    private JDialog dialog;

    public OptionsForm(JDialog dialog,final InstallUtil installUtil) {
        this.dialog = dialog;

        DATA.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER; // Endet die Zeile
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1; // Verteilt den zusätzlichen horizontalen Raum
        gbc.weighty = 1;

        for (Modpack m : Installer.modpacks) {
            if (m.getLabel().equals(MainForm.selectedItem)) {
                for (int i = 0; i < m.getOptionalMods().size(); i++) {
                    HashMap<String, String> hm = m.getOptionalMods().get(i);
                    JCheckBox checkbox = new JCheckBox();
                    checkbox.setName(hm.get("label"));
                    checkbox.setText(hm.get("label") + " | " + hm.get("desc"));
                    checkbox.setSelected("true".equals(hm.get("enabled")));

                    checkbox.addActionListener(e -> hm.put("on", checkbox.isSelected() ? "true" : "false"));

                    if (i < hm.size() - 1) {
                        gbc.weighty = 0;
                    }

                    DATA.add(checkbox, gbc);
                }

            }
        }

        speichernButton.addActionListener(e -> {
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.dispose();
        });

    }

}
