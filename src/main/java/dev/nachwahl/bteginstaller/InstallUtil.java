package dev.nachwahl.bteginstaller;

import com.google.gson.Gson;
import jdk.nashorn.internal.parser.JSONParser;

import javax.net.ssl.HttpsURLConnection;
import javax.swing.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

public class InstallUtil {

    ArrayList<OptionalMod> optionalMods = new ArrayList<OptionalMod>();
    String versionurl = "";
    JFrame frame;
    InstallerInfo installerInfo;

    public InstallUtil(JFrame frame, String versionurl) {
        this.versionurl = versionurl;
        this.frame = frame;
        System.out.println("Init InstallUtil");
        fetchInstallerInfo();
    }

    public InstallerInfo getInstallerInfo() {
        return installerInfo;
    }

    private void fetchInstallerInfo() {
        try {
            URL url = new URL(this.versionurl);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            String inline = "";
            int responseCode = connection.getResponseCode();
            if(responseCode != 200) {
                throw new IOException("Version getting failed with code " + responseCode);
            } else {
                Scanner sc = new Scanner(url.openStream());
                while (sc.hasNext()) {
                    inline += sc.nextLine();
                }
                System.out.println(inline);
                sc.close();
                Gson gson = new Gson();
                this.installerInfo = gson.fromJson(inline, InstallerInfo.class);
            }

        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Es ist ein Fehler aufgetreten. Error: " + e.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
            System.exit(-1);
        }
    }


    public String getVersionNumber() {
        InstallerInfo installerInfo = getInstallerInfo();
        return String.valueOf(installerInfo.version);

    }

    public void removeOptionalMod(OptionalMod optionalMod) {
        System.out.println("Removing optional mod: " + optionalMod.toString());
        if(optionalMods.contains(optionalMod)) {
            optionalMods.remove(optionalMod);
        }
    }

    public void addOptionalMod(OptionalMod optionalMod) {
        System.out.println("Adding optional mod: " + optionalMod.toString());
        if(!optionalMods.contains(optionalMod)) {
            optionalMods.add(optionalMod);
        }
    }

    public void startInstallation() {
        JDialog loading = new JDialog(this.frame, "Installiere...", true);
        loading.setContentPane(new LoadingForm().LoadingForm);
        loading.setSize(500, 150);
        loading.setVisible(true);
        loading.toFront();
        loading.repaint();
    }


}

class InstallerInfo {
    public double version;
    public String downloadURL;
    InstallerInfo() {}
}
