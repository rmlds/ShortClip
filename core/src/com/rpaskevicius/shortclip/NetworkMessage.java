package com.rpaskevicius.shortclip;

import java.util.Arrays;

public class NetworkMessage {

    private byte[] header = new byte[2];
    private byte[] data = new byte[256];

    private byte length;

    public NetworkMessage() {

    }

    public void build(int action, int param) {
        build(action, param, 0);
    }

    public void build(int action, int param, int coreLength) {
        setLength((byte)(coreLength + 2)); //action and param is not included in coreLength
        encodeHeader();

        setAction((byte) action);
        setParam((byte) param);
    }

    public void encodeHeader() { header[0] = 0; header[1] = length; }
    public void decodeHeader() { length = header[1]; }

    public byte[] getHeader() { return this.header; }
    public byte[] getData() { return this.data; }

    public void writeStr(String str) { writeStr(str, 0); }

    public void writeStr(String str, int position) {
        try {
            byte[] arr = str.getBytes("US-ASCII");

            for (int i = 0; i < arr.length; i++) {
                this.data[2 + position + i] = arr[i];
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String readStr(int length) { return readStr(length, 0); }

    public String readStr(int length, int position) {
        String str = "";

        for (int i = 0; i < length; i++) {
            str += (char)(this.data[2 + position + i]);
        }

        return str;
    }

    public void writeInt(int num, int position) {
        this.data[2 + position + 0] = (byte)(num >> 24);
        this.data[2 + position + 1] = (byte)(num >> 16);
        this.data[2 + position + 2] = (byte)(num >> 8);
        this.data[2 + position + 3] = (byte) num;
    }

    public int readInt(int position) {
        return this.data[2 + position + 0] << 24 |
                (this.data[2 + position + 1] & 0xFF) << 16 |
                (this.data[2 + position + 2] & 0xFF) << 8 |
                (this.data[2 + position + 3] & 0xFF);
    }

    public void writeByte(int b, int position) { writeByte((byte) b, position); }

    public void writeByte(byte b, int position) {
        this.data[2 + position] = b;
    }

    public byte readByte(int position) {
        return this.data[2 + position];
    }

    public String debug(int coreLength) {
        String output = "";

        for (int i = 0; i < coreLength; i++) {
            output += (this.data[i + 2] & 0xFF) + " ";
        }

        return output;
    }

    public void setLength(byte length) { this.length = length; }
    public byte getLength() { return this.length; }

    public void setAction(byte action) { data[0] = action; }
    public void setParam(byte param) { data[1] = param; }

    public byte getAction() { return data[0]; }
    public byte getParam() { return data[1]; }
}
