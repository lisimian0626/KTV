package com.beidousat.karaoke.service;

import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.beidousat.karaoke.helper.BnsBeaconHelper;
import com.bestarmedia.libserial.HexUtil;
import com.felhr.usbserial.CDCSerialDevice;
import com.felhr.usbserial.UsbSerialDevice;
import com.felhr.usbserial.UsbSerialInterface;

import java.util.HashMap;
import java.util.Map;


/**
 * Based on example from:
 * https://github.com/felHR85/SerialPortExample/blob/master/example/src/main/java/com/felhr/serialportexample/UsbService.java
 */

public class UsbService extends Service {

    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
    public static final String ACTION_USB_READY = "com.felhr.connectivityservices.USB_READY";
    public static final String ACTION_USB_ATTACHED = "android.hardware.usb.action.USB_DEVICE_ATTACHED";
    public static final String ACTION_USB_DETACHED = "android.hardware.usb.action.USB_DEVICE_DETACHED";
    public static final String ACTION_USB_NOT_SUPPORTED = "com.felhr.usbservice.USB_NOT_SUPPORTED";
    public static final String ACTION_NO_USB = "com.felhr.usbservice.NO_USB";
    //    public static final String ACTION_USB_PERMISSION_GRANTED = "com.felhr.usbservice.USB_PERMISSION_GRANTED";
    public static final String ACTION_USB_PERMISSION_NOT_GRANTED = "com.felhr.usbservice.USB_PERMISSION_NOT_GRANTED";
    public static final String ACTION_USB_DISCONNECTED = "com.felhr.usbservice.USB_DISCONNECTED";
    public static final String ACTION_CDC_DRIVER_NOT_WORKING = "com.felhr.connectivityservices.ACTION_CDC_DRIVER_NOT_WORKING";
    public static final String ACTION_USB_DEVICE_NOT_WORKING = "com.felhr.connectivityservices.ACTION_USB_DEVICE_NOT_WORKING";

    public static final String ACTION_USB_OPENING = "com.felhr.usbservice.USB_OPENING";

    public static final int MESSAGE_FROM_SERIAL_PORT = 0;
    public static boolean SERVICE_CONNECTED = false;

    private IBinder binder = new UsbBinder();

    private Context context;
    private Handler mHandler;
    private UsbManager usbManager;
    private UsbDevice device;
    private UsbDeviceConnection connection;
    private UsbSerialDevice serialPort;
    private boolean serialPortConnected;

    public static final String TAG = "UsbService";

    private String mHadWriteHex;

    /*
     *  Data received from serial port will be received here. Just populate onReceivedData with your code
     *  In this particular example. byte stream is converted to String and send to UI thread to
     *  be treated there.
     */
    private UsbSerialInterface.UsbReadCallback mCallback = new UsbSerialInterface.UsbReadCallback() {
        @Override
        public void onReceivedData(byte[] arg0) {
            try {
                Log.d(TAG, "onReceivedData mCallback:" + HexUtil.ByteArrToHex(arg0));
                BnsBeaconHelper.setHaveBeacon(getApplicationContext(), true);
                if (mHandler != null) {
                    mHandler.obtainMessage(MESSAGE_FROM_SERIAL_PORT, HexUtil.ByteArrToHex(arg0)).sendToTarget();
                }
            } catch (Exception e) {
                Log.e(TAG, "onReceivedData mCallback Exception");
            }
        }
    };


    private void openUsbDevice() {
        try {
            Log.d(TAG, "openUsbDevice device:" + device.getManufacturerName());
            if (usbManager.hasPermission(device)) {
                connection = usbManager.openDevice(device);
                Intent intent = new Intent(ACTION_USB_OPENING);
                context.sendBroadcast(intent);
                new ConnectionThread().run();
            } else {
                requestUserPermission();
            }
        } catch (Exception e) {
            Log.e(TAG, "打开Beacon设备出错了", e);
        }
    }

