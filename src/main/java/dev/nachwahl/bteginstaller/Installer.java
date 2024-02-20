package dev.nachwahl.bteginstaller;

import com.formdev.flatlaf.intellijthemes.FlatOneDarkIJTheme;
import com.formdev.flatlaf.json.Json;
import com.google.gson.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.*;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.*;
import java.util.List;

public class Installer {
    private static int modpackCount = 0;
    public static List<HashMap<String, String>> modpackData = new ArrayList<HashMap<String, String>>();
    public static void main(String[] args) throws IOException {

        FlatOneDarkIJTheme.install(new FlatOneDarkIJTheme());
        final JFrame frame = new JFrame("BTE Germany Installer");
        try {
            frame.setIconImage(ImageIO.read(MainForm.class.getResourceAsStream("/logo.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        downloadLatestConfig("https://cms.bte-germany.de/items/modpack");
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
    private static void downloadLatestConfig(String url) throws IOException {
        JsonObject root = readJsonFromUrl(url);
        root.getAsJsonArray().forEach(jsonElement -> parseModpackObject(jsonElement.getAsJsonObject()));
    }

    private static void parseModpackObject(JsonObject modpack) {
        HashMap<String,String> mp = new HashMap<String,String>();

        String id = String.valueOf(modpack.get("id").getAsInt());
        String label = modpack.get("label").getAsString();
        String modpackURL = modpack.get("modpackDownloadURL").getAsString();
        String fabricURL = modpack.get("fabricDownloadURL").getAsString();
        String fabricVersion = modpack.get("fabricLoaderVersion").getAsString();
        String bteVersion = modpack.get("bteGermanyModpackVersion").getAsString();
        JsonArray optionalMods = modpack.get("optionalMods").getAsJsonArray();
        JsonArray optionalShaders = modpack.get("optionalMods").getAsJsonArray();

        mp.put("id", id);
        mp.put("label" ,label);
        mp.put("modpackURL", modpackURL);
        mp.put("fabricURL", fabricURL);
        mp.put("fabricVersion", fabricVersion);
        mp.put("bteVersion", bteVersion);
        mp.put("optionalMods", optionalMods.getAsString());
        mp.put("optionalShaders", optionalShaders.getAsString());

        modpackCount = modpackCount + 1;
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static JsonObject readJsonFromUrl(String url) throws IOException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JsonObject json = JsonParser.parseString(jsonText).getAsJsonObject();
            return json;
        } finally {
            is.close();
        }
    }

}

