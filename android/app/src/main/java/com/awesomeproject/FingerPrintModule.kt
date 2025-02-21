package com.awesomeproject

import android.content.Context
import android.hardware.usb.UsbManager
import com.facebook.react.bridge.*
import SecuGen.FDxSDKPro.JSGFPLib
import SecuGen.FDxSDKPro.SGFDxDeviceName

class FingerprintModule(reactContext: ReactApplicationContext) :
    ReactContextBaseJavaModule(reactContext) {

    private val sgfplib: JSGFPLib = JSGFPLib(reactContext.getSystemService(Context.USB_SERVICE) as UsbManager)

    override fun getName(): String {
        return "FingerprintModule"
    }

    @ReactMethod
    fun initialize(promise: Promise) {
        val error = sgfplib.Init(SGFDxDeviceName.SG_DEV_AUTO)
        if (error == 0L) {
            promise.resolve("SDK Inicializado com sucesso")
        } else {
            promise.reject("Erro ao inicializar SDK", error.toString())
        }
    }

    @ReactMethod
    fun captureFingerprint(promise: Promise) {
        val buffer = ByteArray(260 * 300) 
        val error = sgfplib.GetImage(buffer)
        if (error == 0L) {
            promise.resolve("Imagem capturada com sucesso")
        } else {
            promise.reject("Erro ao capturar digital", error.toString())
        }
    }

    @ReactMethod
    fun closeDevice(promise: Promise) {
        val error = sgfplib.Close()
        if (error == 0L) {
            promise.resolve("Dispositivo fechado")
        } else {
            promise.reject("Erro ao fechar dispositivo", error.toString())
        }
    }
}