    private final BroadcastReceiver usbReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context arg0, Intent arg1) {
            UsbDevice usbDevice = arg1.getParcelableExtra(UsbManager.EXTRA_DEVICE);
            if (usbDevice != null && "HOLTEK".equals(usbDevice.getManufacturerName())) {
                device = usbDevice;
                Log.d(TAG, "usbReceiver HOLTEK  action:" + arg1.getAction());
                if (ACTION_USB_ATTACHED.equals(arg1.getAction())) {
                    Log.d(TAG, "usbReceiver HOLTEK  :ACTION_USB_ATTACHED");
                    if (!serialPortConnected)
                        findSerialPortDevice(); // A USB device has been attached. Try to open it as a Serial port
                } else if (ACTION_USB_DETACHED.equals(arg1.getAction())) {
                    Log.d(TAG, "usbReceiver HOLTEK  :ACTION_USB_DETACHED");
                    Intent intent = new Intent(ACTION_USB_DISCONNECTED);
                    arg0.sendBroadcast(intent);
                    serialPortConnected = false;
                    BnsBeaconHelper.setHaveBeacon(getApplicationContext(), false);
                    if (serialPort != null) {
                        serialPort.close();
                        serialPort = null;
                    }
                } else if (ACTION_USB_PERMISSION.equals(arg1.getAction())) {
                    if (arg1.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        //user choose YES for your previously popup window asking for grant perssion for this usb device
                        openUsbDevice();
                    } else {
                        //user choose NO for your previously popup window asking for grant perssion for this usb device
                        Toast.makeText(context, "Permission denied for device" + usbDevice, Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    };

    /**
     * Intent to be used to start this service
     */
    public static Intent getIntent(Context context) {
        return new Intent(context, UsbService.class);
    }

    /*
     * Register for USB related broadcasts (USB ATTACHED, USB DETACHED...) and try to open USB port.
     */
    @Override
    public void onCreate() {
        this.context = this;
        serialPortConnected = false;
        UsbService.SERVICE_CONNECTED = true;
        registerReceiver(usbReceiver, getFilter());
        usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
        findSerialPortDevice();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        UsbService.SERVICE_CONNECTED = false;
        unregisterReceiver(usbReceiver);
    }

    /*
     * Write data through Serial Port
     */
    public void write(byte[] data) {
        String beWriteHex = HexUtil.ByteArrToHex(data);
        if (TextUtils.isEmpty(beWriteHex)) {
            Log.d(TAG, "to write beacon hex is empty , be return");
            return;
        }
        if (beWriteHex.equals(mHadWriteHex)) {
            Log.d(TAG, beWriteHex + ":  had be write before , is not need to write again !!!");
            return;
        }
        if (serialPort != null) {
            serialPort.write(data);
            mHadWriteHex = beWriteHex;
            Log.d(TAG, "write serialPort:" + beWriteHex);
        } else {
            Log.w(TAG, "write serialPort is null");
            BnsBeaconHelper.setHaveBeacon(getApplicationContext(), false);
        }
    }

    public void setHandler(Handler mHandler) {
        this.mHandler = mHandler;
    }

    private void findSerialPortDevice() {
        // This snippet will try to open the first encountered usb device connected, excluding usb root hubs
        HashMap<String, UsbDevice> usbDevices;
        try {
            usbDevices = usbManager.getDeviceList();
        } catch (Exception e) {
            Log.e(TAG, "读取usb设备列表出错了", e);
            return;
        }
        if (!usbDevices.isEmpty()) {
            PendingIntent mPermissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);
            for (Map.Entry<String, UsbDevice> entry : usbDevices.entrySet()) {
                UsbDevice device = entry.getValue();
                int deviceVID = device.getVendorId();
                int devicePID = device.getProductId();
                Log.d(TAG, "deviceVID:" + deviceVID);
                Log.d(TAG, "devicePID:" + devicePID);
                Log.d(TAG, "ManufacturerName:" + device.getManufacturerName());
                Log.d(TAG, "DeviceName:" + device.getDeviceName());
                Log.d(TAG, "ProductId:" + device.getProductId());
                if (deviceVID != 0x1d6b && "HOLTEK".equals(device.getManufacturerName()) && (devicePID != 0x0001 || devicePID != 0x0002 || devicePID != 0x0003)) {
                    this.device = device;
                    if (usbManager.hasPermission(device)) {
                        Log.d(TAG, "findSerialPortDevice  openUsbDevice");
                        openUsbDevice();
                        return;
                    } else {
                        usbManager.requestPermission(device, mPermissionIntent);
                    }
                }
            }
        } else {
            Log.d(TAG, "findSerialPortDevice  usbDevices is empty");
            // There is no USB devices connected. Send an intent to MainActivity
            Intent intent = new Intent(ACTION_NO_USB);
            sendBroadcast(intent);
        }
    }

    private IntentFilter getFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_USB_PERMISSION);
        filter.addAction(ACTION_USB_DETACHED);
        filter.addAction(ACTION_USB_ATTACHED);
        return filter;
    }

    /*
     * Request user permission. The response will be received in the BroadcastReceiver
     */
    private void requestUserPermission() {
        PendingIntent mPendingIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);
        usbManager.requestPermission(device, mPendingIntent);
    }


    public class UsbBinder extends Binder {
        public UsbService getService() {
            return UsbService.this;
        }
    }

    /*
     * A simple thread to open a serial port.
     * Although it should be a fast operation. moving usb operations away from UI thread is a good thing.
     */
    private class ConnectionThread extends Thread {
        @Override
        public void run() {
            Log.d(TAG, "USB ConnectionThread running !!! ");
            try {
                if (device == null || connection == null || !"HOLTEK".equals(device.getManufacturerName())) {
                    return;
                }
                serialPort = UsbSerialDevice.createUsbSerialDevice(device, connection);
                Log.d(TAG, "ConnectionThread createUsbSerialDevice:" + serialPort);
                if (serialPort != null) {
                    serialPortConnected = true;
                    if (serialPort.open()) {
                        serialPort.setBaudRate(115200);
                        serialPort.setDataBits(UsbSerialInterface.DATA_BITS_8);
                        serialPort.setStopBits(UsbSerialInterface.STOP_BITS_1);
                        serialPort.setParity(UsbSerialInterface.PARITY_NONE);
                        serialPort.setFlowControl(UsbSerialInterface.FLOW_CONTROL_OFF);
                        serialPort.read(mCallback);
                        // Everything went as expected. Send an intent to MainActivity
                        Intent intent = new Intent(ACTION_USB_READY);
                        context.sendBroadcast(intent);
                        BnsBeaconHelper.setHaveBeacon(getApplicationContext(), true);
                    } else {
                        // Serial port could not be opened, maybe an I/O error or if CDC driver was chosen, it does not really fit
                        // Send an Intent to Main Activity
                        if (serialPort instanceof CDCSerialDevice) {
                            Intent intent = new Intent(ACTION_CDC_DRIVER_NOT_WORKING);
                            context.sendBroadcast(intent);
                        } else {
                            Intent intent = new Intent(ACTION_USB_DEVICE_NOT_WORKING);
                            context.sendBroadcast(intent);
                        }
                    }
                } else {
                    // No driver for given device, even generic CDC driver could not be loaded
                    Intent intent = new Intent(ACTION_USB_NOT_SUPPORTED);
                    context.sendBroadcast(intent);
                }
            } catch (Exception e) {
                Log.e(TAG, "打开beacon异常！", e);
            }
        }
    }
}
