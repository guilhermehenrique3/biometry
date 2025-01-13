package com.fingerprintusbapp;

import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import java.nio.ByteBuffer;
import java.util.HashMap;

public class UsbModule extends ReactContextBaseJavaModule {
    private final UsbManager usbManager;

    public UsbModule(ReactApplicationContext reactContext) {
        super(reactContext);
        usbManager = (UsbManager) reactContext.getSystemService(ReactApplicationContext.USB_SERVICE);
    }

    @Override
    public String getName() {
        return "UsbModule";
    }

    @ReactMethod
    public void getConnectedUsbDevices(Promise promise) {
        try {
            HashMap<String, UsbDevice> deviceList = usbManager.getDeviceList();
            StringBuilder devices = new StringBuilder();
            for (UsbDevice device : deviceList.values()) {
                devices.append(device.getDeviceName()).append("\n");
            }
            promise.resolve(devices.toString());
        } catch (Exception e) {
            promise.reject("ERROR", e.getMessage());
        }
    }

    @ReactMethod
    public void startFingerprintScan(Promise promise) {
        try {
            HashMap<String, UsbDevice> deviceList = usbManager.getDeviceList();
            UsbDevice device = null;
            for (UsbDevice d : deviceList.values()) {
                // Escolha o dispositivo correto com base no vendor/product ID
                if (d.getVendorId() == 1234 && d.getProductId() == 5678) {
                    device = d;
                    break;
                }
            }

            if (device == null) {
                promise.reject("ERROR", "Device not found");
                return;
            }

            UsbDeviceConnection connection = usbManager.openDevice(device);
            UsbInterface usbInterface = device.getInterface(0);
            connection.claimInterface(usbInterface, true);

            UsbEndpoint endpointIn = usbInterface.getEndpoint(0);
            UsbEndpoint endpointOut = usbInterface.getEndpoint(1);

            // Enviar comando para iniciar o scanner
            byte[] command = new byte[]{0x01, 0x00, 0x00}; // Exemplo: comando fictício
            connection.bulkTransfer(endpointOut, command, command.length, 1000);

            // Receber dados
            ByteBuffer buffer = ByteBuffer.allocate(endpointIn.getMaxPacketSize());
            int received = connection.bulkTransfer(endpointIn, buffer.array(), buffer.capacity(), 1000);

            if (received > 0) {
                promise.resolve("Fingerprint data received: " + new String(buffer.array(), 0, received));
            } else {
                promise.reject("ERROR", "No data received from device");
            }

            connection.close();
        } catch (Exception e) {
            promise.reject("ERROR", e.getMessage());
        }
    }
}
