package com.awesomeproject

import android.content.Context
import android.hardware.usb.UsbManager
import com.facebook.react.bridge.*
import SecuGen.FDxSDKPro.JSGFPLib
import SecuGen.FDxSDKPro.SGFDxDeviceName

class FingerprintModule(reactContext: ReactApplicationContext) :
    ReactContextBaseJavaModule(reactContext) {

    private val sgfplib: JSGFPLib = JSGFPLib(reactContext,null)

    override fun getName(): String = "FingerprintModule"

    @ReactMethod
    fun initialize(promise: Promise) {
        try {
            
            val error = sgfplib.Init(SGFDxDeviceName.SG_DEV_AUTO.toLong())
            if (error == 0L) {
                promise.resolve("SDK Inicializado")
            } else {
                promise.reject("INIT_ERROR", "Código: $error")
            }
        } catch (e: Exception) {
            promise.reject("INIT_EXCEPTION", e.message)
        }
    }

    @ReactMethod
    fun captureFingerprint(promise: Promise) {
        try {
            val buffer = ByteArray(300 * 400) 
            val error = sgfplib.GetImage(buffer)
            if (error == 0L) {
                promise.resolve(buffer)
            } else {
                promise.reject("CAPTURE_ERROR", "Código: $error")
            }
        } catch (e: Exception) {
            promise.reject("CAPTURE_EXCEPTION", e.message)
        }
    }
}