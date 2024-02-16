package dev.nachwahl.bteginstaller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OptionsForm {
    public JPanel OptionFormPanel;
    private JCheckBox commandMacrosCheckBox;
    private JCheckBox doubleHotbarCheckBox;
    private JCheckBox customCrosshairCheckBox;
    private JCheckBox skin3dLayersCheckBox;
    private JButton speichernButton;
    private JCheckBox replayModCheckBox;
    private JDialog dialog;

    public OptionsForm(JDialog dialog,final InstallUtil installUtil) {
        this.dialog = dialog;
        commandMacrosCheckBox.setSelected(installUtil.isOptionalModEnabled(OptionalMod.COMMAND_MACROS));
        replayModCheckBox.setSelected(installUtil.isOptionalModEnabled(OptionalMod.REPLAY_MOD));
        doubleHotbarCheckBox.setSelected(installUtil.isOptionalModEnabled(OptionalMod.DOUBLE_HOTBAR));
        customCrosshairCheckBox.setSelected(installUtil.isOptionalModEnabled(OptionalMod.CUSTOM_CROSSHAIR));
        skin3dLayersCheckBox.setSelected(installUtil.isOptionalModEnabled(OptionalMod.SKIN_3DLAYERS));
        commandMacrosCheckBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                installUtil.setOptionalMod(OptionalMod.COMMAND_MACROS,commandMacrosCheckBox.isSelected());
            }
        });

        replayModCheckBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                installUtil.setOptionalMod(OptionalMod.REPLAY_MOD,replayModCheckBox.isSelected());
            }
        });

        doubleHotbarCheckBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                installUtil.setOptionalMod(OptionalMod.DOUBLE_HOTBAR,doubleHotbarCheckBox.isSelected());
            }
        });

        customCrosshairCheckBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                installUtil.setOptionalMod(OptionalMod.CUSTOM_CROSSHAIR,customCrosshairCheckBox.isSelected());
            }
        });
        skin3dLayersCheckBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                installUtil.setOptionalMod(OptionalMod.SKIN_3DLAYERS,skin3dLayersCheckBox.isSelected());
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
