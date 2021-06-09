package dev.nachwahl.bteginstaller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OptionsForm {
    public JPanel OptionFormPanel;
    private JCheckBox optiFineHDUG5CheckBox;
    private JButton speichernButton;
    private JCheckBox schematicaCheckBox;
    private JCheckBox replayModCheckBox;
    private JDialog dialog;

    public OptionsForm(JDialog dialog) {
        this.dialog = dialog;
    }

    public OptionsForm() {
        speichernButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });
    }
}
