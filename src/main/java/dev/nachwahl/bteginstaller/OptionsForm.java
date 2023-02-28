package dev.nachwahl.bteginstaller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OptionsForm {
    public JPanel OptionFormPanel;
    private JCheckBox commandMacrosCheckBox;
    private JButton speichernButton;
    private JCheckBox distantHorizonsCheckBox;
    private JCheckBox replayModCheckBox;
    private JDialog dialog;

    public OptionsForm(JDialog dialog,final InstallUtil installUtil) {
        this.dialog = dialog;
        commandMacrosCheckBox.setSelected(installUtil.isOptionalModEnabled(OptionalMod.COMMAND_MACROS));
        distantHorizonsCheckBox.setSelected(installUtil.isOptionalModEnabled(OptionalMod.DISTANT_HORIZONS));
        replayModCheckBox.setSelected(installUtil.isOptionalModEnabled(OptionalMod.REPLAY_MOD));
        commandMacrosCheckBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                installUtil.setOptionalMod(OptionalMod.COMMAND_MACROS,commandMacrosCheckBox.isSelected());
            }
        });

        distantHorizonsCheckBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                installUtil.setOptionalMod(OptionalMod.DISTANT_HORIZONS,distantHorizonsCheckBox.isSelected());
            }
        });

        replayModCheckBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                installUtil.setOptionalMod(OptionalMod.REPLAY_MOD,replayModCheckBox.isSelected());
            }
        });

        speichernButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                dialog.dispose();
            }
        });
    }
}
