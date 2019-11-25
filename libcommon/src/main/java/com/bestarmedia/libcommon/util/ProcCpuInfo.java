package com.bestarmedia.libcommon.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;

public class ProcCpuInfo {

    /* get 32 bit string */
    public static String getChipIDHex() {

        FileInputStream fis = null;
        String chipid = null;
        StringBuilder cpuinfo = new StringBuilder();

        try {
            fis = new FileInputStream("/proc/cpuinfo");
            byte[] buf = new byte[1024];
            int len = 0;

            while ((len = fis.read(buf)) > 0) {
                cpuinfo.append(new String(buf, 0, len));
            }
            chipid = cpuinfo.toString();
            if (fis != null)
                fis.close();
        } catch (IOException io) {
            io.printStackTrace();
        }

        int index = chipid.indexOf("Serial");
        chipid = chipid.substring(index);
        index = chipid.indexOf(": ");
        chipid = chipid.substring(index + 2);
        return chipid;
    }

    /* get 128 bit string */
    public static String getChipID() {
        StringBuilder chipId = new StringBuilder();
        int intValue = 0;

        String hexString = getChipIDHex();
        // String hexString="01234567890123456789012345678901"; /* for test */
        // String hexString="f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0"; /* for test */
        int hexLen = hexString.length();
        for (int i = 0; i < hexLen; i++) {
            int k;
            intValue = Integer.parseInt(hexString.substring(i, i + 1), 16);

            k = (intValue & 8) >> 3;
            chipId.append(k);

            k = (intValue & 4) >> 2;
            chipId.append(k);

            k = (intValue & 2) >> 1;
            chipId.append(k);

            k = intValue & 1;
            chipId.append(k);
        }

        return chipId.toString();
    }


    /* get 32 bit string */
    public static String getChipTemp() {
        FileInputStream fis = null;
        String chipid = null;
        StringBuilder cpuinfo = new StringBuilder();
        try {
            fis = new FileInputStream("/sys/devices/ff280000.tsadc/temp1_input");
            byte[] buf = new byte[1024];
            int len = 0;
            while ((len = fis.read(buf)) > 0) {
                cpuinfo.append(new String(buf, 0, len));
            }
            chipid = cpuinfo.toString();
            if (fis != null)
                fis.close();
        } catch (IOException io) {
            io.printStackTrace();
        }
        return chipid;
    }

    private static final String CPU_INFO = "/proc/cpuinfo";
    private static final String SERIAL = "serial";
    private static final String SPLIT = ":";

    /**
     * Get the Chip ID in string of hex
     *
     * @return the chip id string of hex
     */
    public static String getChipIDHexH6() {
        String chipSerial = null;
        FileReader fr = null;
        BufferedReader br = null;
        try {
            fr = new FileReader(CPU_INFO);
            br = new BufferedReader(fr);

            String readline = null;

            while ((readline = br.readLine()) != null) {
                if (readline.trim().toLowerCase().startsWith(SERIAL)) {
                    chipSerial = readline;
                    break;
                }
            }
        } catch (IOException io) {
            return null;
        } finally {
            if (fr != null) {
                try {
                    fr.close();
                } catch (IOException e) {
                }
            }
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                }
            }
        }

        String serialSplit[] = chipSerial != null ? chipSerial.split(SPLIT) : null;
        if (serialSplit != null && serialSplit.length == 2) {
            return serialSplit[1].trim();
        }
        return null;
    }

}
