package dev.nachwahl.bteginstaller;

import com.google.gson.Gson;

import javax.net.ssl.HttpsURLConnection;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
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
        if(optionalMods.contains(optionalMod)) {
            optionalMods.remove(optionalMod);
        }
    }

    public void addOptionalMod(OptionalMod optionalMod) {
        if(!optionalMods.contains(optionalMod)) {
            optionalMods.add(optionalMod);
        }
    }

    public void setOptionalMod(OptionalMod optionalMod, boolean active){
        if(active){
            addOptionalMod(optionalMod);
        }else{
            removeOptionalMod(optionalMod);
        }
    }

    public boolean isOptionalModEnabled(OptionalMod optionalMod){
        return optionalMods.contains(optionalMod);
    }

    public static synchronized void playSound(final String name) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    ClassLoader classloader = Thread.currentThread().getContextClassLoader();
                    InputStream is = classloader.getResourceAsStream(name);
                    Clip clip = AudioSystem.getClip();
                    AudioInputStream inputStream = AudioSystem.getAudioInputStream(is);
                    clip.open(inputStream);
                    FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                    gainControl.setValue(-30.0f);
                    clip.start();
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
        }).start();
    }


}

class InstallerInfo {
    public double version;
    public String downloadURL;
    InstallerInfo() {}
}
