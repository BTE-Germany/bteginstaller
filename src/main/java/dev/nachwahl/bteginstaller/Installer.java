package dev.nachwahl.bteginstaller;

import com.formdev.flatlaf.FlatDarculaLaf;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;

public class Installer {
    public static void main(String[] args) {
        FlatDarculaLaf.install();
        final JFrame frame = new JFrame("BTE Germany Installer");
        frame.setContentPane(new MainForm(frame).MainFormPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 300);
        JMenuBar mb = new JMenuBar();
        JMenu about = new JMenu("About");
        about.addMenuListener(new MenuListener() {
            public void menuSelected(MenuEvent e) {
                JDialog  about = new JDialog(frame, "About", true);
                about.setContentPane(new AboutForm().AboutPanel);
                about.setSize(800, 800);
                about.setVisible(true);
                about.toFront();
                about.repaint();
            }

            public void menuDeselected(MenuEvent e) {

            }

            public void menuCanceled(MenuEvent e) {

            }
        });
        mb.add(about);

        frame.setJMenuBar(mb);
        try {
            frame.setIconImage(ImageIO.read(MainForm.class.getResourceAsStream("/logo.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        frame.setVisible(true);
    }
}
