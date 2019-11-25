package com.bestarmedia.libcommon.util;

import com.bestarmedia.libcommon.config.OkConfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * Created by J Wong on 2015/12/8 19:47.
 */
public class DeviceUtil {

//    public static String getH8CupChipID(Context context) {
//        return getCupSerial();
//        return TextUtils.isEmpty(cupId) ? getDeviceId(context) : cupId;
//    }
//    private static String getDeviceId(Context context) {
//        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//        return tm.getDeviceId();
//    }

    public static String getCupSerial() {
        String cupId = (OkConfig.boxManufacturer() == 2 ? ProcCpuInfo.getChipIDHexH6() : ProcCpuInfo.getChipIDHex())
                .replace("\r\n", "").replace("\n", "");
        return cupId;
    }

    /**
     * 获取网卡 MAC
     *
     * @return
     */
    public static String getMacAddress() {
        String strMacAddr = "";
        try {
            InetAddress ip = getLocalInetAddress();
            if (ip != null) {
                byte[] b = NetworkInterface.getByInetAddress(ip).getHardwareAddress();
                StringBuffer buffer = new StringBuffer();
                for (int i = 0; i < b.length; i++) {
                    if (i != 0) {
                        buffer.append(':');
                    }
                    String str = Integer.toHexString(b[i] & 0xFF);
                    buffer.append(str.length() == 1 ? 0 + str : str);
                }
                strMacAddr = buffer.toString().toLowerCase();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strMacAddr;
    }

    /**
     * 获取移动设备本地IP
     *
     * @return
     */
    private static InetAddress getLocalInetAddress() {
        InetAddress ip = null;
        try {//列举
            Enumeration en_netInterface = NetworkInterface.getNetworkInterfaces();
            while (en_netInterface.hasMoreElements()) {//是否还有元素
                NetworkInterface ni = (NetworkInterface) en_netInterface.nextElement();//得到下一个元素
                Enumeration en_ip = ni.getInetAddresses();//得到一个ip地址的列举
                while (en_ip.hasMoreElements()) {
                    ip = (InetAddress) en_ip.nextElement();
                    if (!ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":") == -1) {
                        break;
                    } else {
                        ip = null;
                    }
                }
                if (ip != null) {
                    break;
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return ip;
    }

    public static int getDeviceTypeByAdPos(String adPos) {
        int Type = -1;
        switch (adPos) {
            case "Z2":
            case "J1":
            case "J2":
            case "T1":
            case "T2":
            case "T3":
            case "T4":
            case "R1":
            case "S1":
            case "S2":
            case "W1":
            case "W2":
            case "H1":
            case "Y1":
                Type = 1;
                break;
            case "Z1":
            case "B1":
            case "B2":
            case "G1":
            case "N1":
                Type = 2;
                break;
            case "P2":
            case "Y2":
                Type = 3;
                break;
            case "M1":
            case "K1":
                Type = 4;
                break;
            case "Z3":
            case "T5":
            case "M2":
                Type = 5;
                break;
        }
        return Type;
    }

    public static void execCommand(String command) throws IOException {
        // start the ls command running
        Runtime runtime = Runtime.getRuntime();
        Process proc = runtime.exec(command);        //这句话就是shell与高级语言间的调用
        //如果有参数的话可以用另外一个被重载的exec方法
        //实际上这样执行时启动了一个子进程,它没有父进程的控制台
        //也就看不到输出,所以我们需要用输出流来得到shell执行后的输出
        InputStream inputstream = proc.getInputStream();
        InputStreamReader inputstreamreader = new InputStreamReader(inputstream);
        BufferedReader bufferedreader = new BufferedReader(inputstreamreader);
        String line = "";
        StringBuilder sb = new StringBuilder(line);
        while ((line = bufferedreader.readLine()) != null) {
            sb.append(line);
            sb.append('\n');
        }
        try {
            if (proc.waitFor() != 0) {
                System.err.println("exit value = " + proc.exitValue());
            }
        } catch (InterruptedException e) {
            System.err.println(e);
            Logger.e("DeviceUtil", "shutdown ex:" + e.toString());
        }
    }


    public static void shutdown() {
        try {
            execCommand("reboot -p");
        } catch (Exception e) {
            Logger.e("DeviceUtil", "shutdown ex:" + e.toString());
            e.printStackTrace();
        }
    }


    public static void reboot() {
        try {
            execCommand("reboot ");
        } catch (Exception e) {
            Logger.e("DeviceUtil", "shutdown ex:" + e.toString());
            e.printStackTrace();
        }
    }
}
