package dev.nachwahl.bteginstaller;

import com.google.gson.*;
import de.schlichtherle.truezip.file.TArchiveDetector;
import de.schlichtherle.truezip.file.TFile;
import de.schlichtherle.truezip.file.TFileInputStream;
import org.apache.commons.io.FilenameUtils;

import javax.swing.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
public class InstallTask extends SwingWorker<Void, Integer> {
    String modpackDownloadURL;
    String fabricDownloadURL;
    String cmdKeybindURL;
    String replayModURL;
    String fabricLoaderVersion;
    String bteGermanyModpackVersion;
    InstallUtil installUtil;
    JProgressBar progressBar;
    JLabel progessLabel;
    JDialog progressDialog;
    String fileSeparator;
    String modpackVersion;
    public InstallTask(InstallUtil installUtil, JProgressBar progressBar, JLabel progessLabel, JDialog progressDialog, String modpackVersion) {
        this.installUtil = installUtil;
        this.progressBar = progressBar;
        this.progessLabel = progessLabel;
        this.progressDialog = progressDialog;
        this.modpackVersion = modpackVersion;
    }

    @Override
    protected Void doInBackground() {
        switch (modpackVersion) {
            case "1.19.3":
                modpackDownloadURL = "https://cdn.bte-germany.de/installer/1.19/modpack.zip";
                fabricDownloadURL = "https://cdn.bte-germany.de/installer/1.19/fabric.zip";
                cmdKeybindURL = "https://cdn.modrinth.com/data/h3r1moh7/versions/y3emEjYR/cmdkeybind-1.6.0-1.19.3.jar";
                replayModURL = "https://cdn.modrinth.com/data/Nv2fQJo5/versions/EcNOFu8c/replaymod-1.19.3-2.6.10.jar";
                fabricLoaderVersion = "fabric-loader-0.14.14-1.19.3";
                bteGermanyModpackVersion = "BTE Germany v1.0";
                break;
            case "1.20.1 (latest)":
                modpackDownloadURL = "https://cdn.bte-germany.de/installer/1.20.1/modpack.zip";
                fabricDownloadURL = "https://cdn.bte-germany.de/installer/1.20.1/fabric.zip";
                cmdKeybindURL = "https://cdn.modrinth.com/data/h3r1moh7/versions/snLr0hHP/cmdkeybind-1.6.3-1.20.jar";
                replayModURL = "https://cdn.modrinth.com/data/Nv2fQJo5/versions/akFkhrL8/replaymod-1.20.1-2.6.13.jar";
                fabricLoaderVersion = "fabric-loader-0.14.21-1.20.1";
                bteGermanyModpackVersion = "BTE Germany v1.1";
                break;
            default:
                throw new RuntimeException("Modpack version not supported");
        }
        fileSeparator = FileSystems.getDefault().getSeparator();
        File installationPath = getMinecraftDir("btegermany").toFile();
        File minecraftPath = getMinecraftDir("minecraft").toFile();
        if (!installationPath.exists()) {
            installationPath.mkdir();
        }

        try {
            // MODPACK
            deleteOldFiles(installationPath);
            downloadModpack(modpackDownloadURL, installationPath);
            unzipModpack(installationPath);
            //unzipFile(Paths.get(new File(installationPath.getAbsolutePath() + fileSeparator + "modpack.zip").getPath()));
            File modpackArchive = new File(installationPath.getAbsolutePath() + fileSeparator + "modpack.zip");
            modpackArchive.delete();
            downloadOptionalMods(installationPath + fileSeparator + "mods" + fileSeparator);

            // FABRIC
            downloadFabric(minecraftPath.getAbsolutePath().toString(), new URL(fabricDownloadURL));
            //unzipFile(Paths.get(new File(minecraftPath.getAbsolutePath() + fileSeparator + "versions"+ fileSeparator + "fabric.zip").getPath()));
            unzipFabric(minecraftPath);
            File fabricArchive = new File(minecraftPath.getAbsolutePath() + fileSeparator + "versions"+ fileSeparator + "version.zip");
            fabricArchive.delete();
            editLauncherProfiles(minecraftPath, installationPath);

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public void done() {
        InstallUtil.playSound("notification.wav");
        progressDialog.setTitle("Installation/Update abgeschlossen");
        progessLabel.setText("Installation/Update abgeschlossen");
        progressBar.setValue(100);
    }

    private static Path getMinecraftDir(String mcFolderNanme) {
        String home = System.getProperty("user.home", ".");
        String os = System.getProperty("os.name").toLowerCase(Locale.ROOT);
        if (os.contains("win") && System.getenv("APPDATA") != null) {
            return Paths.get(System.getenv("APPDATA"), "."+mcFolderNanme);
        } else if (os.contains("mac")) {
            return Paths.get(home, "Library", "Application Support", mcFolderNanme);
        }
        return Paths.get(home, "."+mcFolderNanme);
    }

    private boolean deleteOldFiles(File installationPath) {
        progessLabel.setText("Lösche alte Dateien...");
        ArrayList<File> files = new ArrayList<>();
        files.add(new File(installationPath.getAbsolutePath() + fileSeparator + "config"));
        files.add(new File(installationPath.getAbsolutePath() + fileSeparator + "fancymenu_data"));
        files.add(new File(installationPath.getAbsolutePath() + fileSeparator + "fancymenu_setups"));
        files.add(new File(installationPath.getAbsolutePath() + fileSeparator + "mods"));
        files.add(new File(installationPath.getAbsolutePath() + fileSeparator + "resources"));
        for (File file : files) {
            if (file.isDirectory()) {
                try {
                    deleteDirectoryRecursion(file.toPath());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    private boolean downloadModpack(String modpackDownloadURL, File installationPath) throws IOException {
        progessLabel.setText("Downloade Modpack...");
        URL url = new URL(modpackDownloadURL);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36");
        long fileSize = httpURLConnection.getContentLength();
        long downloadedFileSize = 0;
        BufferedInputStream in = new BufferedInputStream(httpURLConnection.getInputStream());
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(installationPath.getAbsolutePath() + fileSeparator + "modpack.zip"), 1024);
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = in.read(buffer)) != -1) {
            out.write(buffer, 0, bytesRead);
            downloadedFileSize++;
            progressBar.setValue((int) ((((double) downloadedFileSize) / ((double) fileSize)) * 100000d));
        }
        in.close();
        out.close();
        return true;
    }

    private boolean unzipModpack(File installationPath) throws IOException {
        System.out.println("Entpacke Modpack...");
        ZipInputStream zis = new ZipInputStream(new FileInputStream(installationPath.getAbsolutePath() + fileSeparator + "modpack.zip"));
        ZipEntry zipEntry = zis.getNextEntry();
        long fileSize = zipEntry.getCompressedSize();

        while (zipEntry != null) {
            String fileName = zipEntry.getName();
            String filePath = installationPath + fileSeparator + fileName;
            System.out.println("Extracting file: " + filePath);
            File file = new File(filePath);

            if (!file.exists()) {
                if (zipEntry.isDirectory()) {
                    file.mkdir();
                    System.out.println("Created directory: " + filePath);
                } else {
                    long zipFileSize = 0;

                    FileOutputStream fos = new FileOutputStream(filePath);
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = zis.read(buffer)) != -1) {
                        fos.write(buffer, 0, bytesRead);
                        zipFileSize++;
                        progressBar.setValue((int) ((((double) zipFileSize) / ((double) fileSize)) * 100000d));
                    }
                    fos.close();
                    System.out.println("Extracted file: " + filePath);
                }
            }
            zipEntry = zis.getNextEntry();
        }

        zis.closeEntry();
        zis.close();
        System.out.println("Modpack extraction complete.");
        return true;
/*

        System.out.println("Entpacke Modpack...");

        TFile archive = new TFile(installationPath.getAbsolutePath() + fileSeparator + "modpack.zip");
        TFile directory = new TFile(installationPath);
        TFile.cp_rp(archive, directory, TArchiveDetector.NULL, TArchiveDetector.NULL);

        System.out.println("Modpack extraction complete.");
        return true;

         */
    }
    /*
    private static void unzipFile(Path filePathToUnzip) {

        Path targetDir = filePathToUnzip.getParent();

        //Open the file
        try (ZipFile zip = new ZipFile(filePathToUnzip.toFile())) {

            FileSystem fileSystem = FileSystems.getDefault();
            Enumeration<? extends ZipEntry> entries = zip.entries();

            //We will unzip files in this folder
            if (!targetDir.toFile().isDirectory()
                    && !targetDir.toFile().mkdirs()) {
                throw new IOException("failed to create directory " + targetDir);
            }

            //Iterate over entries
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                File f = new File(targetDir.resolve(Paths.get(entry.getName())).toString());

                //If directory then create a new directory in uncompressed folder
                if (entry.isDirectory()) {
                    if (!f.isDirectory() && !f.mkdirs()) {
                        throw new IOException("failed to create directory " + f);
                    }
                }

                //Else create the file
                else {
                    File parent = f.getParentFile();
                    if (!parent.isDirectory() && !parent.mkdirs()) {
                        throw new IOException("failed to create directory " + parent);
                    }

                    try(InputStream in = zip.getInputStream(entry)) {
                        Files.copy(in, f.toPath());
                    }

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

     */

    private boolean downloadOptionalMods(String modsFolder) throws IOException {
        if (installUtil.isOptionalModEnabled(OptionalMod.COMMAND_MACROS)) {
            downloadMod(modsFolder, new URL(cmdKeybindURL));
        }
        if (installUtil.isOptionalModEnabled(OptionalMod.REPLAY_MOD)) {
            downloadMod(modsFolder, new URL(replayModURL));
        }
        return true;
    }

    private boolean downloadMod(String modsFolder, URL url) throws IOException {
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36");
        long fileSize = httpURLConnection.getContentLength();
        long downloadedFileSize = 0;
        String modName = new File(url.getPath().toString()).getName();

        progessLabel.setText("Downloade " + modName + "...");
        BufferedInputStream in = new BufferedInputStream(httpURLConnection.getInputStream());
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(modsFolder + fileSeparator + modName), 1024);
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = in.read(buffer)) != -1) {
            out.write(buffer, 0, bytesRead);
            downloadedFileSize++;
            progressBar.setValue((int) ((((double) downloadedFileSize) / ((double) fileSize)) * 100000d));
        }
        in.close();
        out.close();
        return true;
    }

    private boolean downloadFabric(String minecraftFolder,URL url) throws IOException{
        progessLabel.setText("Downloade Fabric...");
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36");
        long fileSize = httpURLConnection.getContentLength();
        long downloadedFileSize = 0;
        BufferedInputStream in = new BufferedInputStream(httpURLConnection.getInputStream());
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(minecraftFolder + fileSeparator + "versions"+ fileSeparator + "version.zip"), 1024);
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = in.read(buffer)) != -1) {
            out.write(buffer, 0, bytesRead);
            downloadedFileSize++;
            progressBar.setValue((int) ((((double) downloadedFileSize) / ((double) fileSize)) * 100000d));
        }
        in.close();
        out.close();
        return true;
    }

    private boolean unzipFabric(File minecraftFolder) throws IOException {
        progessLabel.setText("Entpacke Fabric...");
        ZipInputStream zis = new ZipInputStream(new FileInputStream(minecraftFolder + fileSeparator + "versions" + fileSeparator + "version.zip"));
        ZipEntry zipEntry = zis.getNextEntry();
        long fileSize = zipEntry.getCompressedSize();

        while (zipEntry != null) {
            String fileName = zipEntry.getName();
            String filePath = minecraftFolder + fileSeparator + "versions" +  fileSeparator + fileName;

            if(!new File(filePath).exists()) {
                if (zipEntry.isDirectory()) {
                    File dir = new File(filePath);
                    dir.mkdir();
                } else {
                    long zipFileSize = 0;

                    FileOutputStream fos = new FileOutputStream(filePath);
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = zis.read(buffer)) != -1) {
                        fos.write(buffer, 0, bytesRead);
                        zipFileSize++;
                        progressBar.setValue((int) ((((double) zipFileSize) / ((double) fileSize)) * 100000d));
                    }
                    fos.close();
                }
            }
            zipEntry = zis.getNextEntry();
        }

        zis.closeEntry();
        zis.close();
        return true;
    }

    private boolean editLauncherProfiles(File minecraftFolder, File installationPath){
        progessLabel.setText("Füge Launcher Profil hinzu...");
        try {
            FileReader reader = new FileReader(minecraftFolder + fileSeparator + "launcher_profiles.json");
            Gson gson = new Gson();
            JsonParser parser = new JsonParser();

            JsonObject newEntry = new JsonObject();

            newEntry.addProperty("gameDir", installationPath.getAbsolutePath().toString());
            newEntry.addProperty("icon", "data:image/png;base64,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");
            newEntry.addProperty("javaArgs","-Xss4M -Xmx8G -XX:+DisableExplicitGC -XX:+UnlockExperimentalVMOptions -XX:+UseG1GC -XX:G1NewSizePercent=20 -XX:G1ReservePercent=20 -XX:MaxGCPauseMillis=30 -XX:G1HeapRegionSize=32M -Duser.country=US -Duser.language=en");
            newEntry.addProperty("lastVersionId", fabricLoaderVersion);
            newEntry.addProperty("name", bteGermanyModpackVersion);
            newEntry.addProperty("type", "custom");

            JsonElement element = parser.parse(reader);

            element.getAsJsonObject().get("profiles").getAsJsonObject().add("BTE Germany", newEntry);

            FileWriter writer = new FileWriter(minecraftFolder + fileSeparator + "launcher_profiles.json");
            gson.toJson(element, writer);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    private void deleteDirectoryRecursion(Path path) throws IOException {
        if (Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS)) {
            try (DirectoryStream<Path> entries = Files.newDirectoryStream(path)) {
                for (Path entry : entries) {
                    deleteDirectoryRecursion(entry);
                }
            }
        }
        Files.delete(path);
    }
}
