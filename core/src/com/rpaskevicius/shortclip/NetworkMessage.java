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

    public void writeHeader(byte[] array) {
        for (int i = 0; i < 2; i++) {
            this.header[i] = array[i];
        }
    }

    public void writeData(byte[] array) {
        for (int i = 0; i < array.length; i++) {
            this.data[i] = array[i];
        }
    }

    public void writeCore(byte[] array, int offset) {
        for (int i = 0; i < array.length; i++) {
            this.data[i + 2 + offset] = array[i];
        }
    }

    public String readCore(int length) {
        String output = "";

        for (int i = 0; i < length; i++) {
            output += (char)(this.data[i + 2]);
        }

        return output;
    }

    public String debugCore(int length) {
        String output = "";

        for (int i = 0; i < length; i++) {
            output += (int)(this.data[i + 2]) + " ";
        }

        return output;
    }

    public byte[] getCore(int start, int length) {
        return Arrays.copyOfRange(this.data, 2 + start, 2 + start + length);
    }

    public void setLength(byte length) { this.length = length; }
    public byte getLength() { return this.length; }

    public void setAction(byte action) { data[0] = action; }
    public void setParam(byte param) { data[1] = param; }

    public byte getAction() { return data[0]; }
    public byte getParam() { return data[1]; }
}
