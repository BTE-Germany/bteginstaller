package dev.nachwahl.bteginstaller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class ShadersForm {
    private JPanel DATA;
    private JButton speichernButton;
    JPanel ShaderFormPanel;
    private JDialog dialog;
    public ShadersForm(JDialog dialog,final InstallUtil installUtil) {
        this.dialog = dialog;

        DATA.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER; // Endet die Zeile
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1; // Verteilt den zusätzlichen horizontalen Raum
        gbc.weighty = 1;

        for (Modpack m : Installer.modpacks) {
            if (m.getLabel().equals(MainForm.selectedItem)) {
                for (int i = 0; i < m.getOptionalShaders().size(); i++) {
                    HashMap<String, String> hm = m.getOptionalShaders().get(i);
                    JCheckBox checkbox = new JCheckBox(hm.get("label"));
                    checkbox.setSelected("true".equals(hm.get("on")));
                    checkbox.addActionListener(e -> hm.put("on", checkbox.isSelected() ? "true" : "false"));

                    // Für alle Elemente außer dem letzten, setzen Sie weighty = 0
                    if (i < m.getOptionalShaders().size() - 1) {
                        gbc.weighty = 0;
                    }

                    DATA.add(checkbox, gbc);
                }

            }
        }


        speichernButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                dialog.dispose();
            }
        });
    }
}
