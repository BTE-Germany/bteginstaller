/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
import com.formdev.flatlaf.FlatDefaultsAddon;

module com.formdev.flatlaf {
    requires java.desktop; // version: 11.0.10

    exports com.formdev.flatlaf;
    exports com.formdev.flatlaf.icons;
    exports com.formdev.flatlaf.ui;
    exports com.formdev.flatlaf.util;

    opens com.formdev.flatlaf.resources;

    uses FlatDefaultsAddon;

}

