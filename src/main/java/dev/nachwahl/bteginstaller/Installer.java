package dev.nachwahl.bteginstaller;

import com.formdev.flatlaf.intellijthemes.FlatOneDarkIJTheme;
import com.formdev.flatlaf.json.Json;
import com.google.gson.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class Installer {
    private static int modpackCount = 0;
    public static void main(String[] args) {

        FlatOneDarkIJTheme.install(new FlatOneDarkIJTheme());
        final JFrame frame = new JFrame("BTE Germany Installer");
        try {
            frame.setIconImage(ImageIO.read(MainForm.class.getResourceAsStream("/logo.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        InstallUtil installUtil = new InstallUtil(frame);
        frame.setContentPane(new MainForm(frame, installUtil).MainFormPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.setLocationRelativeTo(null);

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

        downloadLatestConfig();

        frame.setVisible(true);
    }
    private static void downloadLatestConfig() {
        JsonParser jsonParser = new JsonParser();

        try (FileReader reader = new FileReader("C:/Users/hugoh/Desktop/modpack.json")) {
            Gson gson = new Gson();
            JsonParser parser = new JsonParser();
            JsonElement jel = parser.parse(reader);
            JsonArray instances = jel.getAsJsonObject().get("instances").getAsJsonArray();

            instances.forEach(jsonElement ->  parseModpackObject((JsonObject) jsonElement));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(modpackCount + " modpacks found!");


    }

    private static void parseModpackObject(JsonObject modpack) {

        JsonObject mpObject = (JsonObject) modpack.get("modpack");

        String label = (String) mpObject.get("label").getAsString();

        String modpackURL = (String) mpObject.get("modpackDownloadURL").getAsString();

        String fabricURL = (String) mpObject.get("fabricDownloadURL").getAsString();

        String cmdKeybindURL = (String) mpObject.get("cmdKeybingURL").getAsString();

        String replayModURL = (String) mpObject.get("replayModURL").getAsString();

        String doubleHotbarURL = (String) mpObject.get("doubleHotbarURL").getAsString();

        String customCrosshairURL = (String) mpObject.get("customCrosshairURL").getAsString();

        String skin3dlayersURL = (String) mpObject.get("skin3dlayersURL").getAsString();

        String fabricLoaderVersion = (String) mpObject.get("fabricLoaderVersion").getAsString();

        String modpackVersion = (String) mpObject.get("bteGermanyModpackVersion").getAsString();

        modpackCount = modpackCount + 1;

    }
}

