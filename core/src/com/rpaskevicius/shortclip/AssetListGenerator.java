package com.rpaskevicius.shortclip;

import java.io.File;
import java.io.FilenameFilter;

public class AssetListGenerator {
    public AssetListGenerator() {
        File dir = new File("C:\\Users\\Romas\\IdeaProjects\\ShortClip\\android\\assets");

        File[] files = dir.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".wav");
            }
        });

        for (File file : files) {
            System.out.println("\"" + file.getName() + "\",");
        }

    }
}
