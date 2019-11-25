package com.demo;

import android.util.Log;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SerialPort {
    /**
     * 串口文件描述符，禁止删除或重命名，因为native层关闭串口时需要使用
     */
    private FileDescriptor mFd;
    /**
     * 输入流，用于接收串口数据
     */
    private FileInputStream mFileInputStream;
    /**
     * 输出流，用于发送串口数据
     */
    private FileOutputStream mFileOutputStream;

    /**
     * 构造函数
     *
     * @param device   串口名
     * @param baudrate 波特率
     * @param flags    操作标识
     * @throws IOException       IO异常，开启串口失败时触发
     */
    public SerialPort(File device, int baudrate, int flags) throws SecurityException, IOException {
        /* 检测设备管理权限，即文件的权限属性 */
//		if (!device.canRead() || !device.canWrite()) {
//			try {
//				/* 若没有读/写权限，试着chmod该设备 */
//				Process su;
//				su = Runtime.getRuntime().exec("/system/bin/su");
//				String cmd = "chmod 666 " + device.getAbsolutePath() + "\n" + "exit\n";
//				su.getOutputStream().write(cmd.getBytes());
//				if ((su.waitFor() != 0) || !device.canRead() || !device.canWrite()) {
//					throw new SecurityException();
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//				throw new SecurityException();
//			}
//		}
        try {
            mFd = open(device.getAbsolutePath(), baudrate, flags);
            if (mFd == null) {
                return;
            }
            mFileInputStream = new FileInputStream(mFd);
            mFileOutputStream = new FileOutputStream(mFd);
        } catch (Exception ex) {
            Log.w("SerialPort", ex.toString());
        }
    }

    /**
     * 获取输入流
     *
     * @return 串口输入流
     */
    public InputStream getInputStream() {
        return mFileInputStream;
    }

    /**
     * 获取输出流
     *
     * @return 串口输出流
     */
    public OutputStream getOutputStream() {
        return mFileOutputStream;
    }

    /**
     * 原生函数，开启串口虚拟文件
     *
     * @param path     串口虚拟文件路径
     * @param baudrate 波特率
     * @param flags    操作标识
     * @return
     */
    private native static FileDescriptor open(String path, int baudrate, int flags);

    /**
     * 原生函数，关闭串口虚拟文件
     */
    public native void close();

    static {
        System.loadLibrary("serialPort");
    }
}
