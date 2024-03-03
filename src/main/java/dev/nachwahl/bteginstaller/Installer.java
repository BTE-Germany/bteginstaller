package dev.nachwahl.bteginstaller;

import com.formdev.flatlaf.intellijthemes.FlatOneDarkIJTheme;
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
    public static List<Modpack> modpacks = new ArrayList<Modpack>();

    public static void main(String[] args) throws IOException {

        FlatOneDarkIJTheme.install(new FlatOneDarkIJTheme());
        final JFrame frame = new JFrame("BTE Germany Installer");
        try {
            frame.setIconImage(ImageIO.read(MainForm.class.getResourceAsStream("/logo.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        downloadLatestConfig("https://cms.bte-germany.de/items/modpack");
        Collections.reverse(modpacks);

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
        JsonArray root = readJsonFromUrl(url).get("data").getAsJsonArray();
        root.forEach(jsonElement -> parseModpackObject(jsonElement.getAsJsonObject()));
    }

    private static void parseModpackObject(JsonObject modpack) {
        String id = modpack.get("id").getAsString();
        String label = modpack.get("label").getAsString();
        String modpackURL = modpack.get("modpackDownloadURL").getAsString();
        String fabricURL = modpack.get("fabricDownloadURL").getAsString();
        String fabricVersion = modpack.get("fabricLoaderVersion").getAsString();
        String bteVersion = modpack.get("bteGermanyModpackVersion").getAsString();

        List<HashMap<String, String>> optionalMods = new ArrayList<>();
        modpack.get("optionalMods").getAsJsonArray().forEach(modElement -> {
            HashMap<String, String> mod = new HashMap<>();
            mod.put("label", modElement.getAsJsonObject().get("label").getAsString());
            mod.put("desc", modElement.getAsJsonObject().get("desc").getAsString());
            mod.put("url", modElement.getAsJsonObject().get("url").getAsString());
            mod.put("on", "false");
            optionalMods.add(mod);
        });

        List<HashMap<String, String>> optionalShaders = new ArrayList<>();
        modpack.get("optionalShaders").getAsJsonArray().forEach(shaderElement -> {
            HashMap<String, String> shader = new HashMap<>();
            shader.put("label", shaderElement.getAsJsonObject().get("label").getAsString());
            shader.put("url", shaderElement.getAsJsonObject().get("url").getAsString());
            shader.put("on", "false");
            optionalShaders.add(shader);
        });

        BTEmodpack fin = new BTEmodpack(id, label, modpackURL, fabricURL, fabricVersion, bteVersion, optionalMods, optionalShaders);

        modpacks.add(fin);
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

