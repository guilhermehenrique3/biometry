package com.reactnativefingerprintuareu

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Base64
import android.widget.Toast
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.WritableMap
import asia.kanopi.uareu4500library.Fingerprint
import asia.kanopi.uareu4500library.Status
import java.io.ByteArrayOutputStream

import android.content.Context
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager


class FingerprintUareuModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {

    companion object {
        const val NAME = "FingerPrintUareu"
        private const val E_FAILED_TO_SHOW_FINGER_SCAN = "E_FAILED_TO_SHOW_FINGER_SCAN"
    }

    private var context: ReactApplicationContext = reactContext
    private var img: ByteArray? = null
    private var bm: Bitmap? = null
    private var convertBase64: String? = null
    private var mFingerPromise: Promise? = null
    private var fingerprint: Fingerprint? = null

    override fun getName(): String {
        return NAME
    }

    @ReactMethod
    fun startScan(promise: Promise) {
        fingerprint = Fingerprint()
        mFingerPromise = promise
        fingerprint?.scan(context, printHandler, updateHandler)
    }

    fun getConnectedUsbDevices(promise: Promise) {
        val usbManager = context.getSystemService(Context.USB_SERVICE) as UsbManager
        val deviceList: Map<String, UsbDevice> = usbManager.deviceList
        if (deviceList.isEmpty()) {
            promise.reject("NO_DEVICES", "No USB devices found")
        } else {
            val devicesInfo = StringBuilder()
            for ((_, device) in deviceList) {
                devicesInfo.append("Device: ${device.deviceName}, Vendor ID: ${device.vendorId}, Product ID: ${device.productId}\n")
            }
            promise.resolve(devicesInfo.toString())
        }
    }

    private val updateHandler = Handler(Looper.getMainLooper()) { msg ->
        val map = Arguments.createMap()
        val status = msg.data.getInt("status")
        val message = when (status) {
            Status.INITIALISED -> "Configurando Lector"
            Status.SCANNER_POWERED_ON -> "Lector Encendido"
            Status.READY_TO_SCAN -> "Listo para Escanear Huella"
            Status.FINGER_DETECTED -> "Huella Detectada"
            Status.RECEIVING_IMAGE -> "Imagen Recibida"
            Status.FINGER_LIFTED -> "Se ha quitado el dedo del lector"
            Status.SCANNER_POWERED_OFF -> "Lector Apagado"
            Status.SUCCESS -> "Huella Digital Capturada Exitosamente"
            Status.ERROR -> "Error: ${msg.data.getString("errorMessage")}"
            else -> "Error: ${msg.data.getString("errorMessage")}"
        }
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        true
    }

    private val printHandler = Handler(Looper.getMainLooper()) { msg ->
        val status = msg.data.getInt("status")
        val map: WritableMap = Arguments.createMap()

        if (status == Status.SUCCESS) {
            img = msg.data.getByteArray("img")
            bm = BitmapFactory.decodeByteArray(img, 0, img!!.size)

            val byteArrayOutputStream = ByteArrayOutputStream()
            bm?.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)

            val byteArray = byteArrayOutputStream.toByteArray()

            // ### encoded variable contains the base64 of the fingerprint ###
            val encoded = Base64.encodeToString(byteArray, Base64.DEFAULT)


        
        // a lib de WSQData esta trancada no jitpack
        
            // ### wsqData is optional data for codification in WSQ ###
            // val wsqData = WSQEncoder(bm)
            //     .setBitrate(WSQEncoder.BITRATE_5_TO_1)
            //     .encode()
            // convertBase64 = Base64.encodeToString(wsqData, Base64.DEFAULT)

            // map.putString("convertBase64", convertBase64)

            map.putString("encoded", encoded)
        

            mFingerPromise?.resolve(map)
        } else {
            mFingerPromise?.reject(E_FAILED_TO_SHOW_FINGER_SCAN, "Failed Scan")
        }
        true
    }
}
