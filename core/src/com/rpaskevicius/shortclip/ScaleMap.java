package com.rpaskevicius.shortclip;

public class ScaleMap {

    public static final String[] scales = new String[] { "c", "c#", "d", "d#", "e", "f", "f#", "g", "g#", "a", "a#", "b" };

    public static String getScaleString(int index) { return scales[index]; }

    public static int[] genScaleMap(int root) {
        return new int[] { root, root + 2, root + 3, root + 5, root + 7, root + 8, root + 10, root + 12 };
    }

    public static int[] genScaleMap(String root) {
        for (int i = 0; i < 12; i++) {
            if (scales[i].equals(root)) {
                return genScaleMap(i);
            }
        }

        return new int[] {};
    }
}
