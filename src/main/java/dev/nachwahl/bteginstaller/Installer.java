package dev.nachwahl.bteginstaller;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.intellijthemes.FlatOneDarkIJTheme;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class Installer {




    public static void main(String[] args) {

        final String VERSIONURL = "https://bte-germany.de/api/getInstallerInfo";

        FlatOneDarkIJTheme.install(new FlatOneDarkIJTheme());
        final JFrame frame = new JFrame("BTE Germany Installer");
        try {
            frame.setIconImage(ImageIO.read(MainForm.class.getResourceAsStream("/logo.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        InstallUtil installUtil = new InstallUtil(frame, VERSIONURL);

        frame.setContentPane(new MainForm(frame, installUtil).MainFormPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 300);
        JMenuBar mb = new JMenuBar();
        JMenu about = new JMenu("About");
        about.addMenuListener(new MenuListener() {
            public void menuSelected(MenuEvent e) {
                Desktop desktop = java.awt.Desktop.getDesktop();
                try {

                    URI uri = new URI(
                            "https://buildthe.earth/installer-licenses");
                    desktop.browse(uri);
                } catch (URISyntaxException ex) {
                    throw new RuntimeException(ex);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }


            }

            public void menuDeselected(MenuEvent e) {

            }

            public void menuCanceled(MenuEvent e) {

            }
        });
        mb.add(about);

        frame.setJMenuBar(mb);



        frame.setVisible(true);
    }
}
