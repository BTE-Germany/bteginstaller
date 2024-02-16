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
import java.util.*;
import java.util.List;

public class Installer {
    private static int modpackCount = 0;
    public static List<HashMap<String, String>> modpackData = new ArrayList<HashMap<String, String>>();
    public static void main(String[] args) {

        FlatOneDarkIJTheme.install(new FlatOneDarkIJTheme());
        final JFrame frame = new JFrame("BTE Germany Installer");
        try {
            frame.setIconImage(ImageIO.read(MainForm.class.getResourceAsStream("/logo.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        downloadLatestConfig();
        Collections.reverse(modpackData);

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
    }

    private static void parseModpackObject(JsonObject modpack) {
        HashMap<String,String> mp = new HashMap<String,String>();

        JsonObject mpObject = (JsonObject) modpack.get("modpack");

        String label = (String) mpObject.get("label").getAsString();

        mp.put("label", label);
        String modpackURL = (String) mpObject.get("modpackDownloadURL").getAsString();
        mp.put("modpackDownloadURL", modpackURL);
        String fabricURL = (String) mpObject.get("fabricDownloadURL").getAsString();
        mp.put("fabricDownloadURL", fabricURL);
        String cmdKeybindURL = (String) mpObject.get("cmdKeybindURL").getAsString();
        mp.put("cmdKeybindURL", cmdKeybindURL);
        String replayModURL = (String) mpObject.get("replayModURL").getAsString();
        mp.put("replayModURL", replayModURL);
        String doubleHotbarURL = (String) mpObject.get("doubleHotbarURL").getAsString();
        mp.put("doubleHotbarURL", doubleHotbarURL);
        String customCrosshairURL = (String) mpObject.get("customCrosshairURL").getAsString();
        mp.put("customCrosshairURL", customCrosshairURL);
        String skin3dlayersURL = (String) mpObject.get("skin3dlayersURL").getAsString();
        mp.put("skin3dlayersURL", skin3dlayersURL);
        String fabricLoaderVersion = (String) mpObject.get("fabricLoaderVersion").getAsString();
        mp.put("fabricLoaderVersion", fabricLoaderVersion);
        String modpackVersion = (String) mpObject.get("bteGermanyModpackVersion").getAsString();
        mp.put("bteGermanyModpackVersion", modpackVersion);
        mp.put("modpackIndex", String.valueOf(modpackCount));

        modpackData.add(mp);

        modpackCount = modpackCount + 1;
    }
}

