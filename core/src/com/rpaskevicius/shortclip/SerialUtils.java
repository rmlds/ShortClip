package com.rpaskevicius.shortclip;

public class SerialUtils {
    //Taken from https://stackoverflow.com/questions/7619058/convert-a-byte-array-to-integer-in-java-and-vice-versa

    public static final byte[] intToArray(int num) {
        return new byte[] {
                (byte)(num >> 24),
                (byte)(num >> 16),
                (byte)(num >> 8),
                (byte) num
        };
    }

    public static final byte[] intsToArray(int num1, int num2) {
        return new byte[] {
                (byte)(num1 >> 24),
                (byte)(num1 >> 16),
                (byte)(num1 >> 8),
                (byte) num1,

                (byte)(num2 >> 24),
                (byte)(num2 >> 16),
                (byte)(num2 >> 8),
                (byte) num2
        };
    }

    public static final int arrayToInt(byte[] arr) {
        return arr[0] << 24 | (arr[1] & 0xFF) << 16 | (arr[2] & 0xFF) << 8 | (arr[3] & 0xFF);
    }
}
