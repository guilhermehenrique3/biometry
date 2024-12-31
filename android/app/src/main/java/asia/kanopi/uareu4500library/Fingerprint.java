package asia.kanopi.uareu4500library;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.util.HashMap;
import java.util.Iterator;

public class Fingerprint {

    private UruConnection reader;
    private UsbManager usbManager;
    private Context context;
    private Handler imageHandler;
    private Handler updateHandler;
    private Status status;
    private static boolean deviceRegistered;

    private static final int U_ARE_U_4500B_PRODUCT_ID = 10;
    private static final int U_ARE_U_4500B_VENDOR_ID = 1466;


    private static final String ACTION_USB_PERMISSION = "asia.kanopi.USB_PERMISSION";
    private static final String LOG_TAG = "Fingerprint";

    public Fingerprint() {
        status = new Status();
        status.setStatus(Status.INITIALISED);
        deviceRegistered = false;
    }

    public void scan(Context context, Handler imageHandler) {
        this.context = context;
        this.imageHandler = imageHandler;
        this.updateHandler = null;
        connectToReader();
    }

    public void scan(Context context, Handler imageHandler, Handler updateHandler) {
        this.context = context;
        this.imageHandler = imageHandler;
        this.updateHandler = updateHandler;
        connectToReader();
    }

    public int getStatus() {
        return status.getStatus();
    }

    private void setStatus(int code, String message) {
        status.setStatus(code, message);
        sendUpdate();
    }

    private void sendUpdate() {
        if (updateHandler != null) {
            Message msg = updateHandler.obtainMessage();
            Bundle bundle = new Bundle();
            bundle.putInt("status", status.getStatus());
            if (status.getStatus() == Status.ERROR) {
                bundle.putString("errorMessage", status.getErrorMessage());
            }
            msg.setData(bundle);
            updateHandler.sendMessage(msg);
        }
    }

    private void connectToReader() {
        reader = new UruConnection();
        usbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
        boolean deviceFound = false;
        HashMap<String, UsbDevice> deviceList = usbManager.getDeviceList();
    
        if (deviceList.isEmpty()) {
            setStatus(Status.ERROR, "No USB devices found");
        } else {
            Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
            PendingIntent mPermissionIntent = PendingIntent.getBroadcast(
                    context, 0, new Intent(ACTION_USB_PERMISSION), PendingIntent.FLAG_IMMUTABLE);
    
            // Registra o receiver de permissão USB se ainda não estiver registrado
            if (!deviceRegistered) {
                IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
                context.registerReceiver(mUsbReceiver, filter);
                deviceRegistered = true;
            }
    
            // Itera sobre os dispositivos USB encontrados
            while (deviceIterator.hasNext()) {
                UsbDevice device = deviceIterator.next();
                deviceFound = true;
    
                // Solicita permissão para o dispositivo
                usbManager.requestPermission(device, mPermissionIntent);
            }
    
            if (!deviceFound) {
                setStatus(Status.ERROR, "No USB devices matched");
            }
        }
    }

    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if (device != null) {
                            Log.d(LOG_TAG, "Permission granted for device: " + device.getDeviceName());
                            initiateCommunication(device);
                        } else {
                            Log.e(LOG_TAG, "Device is null after permission granted");
                            setStatus(Status.ERROR, "Device is null after permission granted");
                        }
                    } else {
                        if (device != null) {
                            String deviceName = device.getDeviceName();
                            String deviceVendorId = String.valueOf(device.getVendorId());
                            String deviceProductId = String.valueOf(device.getProductId());
                            String errorMessage = String.format(
                                "Permission denied for device: %s, Vendor ID: %s, Product ID: %s",
                                deviceName, deviceVendorId, deviceProductId
                            );
                            Log.e(LOG_TAG, errorMessage);
                            setStatus(Status.ERROR, errorMessage);
                        } else {
                            Log.e(LOG_TAG, "Device is null after permission denied");
                            setStatus(Status.ERROR, "Device is null after permission denied");
                        }
                    }
                }
            }
        }
    };
    
    

    private void initiateCommunication(UsbDevice usbDevice) {
        try {
            Thread t;
            UsbInterface uru_interface = usbDevice.getInterface(0);
            final UsbDeviceConnection uru_connection = usbManager.openDevice(usbDevice);
            uru_connection.claimInterface(uru_interface, true);

            reader.m_connection = uru_connection;

            // Set up listener in new thread
            ScanFinger r = new ScanFinger(reader, usbDevice, status, imageHandler,
                    updateHandler, context);
            t = new Thread(r);
            t.start();

            reader.init_reader(uru_connection);

        } catch (Exception e) {
            Log.e (LOG_TAG, "Error: " + e.getMessage());
        }
    }

    public void unregisterDevice(Context context) {
        if (deviceRegistered) {
            context.unregisterReceiver(mUsbReceiver);
            deviceRegistered = false;
        }
    }

    public void turnOffReader() {
        if (deviceRegistered) {
            reader.turnScannerOff();
            unregisterDevice(context);
        }
    }
}
