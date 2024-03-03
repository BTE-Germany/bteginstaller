package dev.nachwahl.bteginstaller;

import java.util.HashMap;
import java.util.List;

public interface Modpack {
    String getId();
    String getLabel();
    String getModpackURL();
    String getFabricURL();
    String getFabricVersion();
    String getBteVersion();
    List<HashMap<String, String>> getOptionalMods();
    List<HashMap<String, String>> getOptionalShaders();
}



