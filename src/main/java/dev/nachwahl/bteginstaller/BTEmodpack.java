package dev.nachwahl.bteginstaller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BTEmodpack implements Modpack {
    private String id;
    private String label;
    private String modpackURL;
    private String fabricURL;
    private String fabricVersion;
    private String bteVersion;
    private List<HashMap<String, String>> optionalMods;
    private List<HashMap<String, String>> optionalShaders;

    public BTEmodpack(String id, String label, String modpackURL, String fabricURL,
                        String fabricVersion, String bteVersion,
                        List<HashMap<String, String>> optionalMods,
                        List<HashMap<String, String>> optionalShaders) {
        this.id = id;
        this.label = label;
        this.modpackURL = modpackURL;
        this.fabricURL = fabricURL;
        this.fabricVersion = fabricVersion;
        this.bteVersion = bteVersion;
        this.optionalMods = optionalMods;
        this.optionalShaders = optionalShaders;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public String getModpackURL() {
        return modpackURL;
    }

    @Override
    public String getFabricURL() {
        return fabricURL;
    }

    @Override
    public String getFabricVersion() {
        return fabricVersion;
    }

    @Override
    public String getBteVersion() {
        return bteVersion;
    }

    @Override
    public List<HashMap<String, String>> getOptionalMods() {
        return optionalMods;
    }

    @Override
    public List<HashMap<String, String>> getOptionalShaders() {
        return optionalShaders;
    }
}